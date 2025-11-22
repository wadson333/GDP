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
    SortDirective,
    SortByDirective,
    DurationPipe,
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
  ],
})
export class PatientComponent implements OnInit {
  subscription: Subscription | null = null;
  patients?: IPatient[];
  isLoading = false;
  selectedPatient: IPatient | null = null;

  sortState = sortStateSignal({});

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  // Filtres
  activeFilters: ActiveFilter[] = [];
  selectedCriterion: FilterCriterion | null = null;
  filterValue: any = null;

  availableCriteria: FilterCriterion[] = [
    { field: 'firstName', label: 'Prénom', type: 'text', operator: 'contains' },
    { field: 'lastName', label: 'Nom', type: 'text', operator: 'contains' },
    { field: 'nif', label: 'NIF', type: 'text', operator: 'contains' },
    { field: 'medicalRecordNumber', label: 'N° Dossier Médical', type: 'text', operator: 'contains' },
    { field: 'phone1', label: 'Téléphone', type: 'text', operator: 'contains' },
    {
      field: 'status',
      label: 'Statut',
      type: 'enum',
      operator: 'equals',
      enumValues: [
        { label: 'Actif', value: 'ACTIVE' },
        { label: 'Inactif', value: 'INACTIVE' },
        { label: 'Décédé', value: 'DECEASED' },
        { label: 'Archivé', value: 'ARCHIVED' },
      ],
    },
    {
      field: 'gender',
      label: 'Sexe',
      type: 'enum',
      operator: 'equals',
      enumValues: [
        { label: 'Homme', value: 'MALE' },
        { label: 'Femme', value: 'FEMALE' },
        { label: 'Autre', value: 'OTHER' },
      ],
    },
    {
      field: 'bloodType',
      label: 'Groupe Sanguin',
      type: 'enum',
      operator: 'equals',
      enumValues: [
        { label: 'A+', value: 'A_POS' },
        { label: 'A-', value: 'A_NEG' },
        { label: 'B+', value: 'B_POS' },
        { label: 'B-', value: 'B_NEG' },
        { label: 'AB+', value: 'AB_POS' },
        { label: 'AB-', value: 'AB_NEG' },
        { label: 'O+', value: 'O_POS' },
        { label: 'O-', value: 'O_NEG' },
        { label: 'Inconnu', value: 'UNKNOWN' },
      ],
    },
    { field: 'birthDate', label: 'Date de naissance', type: 'date', operator: 'equals' },
  ];

  // Recherche globale
  globalSearchTerm = '';

  public router = inject(Router);
  protected patientService = inject(PatientService);
  protected activatedRoute = inject(ActivatedRoute);
  protected sortService = inject(SortService);
  protected dataUtils = inject(DataUtils);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);

  trackId = (item: IPatient): number => this.patientService.getPatientIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();
  }

  onSelect(patient: IPatient): void {
    this.selectedPatient = patient;
    // Optionnel : Mettre à jour l'URL sans recharger
    this.router.navigate([], {
      relativeTo: this.activatedRoute,
      queryParams: { patientId: patient.id },
      queryParamsHandling: 'merge',
    });
  }

  addFilter(): void {
    if (!this.selectedCriterion || !this.filterValue) {
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

  delete(patient: IPatient): void {
    const modalRef = this.modalService.open(PatientDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.patient = patient;
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => {
          this.selectedPatient = null;
          this.load();
        }),
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
