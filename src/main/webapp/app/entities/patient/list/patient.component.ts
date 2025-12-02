// import { Component, NgZone, OnInit, inject } from '@angular/core';
// import { HttpHeaders } from '@angular/common/http';
// import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
// import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
// import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

// import SharedModule from 'app/shared/shared.module';
// import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
// import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
// import { ItemCountComponent } from 'app/shared/pagination';
// import { FormsModule } from '@angular/forms';
// import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
// import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
// import { DataUtils } from 'app/core/util/data-util.service';
// import { IPatient } from '../patient.model';

// import { EntityArrayResponseType, PatientService } from '../service/patient.service';
// import { PatientDeleteDialogComponent } from '../delete/patient-delete-dialog.component';

// @Component({
//   standalone: true,
//   selector: 'jhi-patient',
//   templateUrl: './patient.component.html',
//   imports: [
//     RouterModule,
//     FormsModule,
//     SharedModule,
//     SortDirective,
//     SortByDirective,
//     DurationPipe,
//     FormatMediumDatetimePipe,
//     FormatMediumDatePipe,
//     ItemCountComponent,
//   ],
// })
// export class PatientComponent implements OnInit {
//   subscription: Subscription | null = null;
//   patients?: IPatient[];
//   isLoading = false;

//   sortState = sortStateSignal({});

//   itemsPerPage = ITEMS_PER_PAGE;
//   totalItems = 0;
//   page = 1;

//   public router = inject(Router);
//   protected patientService = inject(PatientService);
//   protected activatedRoute = inject(ActivatedRoute);
//   protected sortService = inject(SortService);
//   protected dataUtils = inject(DataUtils);
//   protected modalService = inject(NgbModal);
//   protected ngZone = inject(NgZone);

//   trackId = (item: IPatient): number => this.patientService.getPatientIdentifier(item);

//   ngOnInit(): void {
//     this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
//       .pipe(
//         tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
//         tap(() => this.load()),
//       )
//       .subscribe();
//   }

//   byteSize(base64String: string): string {
//     return this.dataUtils.byteSize(base64String);
//   }

//   openFile(base64String: string, contentType: string | null | undefined): void {
//     return this.dataUtils.openFile(base64String, contentType);
//   }

//   delete(patient: IPatient): void {
//     const modalRef = this.modalService.open(PatientDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
//     modalRef.componentInstance.patient = patient;
//     // unsubscribe not needed because closed completes on modal close
//     modalRef.closed
//       .pipe(
//         filter(reason => reason === ITEM_DELETED_EVENT),
//         tap(() => this.load()),
//       )
//       .subscribe();
//   }

//   load(): void {
//     this.queryBackend().subscribe({
//       next: (res: EntityArrayResponseType) => {
//         this.onResponseSuccess(res);
//       },
//     });
//   }

//   navigateToWithComponentValues(event: SortState): void {
//     this.handleNavigation(this.page, event);
//   }

//   navigateToPage(page: number): void {
//     this.handleNavigation(page, this.sortState());
//   }

//   protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
//     const page = params.get(PAGE_HEADER);
//     this.page = +(page ?? 1);
//     this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
//   }

//   protected onResponseSuccess(response: EntityArrayResponseType): void {
//     this.fillComponentAttributesFromResponseHeader(response.headers);
//     const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
//     this.patients = dataFromBody;
//   }

//   protected fillComponentAttributesFromResponseBody(data: IPatient[] | null): IPatient[] {
//     return data ?? [];
//   }

//   protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
//     this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
//   }

//   protected queryBackend(): Observable<EntityArrayResponseType> {
//     const { page } = this;

//     this.isLoading = true;
//     const pageToLoad: number = page;
//     const queryObject: any = {
//       page: pageToLoad - 1,
//       size: this.itemsPerPage,
//       sort: this.sortService.buildSortParam(this.sortState()),
//     };
//     return this.patientService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
//   }

//   protected handleNavigation(page: number, sortState: SortState): void {
//     const queryParamsObj = {
//       page,
//       size: this.itemsPerPage,
//       sort: this.sortService.buildSortParam(sortState),
//     };

//     this.ngZone.run(() => {
//       this.router.navigate(['./'], {
//         relativeTo: this.activatedRoute,
//         queryParams: queryParamsObj,
//       });
//     });
//   }
// }
import { Component, NgZone, OnInit, inject } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule } from '@angular/forms';
import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { DataUtils } from 'app/core/util/data-util.service';
import { IPatient } from '../patient.model';

import { EntityArrayResponseType, PatientService } from '../service/patient.service';
import { PatientDeleteDialogComponent } from '../delete/patient-delete-dialog.component';
import { PatientDetailComponent } from '../detail/patient-detail.component';
import { PatientUpdateComponent } from '../update/patient-update.component';

// PrimeNG Imports
import { ButtonModule } from 'primeng/button';
import { AvatarModule } from 'primeng/avatar';
import { ChipModule } from 'primeng/chip';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { PaginatorModule } from 'primeng/paginator';
import { TooltipModule } from 'primeng/tooltip';
import { CardModule } from 'primeng/card';
import { TagModule } from 'primeng/tag';
import { CalendarModule } from 'primeng/calendar';
import { CommonModule } from '@angular/common';
import { SkeletonModule } from 'primeng/skeleton';
import { MenuModule } from 'primeng/menu';
import { MenuItem, MessageService, ConfirmationService } from 'primeng/api';
import { ToolbarModule } from 'primeng/toolbar';
import { DividerModule } from 'primeng/divider';
import { ToastModule } from 'primeng/toast';
import { TabViewModule } from 'primeng/tabview';
import { DialogModule } from 'primeng/dialog';
import { ConfirmDialogModule } from 'primeng/confirmdialog';

interface FilterCriterion {
  field: string;
  label: string;
  type: 'text' | 'enum' | 'date' | 'number';
  enumValues?: { label: string; value: string }[];
  operator?: 'contains' | 'equals' | 'greaterThan' | 'lessThan';
}

interface ActiveFilter {
  field: string;
  label: string;
  value: any;
  displayValue: string;
  operator: string;
}

@Component({
  standalone: true,
  selector: 'jhi-patient',
  templateUrl: './patient.component.html',
  styleUrls: ['./patient.component.scss'],
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    SharedModule,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    ItemCountComponent,
    PatientDetailComponent,
    ButtonModule,
    AvatarModule,
    ChipModule,
    DropdownModule,
    InputTextModule,
    PaginatorModule,
    TooltipModule,
    CardModule,
    TagModule,
    CalendarModule,
    SkeletonModule,
    MenuModule,
    ToolbarModule,
    DividerModule,
    ToastModule,
    TabViewModule,
    DialogModule,
    PatientUpdateComponent,
    ConfirmDialogModule,
  ],
  providers: [MessageService, ConfirmationService],
})
export class PatientComponent implements OnInit {
  subscription: Subscription | null = null;
  patients?: IPatient[];
  isLoading = false;
  showDialog = false;
  dialogMode: 'CREATE' | 'EDIT' = 'CREATE';
  selectedPatient: IPatient | null = null;

  sortState = sortStateSignal({});

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  // Filtres
  activeFilters: ActiveFilter[] = [];
  selectedCriterion: FilterCriterion | null = null;
  filterValue: any = null;

  searchType!: 'simple' | 'advanced';

  // Recherche globale
  globalSearchTerm = '';

  menuItems: MenuItem[] = [
    {
      label: 'Export',
      items: [
        { label: 'CSV', icon: 'pi pi-file' },
        { label: 'Excel', icon: 'pi pi-file-excel' },
        { label: 'PDF', icon: 'pi pi-file-pdf' },
      ],
    },
  ];

  public router = inject(Router);
  protected patientService = inject(PatientService);
  protected activatedRoute = inject(ActivatedRoute);
  protected sortService = inject(SortService);
  protected dataUtils = inject(DataUtils);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);
  protected messageService = inject(MessageService);
  protected translateService = inject(TranslateService);

  trackId = (item: IPatient): number => this.patientService.getPatientIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();
  }

  get searchTypeOptions(): { label: string; value: string }[] {
    return [
      {
        label: this.translateService.instant('gdpApp.patient.search.searchType.simple'),
        value: 'simple',
      },
      {
        label: this.translateService.instant('gdpApp.patient.search.searchType.advanced'),
        value: 'advanced',
      },
    ];
  }

  get availableCriteria(): FilterCriterion[] {
    return [
      { field: 'firstName', label: this.translateService.instant('gdpApp.patient.firstName'), type: 'text', operator: 'contains' },
      { field: 'lastName', label: this.translateService.instant('gdpApp.patient.lastName'), type: 'text', operator: 'contains' },
      { field: 'nif', label: this.translateService.instant('gdpApp.patient.nif'), type: 'text', operator: 'contains' },
      {
        field: 'medicalRecordNumber',
        label: this.translateService.instant('gdpApp.patient.medicalRecordNumber'),
        type: 'text',
        operator: 'contains',
      },
      { field: 'phone1', label: this.translateService.instant('gdpApp.patient.phone1'), type: 'text', operator: 'contains' },
      {
        field: 'status',
        label: this.translateService.instant('gdpApp.patient.enumValues.status.title'),
        type: 'enum',
        operator: 'equals',
        enumValues: [
          { label: this.translateService.instant('gdpApp.patient.enumValues.status.actif'), value: 'ACTIVE' },
          { label: this.translateService.instant('gdpApp.patient.enumValues.status.inactif'), value: 'INACTIVE' },
          { label: this.translateService.instant('gdpApp.patient.enumValues.status.deceased'), value: 'DECEASED' },
          { label: this.translateService.instant('gdpApp.patient.enumValues.status.archive'), value: 'ARCHIVED' },
        ],
      },
      {
        field: 'gender',
        label: this.translateService.instant('gdpApp.patient.enumValues.gender.title'),
        type: 'enum',
        operator: 'equals',
        enumValues: [
          { label: this.translateService.instant('gdpApp.patient.enumValues.gender.male'), value: 'MALE' },
          { label: this.translateService.instant('gdpApp.patient.enumValues.gender.female'), value: 'FEMALE' },
          { label: this.translateService.instant('gdpApp.patient.enumValues.gender.other'), value: 'OTHER' },
        ],
      },
      {
        field: 'bloodType',
        label: 'Groupe Sanguin',
        type: 'enum',
        operator: 'equals',
        enumValues: [
          { label: this.translateService.instant('gdpApp.patient.enumValues.bloodType.aPositive'), value: 'A_POS' },
          { label: this.translateService.instant('gdpApp.patient.enumValues.bloodType.aNegative'), value: 'A_NEG' },
          { label: this.translateService.instant('gdpApp.patient.enumValues.bloodType.bPositive'), value: 'B_POS' },
          { label: this.translateService.instant('gdpApp.patient.enumValues.bloodType.bNegative'), value: 'B_NEG' },
          { label: this.translateService.instant('gdpApp.patient.enumValues.bloodType.abPositive'), value: 'AB_POS' },
          { label: this.translateService.instant('gdpApp.patient.enumValues.bloodType.abNegative'), value: 'AB_NEG' },
          { label: this.translateService.instant('gdpApp.patient.enumValues.bloodType.oPositive'), value: 'O_POS' },
          { label: this.translateService.instant('gdpApp.patient.enumValues.bloodType.oNegative'), value: 'O_NEG' },
          { label: this.translateService.instant('gdpApp.patient.enumValues.bloodType.unknown'), value: 'UNKNOWN' },
        ],
      },
      { field: 'birthDate', label: this.translateService.instant('gdpApp.patient.birthDate'), type: 'date', operator: 'equals' },
    ];
  }
  onSelect(patient: IPatient): void {
    this.selectedPatient = patient;
    // Optionnel : Mettre à jour l'URL sans recharger
    // this.router.navigate([], {
    //   // relativeTo: this.activatedRoute,
    //   queryParams: { patient: patient.uid },
    //   queryParamsHandling: 'merge',
    // });
  }

  closeDetail(): void {
    this.selectedPatient = null;
    this.clearSelectionFromUrl();
  }

  clearSelectionFromUrl(): void {
    // this.ngZone.run(() => {
    // this.router.navigate([], {
    //   // relativeTo: this.activatedRoute,
    //   queryParams: { patient: null },
    //   queryParamsHandling: 'merge',
    // });
    // });
  }

  addFilter(): void {
    if (!this.selectedCriterion || !this.filterValue) {
      this.messageService.add({
        severity: 'error',
        summary: this.translateService.instant('gdpApp.patient.search.selectFilterCriteriaErrorTitle'),
        detail: this.translateService.instant('gdpApp.patient.search.selectFilterCriteriaErrorMsg'),
      });
      return;
    }

    const displayValue = this.getDisplayValue(this.selectedCriterion, this.filterValue);

    const filter2: ActiveFilter = {
      field: this.selectedCriterion.field,
      label: this.selectedCriterion.label,
      value: this.filterValue,
      displayValue,
      operator: this.selectedCriterion.operator ?? 'contains',
    };

    this.activeFilters.push(filter2);
    this.selectedCriterion = null;
    this.filterValue = null;
    this.page = 1; // Réinitialiser à la première page
    this.load();
  }

  removeFilter(filter2: ActiveFilter): void {
    this.activeFilters = this.activeFilters.filter(f => f !== filter2);
    this.page = 1;
    this.load();
  }

  clearAllFilters(): void {
    this.activeFilters = [];
    this.globalSearchTerm = '';
    this.page = 1;
    this.load();
  }

  applyGlobalSearch(): void {
    this.page = 1;
    this.load();
  }

  getDisplayValue(criterion: FilterCriterion, value: any): string {
    if (criterion.type === 'enum' && criterion.enumValues) {
      const enumValue = criterion.enumValues.find(ev => ev.value === value);
      // eslint-disable-next-line @typescript-eslint/no-unsafe-return
      return enumValue ? enumValue.label : value;
    }
    if (criterion.type === 'date' && value) {
      return new Date(value).toLocaleDateString('fr-FR');
    }
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return value?.toString() || '';
  }

  buildQueryParams(): any {
    const queryObject: any = {
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(this.sortState()),
    };

    // Appliquer les filtres actifs
    this.activeFilters.forEach(filter2 => {
      const paramKey = `${filter2.field}.${filter2.operator}`;
      queryObject[paramKey] = filter2.value;
    });

    // Appliquer la recherche globale (full-text)
    if (this.globalSearchTerm.trim()) {
      queryObject['fullTextSearch.contains'] = this.globalSearchTerm.trim();
    }

    return queryObject;
  }

  getInitials(patient: IPatient): string {
    const first = patient.firstName?.charAt(0) ?? '';
    const last = patient.lastName?.charAt(0) ?? '';
    return (first + last).toUpperCase();
  }

  getStatusSeverity(status: string | null | undefined): 'success' | 'secondary' | 'info' | 'warning' | 'danger' {
    switch (status) {
      case 'ACTIVE':
        return 'success';
      case 'INACTIVE':
        return 'warning';
      case 'DECEASED':
        return 'danger';
      case 'ARCHIVED':
        return 'secondary';
      default:
        return 'info';
    }
  }

  getStatusLabel(status: string | null | undefined): string {
    const statusMap: Record<string, string> = {
      ACTIVE: 'Actif',
      INACTIVE: 'Inactif',
      DECEASED: 'Décédé',
      ARCHIVED: 'Archivé',
    };
    return statusMap[status ?? ''] || 'N/A';
  }

  getGenderLabel(gender: string | null | undefined): string {
    const genderMap: Record<string, string> = {
      MALE: 'Homme',
      FEMALE: 'Femme',
      OTHER: 'Autre',
    };
    return genderMap[gender ?? ''] || 'N/A';
  }

  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });

    console.error('Load method called');
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(this.page, event);
  }

  navigateToPage(page: number): void {
    this.handleNavigation(page, this.sortState());
  }

  onPageChange(event: any): void {
    // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
    this.page = event.page + 1; // PrimeNG utilise un index 0-based
    this.load();
  }

  // Dialog Methods
  openCreateDialog(): void {
    this.dialogMode = 'CREATE';
    // this.selectedPatient = null;
    this.showDialog = true;
  }

  openEditDialog(): void {
    this.dialogMode = 'EDIT';
    // this.selectedPatient = patient;
    this.showDialog = true;
  }

  closeDialog(): void {
    this.showDialog = false;
    // this.selectedPatient = null;
  }

  onPatientSaved(patient: IPatient): void {
    this.showDialog = false;
    this.selectedPatient = patient;
    this.load(); // Reload the list
  }

  onDialogCancelled(): void {
    this.showDialog = false;
    this.selectedPatient = null;
  }

  // Gender Icon
  getGenderIcon(gender: string | undefined): string {
    switch (gender?.toLowerCase()) {
      case 'male':
        return 'pi pi-mars';
      case 'female':
        return 'pi pi-venus';
      default:
        return 'pi pi-question';
    }
  }

  // Age Calculation
  calculateAge(birthDate: any): number | null {
    if (!birthDate) return null;
    const today = new Date();
    const birth = new Date(birthDate);
    let age = today.getFullYear() - birth.getFullYear();
    const monthDiff = today.getMonth() - birth.getMonth();
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
      age--;
    }
    return age;
  }

  // Export functionality
  exportPatients(): void {
    this.messageService.add({
      severity: 'info',
      summary: 'Export',
      detail: 'Export functionality to be implemented',
      life: 3000,
    });
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.patients = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IPatient[] | null): IPatient[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const queryObject = this.buildQueryParams();
    return this.patientService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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
}
