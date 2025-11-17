import { Component, NgZone, OnInit, inject } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subject, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule } from '@angular/forms';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { IMedication } from '../medication.model';
import { EntityArrayResponseType, MedicationService } from '../service/medication.service';
import { MedicationDeleteDialogComponent } from '../delete/medication-delete-dialog.component';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TranslateModule } from '@ngx-translate/core';
import { DialogModule } from 'primeng/dialog';
import { ConfirmationService, MenuItem, MessageService } from 'primeng/api';
import { ToolbarModule } from 'primeng/toolbar';
import { TooltipModule } from 'primeng/tooltip';
import { ExportService } from 'app/shared/services/export-file.service';
import { UserPreferencesService } from 'app/shared/services/list-view-preferences.service';
import { CardModule } from 'primeng/card';
import { PaginatorModule } from 'primeng/paginator';
import { BadgeModule } from 'primeng/badge';
import { DropdownModule } from 'primeng/dropdown';
import { MenuModule } from 'primeng/menu';
import { SkeletonModule } from 'primeng/skeleton';
import { PrescriptionStatus } from 'app/entities/enumerations/prescription-status.model';
import { RiskLevel } from 'app/entities/enumerations/risk-level.model';
import { RouteAdmin } from 'app/entities/enumerations/route-admin.model';
import { SearchCriteria } from '../search-criteria.model';
import { DividerModule } from 'primeng/divider';
import { ToastModule } from 'primeng/toast';
import { animate, style, transition, trigger } from '@angular/animations';
import { MedicationDetailComponent } from '../detail/medication-detail.component';
import { MedicationUpdateComponent } from '../update/medication-update.component';

interface DialogParams {
  dialog?: 'view-medoc' | 'add-medoc' | 'edit-medoc';
  'selected-medoc'?: string | number;
  'is-new'?: boolean;
  tab?: 'details' | 'history';
}

@Component({
  standalone: true,
  selector: 'jhi-medication',
  templateUrl: './medication.component.html',
  styleUrls: ['./medication.component.scss'],
  imports: [
    RouterModule,
    FormsModule,
    SharedModule,
    FormatMediumDatePipe,
    TableModule,
    ButtonModule,
    InputTextModule,
    ToolbarModule,
    TranslateModule,
    DialogModule,
    TooltipModule,
    CardModule,
    PaginatorModule,
    BadgeModule,
    DropdownModule,
    MenuModule,
    SkeletonModule,
    DividerModule,
    ToastModule,
    MedicationDetailComponent,
    MedicationUpdateComponent,
  ],
  providers: [SortService, MessageService, ConfirmationService],
  animations: [
    trigger('slideDown', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(-10px)' }),
        animate('200ms ease-out', style({ opacity: 1, transform: 'translateY(0)' })),
      ]),
      transition(':leave', [animate('200ms ease-in', style({ opacity: 0, transform: 'translateY(-10px)' }))]),
    ]),
  ],
})
export class MedicationComponent implements OnInit {
  readonly STORAGE_KEY = 'medication-preferences';
  viewMode: 'table' | 'card' = 'table';
  subscription: Subscription | null = null;
  medications?: IMedication[];
  isLoading = false;

  sortState = sortStateSignal({});

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

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

  // Add these properties
  searchCriteria: SearchCriteria = {};
  showAdvancedSearch = false;

  // Add these options for dropdowns
  routeOptions = Object.values(RouteAdmin).map(value => ({
    label: value,
    value,
  }));

  prescriptionStatusOptions = Object.values(PrescriptionStatus).map(preStatut => ({ label: preStatut, value: preStatut }));
  riskLevelOptions = Object.values(RiskLevel).map(riskLevel => ({ label: riskLevel, value: riskLevel }));

  statusOptions = [
    { label: 'all', value: null },
    { label: 'active', value: true },
    { label: 'inactive', value: false },
  ];

  selectedMedication: IMedication | null = null;
  displayDetailDialog = false;
  displayFormDialog = false;

  public router = inject(Router);
  protected medicationService = inject(MedicationService);
  protected activatedRoute = inject(ActivatedRoute);
  protected sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);
  protected exportService = inject(ExportService);
  protected userPreferences = inject(UserPreferencesService);

  trackId = (item: IMedication): number => this.medicationService.getMedicationIdentifier(item);

  ngOnInit(): void {
    const preferences = this.userPreferences.getViewPreferences(this.STORAGE_KEY);
    this.viewMode = preferences.viewMode;
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();
  }

  delete(medication: IMedication): void {
    const modalRef = this.modalService.open(MedicationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.medication = medication;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  toggleView(): void {
    this.viewMode = this.viewMode === 'table' ? 'card' : 'table';
    this.userPreferences.saveViewPreferences(this.STORAGE_KEY, { viewMode: this.viewMode });
  }

  onPageChange(event: any): void {
    // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
    this.page = event.page + 1;
    this.itemsPerPage = event.rows;
    this.load();
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(this.page, event);
  }

  navigateToPage(page: number): void {
    this.handleNavigation(page, this.sortState());
  }

  exportCSV(): void {
    if (!this.medications) return;

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

    this.exportService.exportToExcel(this.medications, 'medication-export');
  }

  exportExcel(): void {
    if (!this.medications) return;
    this.exportService.exportToExcel(this.medications, 'medication-export');
  }

  exportPDF(): void {
    if (!this.medications) return;

    const columns = [
      { header: 'Name', key: 'name' },
      { header: 'International Name', key: 'internationalName' },
      { header: 'Manufacturer', key: 'manufacturer' },
      { header: 'Strength', key: 'strength' },
      { header: 'Formulation', key: 'formulation' },
      { header: 'Unit Price', key: 'unitPrice' },
      { header: 'Expiry Date', key: 'expiryDate' },
      { header: 'Risk Level', key: 'riskLevel' },
    ];

    this.exportService.exportToPdf(this.medications, 'Medication', 'medication-export', columns, 'landscape');
  }

  // Add these methods
  onSearch(): void {
    this.page = 1;
    this.load();
  }

  clearSearch(): void {
    this.searchCriteria = {};
    this.load();
  }

  hasActiveFilters(): boolean {
    return Object.values(this.searchCriteria).some(value => value !== undefined && value !== '' && value !== null);
  }

  toggleAdvancedSearch(): void {
    this.showAdvancedSearch = !this.showAdvancedSearch;
  }

  onStatusChange(value: boolean | null): void {
    if (value !== null) {
      this.searchCriteria.active = value;
    }
    this.onSearch();
  }

  showMedicationDetails(medication: IMedication): void {
    this.selectedMedication = medication;
    this.displayDetailDialog = true;

    this.updateUrlParams({
      dialog: 'view-medoc',
      'selected-medoc': medication.id,
      'is-new': false,
      tab: 'details',
    });
  }

  closeDetailDialog(): void {
    this.displayDetailDialog = false;
    this.selectedMedication = null;
    this.clearDialogParams();
  }

  showAddMedicationFormDialog(): void {
    this.displayFormDialog = true;

    this.updateUrlParams({
      dialog: 'add-medoc',
      'is-new': true,
      tab: 'details',
    });
  }

  closeFormDialog(): void {
    this.displayFormDialog = false;
    this.selectedMedication = null;
    this.clearDialogParams();
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.medications = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IMedication[] | null): IMedication[] {
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
      // Add search criteria
      ...this.buildQueryParams(this.searchCriteria),
    };
    console.error(queryObject);
    return this.medicationService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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
  protected buildQueryParams(criteria: SearchCriteria): any {
    const params: any = {};

    if (criteria.name) params['name.contains'] = criteria.name;
    if (criteria.codeAtc) params['codeAtc.contains'] = criteria.codeAtc;
    if (criteria.formulation) params['formulation.contains'] = criteria.formulation;
    if (criteria.strength) params['strength.contains'] = criteria.strength;
    if (criteria.manufacturer) params['manufacturer.contains'] = criteria.manufacturer;
    if (criteria.unitPriceMin != null) params['unitPrice.greaterThanOrEqual'] = criteria.unitPriceMin;
    if (criteria.unitPriceMax != null) params['unitPrice.lessThanOrEqual'] = criteria.unitPriceMax;
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    if (criteria.active !== undefined && criteria.active != null) params['active.equals'] = criteria.active;
    if (criteria.routeOfAdministration) params['routeOfAdministration.equals'] = criteria.routeOfAdministration;
    if (criteria.prescriptionStatus) params['prescriptionStatus.equals'] = criteria.prescriptionStatus;
    if (criteria.riskLevel) params['riskLevel.equals'] = criteria.riskLevel;

    return params;
  }

  protected updateUrlParams(params: DialogParams): void {
    this.router.navigate([], {
      queryParams: params,
      queryParamsHandling: 'merge',
    });
  }

  protected clearDialogParams(): void {
    this.updateUrlParams({
      dialog: undefined,
      'selected-medoc': undefined,
      'is-new': undefined,
      tab: undefined,
    });
  }
}
