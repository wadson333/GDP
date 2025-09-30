/* eslint-disable @typescript-eslint/prefer-nullish-coalescing */
import { Component, NgZone, OnInit, inject } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule } from '@angular/forms';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { ILabTestCatalog } from '../lab-test-catalog.model';
import { EntityArrayResponseType, LabTestCatalogService } from '../service/lab-test-catalog.service';
import { LabTestCatalogDeleteDialogComponent } from '../delete/lab-test-catalog-delete-dialog.component';

import { MenuItem } from 'primeng/api';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { ConfirmationService, MessageService } from 'primeng/api';
import { TimelineModule } from 'primeng/timeline';
import { TagModule } from 'primeng/tag';
import { DropdownModule } from 'primeng/dropdown';
import { MenuModule } from 'primeng/menu';
import { ToolbarModule } from 'primeng/toolbar';
import { SplitButtonModule } from 'primeng/splitbutton';
import { TooltipModule } from 'primeng/tooltip';
import { LabTestType } from 'app/entities/enumerations/lab-test-type.model';
import { MultiSelectModule } from 'primeng/multiselect';
import { LabTestMethod } from 'app/entities/enumerations/lab-test-method.model';
import { LabTestCatalogUpdateComponent } from '../update/lab-test-catalog-update.component';
import { SkeletonModule } from 'primeng/skeleton';
import { ChipModule } from 'primeng/chip';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { TranslateService } from '@ngx-translate/core';
import { LabTestCatalogDetailComponent } from '../detail/lab-test-catalog-detail.component';
import { SearchCriteria } from '../search-criteria.model';
import { CardModule } from 'primeng/card';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ToggleButtonModule } from 'primeng/togglebutton';
import { InputTextModule } from 'primeng/inputtext';
import { DividerModule } from 'primeng/divider';
import { ExportService } from 'app/shared/services/export-file.service';

interface DialogParams {
  dialog?: 'view-ltc' | 'add-ltc' | 'edit-ltc';
  'selected-lab'?: string | number;
  'is-new'?: boolean;
  tab?: 'details' | 'history';
}
@Component({
  standalone: true,
  selector: 'jhi-lab-test-catalog',
  templateUrl: './lab-test-catalog.component.html',
  styleUrls: ['./lab-test-catalog.component.scss'],
  imports: [
    RouterModule,
    FormsModule,
    SharedModule,
    SortDirective,
    SortByDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    ItemCountComponent,
    TableModule,
    DialogModule,
    ButtonModule,
    TimelineModule,
    TagModule,
    DropdownModule,
    MenuModule,
    ToolbarModule,
    SplitButtonModule,
    TooltipModule,
    MultiSelectModule,
    LabTestCatalogUpdateComponent,
    SkeletonModule,
    ChipModule,
    ToastModule,
    ConfirmDialogModule,
    LabTestCatalogDetailComponent,
    CardModule,
    ProgressSpinnerModule,
    ToggleButtonModule,
    InputTextModule,
    DividerModule,
  ],
  providers: [MessageService, ConfirmationService],
})
export class LabTestCatalogComponent implements OnInit {
  subscription: Subscription | null = null;
  labTestCatalogs?: ILabTestCatalog[];
  isLoading = false;

  sortState = sortStateSignal({});

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  selectedCatalog: ILabTestCatalog | null = null;
  displayFormDialog = false;
  displayHistoryDialog = false;
  displayVersionDialog = false;
  isNewLab = false;
  metaKey = true;

  typesOptions = Object.values(LabTestType).map(type => ({ label: type, value: type }));
  methodsOptions = Object.values(LabTestMethod).map(method => ({ label: method, value: method }));
  statusOptions = [
    { label: 'Actif', value: true },
    { label: 'Inactif', value: false },
  ];

  menuItems: MenuItem[] = [
    {
      label: 'Export',
      items: [
        { label: 'CSV', icon: 'pi pi-file', command: () => this.exportCSV() },
        { label: 'Excel', icon: 'pi pi-file-excel', command: () => this.exportExcel() },
        { label: 'PDF', icon: 'pi pi-file-pdf', command: () => this.exportPDF() },
      ],
    },
  ];

  viewMode: 'table' | 'card' = 'table';
  searchCriteria: SearchCriteria = {};
  showLatestOnly = false;

  gridColumns = 4;
  gridOptions = [
    { label: '3 Columns', value: 3 },
    { label: '4 Columns', value: 4 },
    { label: '5 Columns', value: 5 },
  ];

  public router = inject(Router);
  protected labTestCatalogService = inject(LabTestCatalogService);
  protected activatedRoute = inject(ActivatedRoute);
  protected sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);
  private exportService = inject(ExportService);

  trackId = (item: ILabTestCatalog): number => this.labTestCatalogService.getLabTestCatalogIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();
  }

  // delete(labTestCatalog: ILabTestCatalog): void {
  //   const modalRef = this.modalService.open(LabTestCatalogDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
  //   modalRef.componentInstance.labTestCatalog = labTestCatalog;
  //   // unsubscribe not needed because closed completes on modal close
  //   modalRef.closed
  //     .pipe(
  //       filter(reason => reason === ITEM_DELETED_EVENT),
  //       tap(() => this.load()),
  //     )
  //     .subscribe();
  // }

  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(this.page, event);
  }

  navigateToPage(page: number): void {
    this.handleNavigation(page, this.sortState());
  }
  // ---- manage lab test catalog detail dialog
  showDetailsDialog(catalog: ILabTestCatalog): void {
    // this.clearDialogParams();
    this.selectedCatalog = catalog;
    this.displayVersionDialog = true;
    this.updateUrlParams({ dialog: 'view-ltc', 'selected-lab': catalog.id, 'is-new': false });
  }
  closeDetailDialog(): void {
    this.displayVersionDialog = false;
    this.selectedCatalog = null;
    this.clearDialogParams();
  }

  onDialogHide(): void {
    this.selectedCatalog = null;
    this.clearDialogParams();
  }

  exportCSV(): void {
    if (!this.labTestCatalogs) return;

    // Define columns for export
    const columns = [
      { header: 'Name', key: 'name' },
      { header: 'Type', key: 'type' },
      { header: 'Method', key: 'method' },
      { header: 'Unit', key: 'unit' },
      { header: 'Cost', key: 'cost' },
      { header: 'Turnaround Time', key: 'turnaroundTime' },
      { header: 'Status', key: 'active' },
    ];

    this.exportService.exportToExcel(this.labTestCatalogs, 'lab-test-catalog-export');
  }

  exportExcel(): void {
    if (!this.labTestCatalogs) return;
    this.exportService.exportToExcel(this.labTestCatalogs, 'lab-test-catalog-export');
  }

  exportPDF(): void {
    if (!this.labTestCatalogs) return;

    const columns = [
      { header: 'Name', key: 'name' },
      { header: 'Type', key: 'type' },
      { header: 'Method', key: 'method' },
      { header: 'Unit', key: 'unit' },
      { header: 'Cost', key: 'cost' },
      { header: 'TAT', key: 'turnaroundTime' },
    ];

    this.exportService.exportToPdf(this.labTestCatalogs, 'Laboratory Test Catalog', 'lab-test-catalog-export', columns);
  }

  showAddLabTestCatalogDialog(): void {
    this.selectedCatalog = null;
    this.displayFormDialog = true;
    this.isNewLab = true;
    this.router.navigate([], {
      queryParams: { dialog: 'add-lab', 'selected-lab': 'new', 'is-new': true },
      queryParamsHandling: 'merge',
    });
  }

  showEditLabTestCatalogDialog(catalog: ILabTestCatalog): void {
    this.selectedCatalog = catalog;
    this.displayFormDialog = true;
    this.isNewLab = false;
    this.router.navigate([], {
      queryParams: { dialog: 'edit-lab', 'selected-lab': catalog.id, 'is-new': false },
      queryParamsHandling: 'merge',
    });
  }

  closeFormDialog(success: boolean): void {
    this.displayFormDialog = false;
    this.selectedCatalog = null;
    if (success) {
      this.load();
    }
    this.clearDialogParams();
  }

  toggleView(): void {
    this.viewMode = this.viewMode === 'table' ? 'card' : 'table';
  }

  search(): void {
    this.isLoading = true;
    if (this.showLatestOnly) {
      this.loadLatestVersions();
    } else {
      this.searchWithCriteria();
    }
  }

  clearSearch(): void {
    this.searchCriteria = {};
    this.load();
  }
  getGridCols(): string {
    return `col-12 md:col-6 lg:col-${12 / this.gridColumns}`;
  }

  hasActiveFilters(): boolean {
    return !!(
      this.searchCriteria.name ||
      this.searchCriteria.type ||
      this.searchCriteria.method ||
      this.searchCriteria.active !== undefined
    );
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.labTestCatalogs = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: ILabTestCatalog[] | null): ILabTestCatalog[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    const { page } = this;

    this.isLoading = true;
    const pageToLoad: number = page;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    return this.labTestCatalogService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page: number, sortState: SortState): void {
    const queryParamsObj = {
      page,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(sortState),
    };

    this.ngZone.run(() => {
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    });
  }

  // protected handleDialogFromUrl(params: ParamMap): void {
  //   const dialog = params.get('dialog');
  //   const selectedLab = params.get('selected-lab');

  //   if (dialog && selectedLab) {
  //     switch (dialog) {
  //       case 'view-ltc': {
  //         const lab = this.labTestCatalogs?.find(l => l.id.toString() === selectedLab);
  //         if (lab) {
  //           this.showDetailsDialog(lab);
  //         }
  //         break;
  //       }
  //       case 'add-lab':
  //         this.showAddLabTestCatalogDialog();
  //         break;
  //       // Add other cases as needed
  //     }
  //   }
  // }

  protected updateUrlParams(params: DialogParams): void {
    this.router.navigate([], {
      queryParams: params,
      queryParamsHandling: 'merge',
    });
  }

  protected clearDialogParams(): void {
    this.updateUrlParams({
      dialog: undefined,
      'selected-lab': undefined,
      'is-new': undefined,
      tab: undefined,
    });
  }

  protected searchWithCriteria(): void {
    const queryObject: any = {
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    Object.entries(this.searchCriteria).forEach(([key, value]) => {
      if (value !== undefined && value !== null) {
        queryObject[key] = value;
      }
    });

    this.labTestCatalogService.search(queryObject).subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  protected loadLatestVersions(): void {
    const queryObject = {
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(this.sortState()),
    };

    this.labTestCatalogService.getLatestVersions(queryObject).subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }
}
