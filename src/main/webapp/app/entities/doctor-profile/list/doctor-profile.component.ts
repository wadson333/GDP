/* eslint-disable @typescript-eslint/no-unsafe-return */
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
// import { FilterComponent, FilterOptions, IFilterOption, IFilterOptions } from 'app/shared/filter';
// import { DoctorProfileService, EntityArrayResponseType } from '../service/doctor-profile.service';
// import { DoctorProfileDeleteDialogComponent } from '../delete/doctor-profile-delete-dialog.component';
// import { IDoctorProfile } from '../doctor-profile.model';

// @Component({
//   standalone: true,
//   selector: 'jhi-doctor-profile',
//   templateUrl: './doctor-profile.component.html',
//   imports: [
//     RouterModule,
//     FormsModule,
//     SharedModule,
//     SortDirective,
//     SortByDirective,
//     DurationPipe,
//     FormatMediumDatetimePipe,
//     FormatMediumDatePipe,
//     FilterComponent,
//     ItemCountComponent,
//   ],
// })
// export class DoctorProfileComponent implements OnInit {
//   subscription: Subscription | null = null;
//   doctorProfiles?: IDoctorProfile[];
//   isLoading = false;

//   sortState = sortStateSignal({});
//   filters: IFilterOptions = new FilterOptions();

//   itemsPerPage = ITEMS_PER_PAGE;
//   totalItems = 0;
//   page = 1;

//   public router = inject(Router);
//   protected doctorProfileService = inject(DoctorProfileService);
//   protected activatedRoute = inject(ActivatedRoute);
//   protected sortService = inject(SortService);
//   protected dataUtils = inject(DataUtils);
//   protected modalService = inject(NgbModal);
//   protected ngZone = inject(NgZone);

//   trackId = (item: IDoctorProfile): number => this.doctorProfileService.getDoctorProfileIdentifier(item);

//   ngOnInit(): void {
//     this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
//       .pipe(
//         tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
//         tap(() => this.load()),
//       )
//       .subscribe();

//     this.filters.filterChanges.subscribe(filterOptions => this.handleNavigation(1, this.sortState(), filterOptions));
//   }

//   byteSize(base64String: string): string {
//     return this.dataUtils.byteSize(base64String);
//   }

//   openFile(base64String: string, contentType: string | null | undefined): void {
//     return this.dataUtils.openFile(base64String, contentType);
//   }

//   delete(doctorProfile: IDoctorProfile): void {
//     const modalRef = this.modalService.open(DoctorProfileDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
//     modalRef.componentInstance.doctorProfile = doctorProfile;
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
//     this.handleNavigation(this.page, event, this.filters.filterOptions);
//   }

//   navigateToPage(page: number): void {
//     this.handleNavigation(page, this.sortState(), this.filters.filterOptions);
//   }

//   protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
//     const page = params.get(PAGE_HEADER);
//     this.page = +(page ?? 1);
//     this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
//     this.filters.initializeFromParams(params);
//   }

//   protected onResponseSuccess(response: EntityArrayResponseType): void {
//     this.fillComponentAttributesFromResponseHeader(response.headers);
//     const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
//     this.doctorProfiles = dataFromBody;
//   }

//   protected fillComponentAttributesFromResponseBody(data: IDoctorProfile[] | null): IDoctorProfile[] {
//     return data ?? [];
//   }

//   protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
//     this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
//   }

//   protected queryBackend(): Observable<EntityArrayResponseType> {
//     const { page, filters } = this;

//     this.isLoading = true;
//     const pageToLoad: number = page;
//     const queryObject: any = {
//       page: pageToLoad - 1,
//       size: this.itemsPerPage,
//       eagerload: true,
//       sort: this.sortService.buildSortParam(this.sortState()),
//     };
//     filters.filterOptions.forEach(filterOption => {
//       queryObject[filterOption.name] = filterOption.values;
//     });
//     return this.doctorProfileService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
//   }

//   protected handleNavigation(page: number, sortState: SortState, filterOptions?: IFilterOption[]): void {
//     const queryParamsObj: any = {
//       page,
//       size: this.itemsPerPage,
//       sort: this.sortService.buildSortParam(sortState),
//     };

//     filterOptions?.forEach(filterOption => {
//       queryParamsObj[filterOption.nameAsQueryParam()] = filterOption.values;
//     });

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
import { FilterComponent, FilterOptions, IFilterOption, IFilterOptions } from 'app/shared/filter';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { DoctorProfileService, EntityArrayResponseType } from '../service/doctor-profile.service';
import { DoctorProfileDeleteDialogComponent } from '../delete/doctor-profile-delete-dialog.component';
import { IDoctorProfile } from '../doctor-profile.model';
import { DoctorStatus } from 'app/entities/enumerations/doctor-status.model';
import { MedicalSpecialty } from 'app/entities/enumerations/medical-specialty.model';

import { TableModule, TableLazyLoadEvent } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { TagModule } from 'primeng/tag';
import { AvatarModule } from 'primeng/avatar';
import { TooltipModule } from 'primeng/tooltip';
import { SkeletonModule } from 'primeng/skeleton';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DialogModule } from 'primeng/dialog';
import { ConfirmationService, MenuItem } from 'primeng/api';
import { TranslateService } from '@ngx-translate/core';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { DoctorProfileUpdateComponent } from '../update/doctor-profile-update.component';
import { ToolbarModule } from 'primeng/toolbar';
import { MenuModule } from 'primeng/menu';
import { DividerModule } from 'primeng/divider';
import { DoctorProfileDetailComponent } from '../detail/doctor-profile-detail.component';
interface DropdownOption {
  label: string;
  value: string;
}

@Component({
  standalone: true,
  selector: 'jhi-doctor-profile',
  templateUrl: './doctor-profile.component.html',
  styleUrls: ['./doctor-profile.component.scss'],
  imports: [
    RouterModule,
    FormsModule,
    SharedModule,
    SortDirective,
    SortByDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    FilterComponent,
    ItemCountComponent,
    TableModule,
    ButtonModule,
    InputTextModule,
    DropdownModule,
    TagModule,
    AvatarModule,
    TooltipModule,
    SkeletonModule,
    ConfirmDialogModule,
    DialogModule,
    InputGroupModule,
    InputGroupAddonModule,
    DoctorProfileUpdateComponent,
    ToolbarModule,
    MenuModule,
    DividerModule,
    DoctorProfileDetailComponent,
  ],
  providers: [ConfirmationService],
})
export class DoctorProfileComponent implements OnInit {
  subscription: Subscription | null = null;
  doctorProfiles?: IDoctorProfile[];
  isLoading = false;
  loading = false;

  sortState = sortStateSignal({});
  filters: IFilterOptions = new FilterOptions();

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  // PrimeNG specific filters
  selectedStatus?: string;
  selectedSpecialty?: string;
  globalSearchTerm = '';

  // Options for dropdowns
  statusOptions: DropdownOption[] = [];
  specialtyOptions: DropdownOption[] = [];

  // Dialog
  displayDialog = false;
  dialogTitle = '';
  selectedDoctor?: IDoctorProfile;
  displayDetailDialog = false;

  // Current user
  currentAccount: Account | null = null;

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

  mode: 'CREATE' | 'EDIT' = 'CREATE';

  public router = inject(Router);
  protected doctorProfileService = inject(DoctorProfileService);
  protected activatedRoute = inject(ActivatedRoute);
  protected sortService = inject(SortService);
  protected dataUtils = inject(DataUtils);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);
  protected accountService = inject(AccountService);
  protected confirmationService = inject(ConfirmationService);
  protected translateService = inject(TranslateService);

  trackId = (item: IDoctorProfile): number => this.doctorProfileService.getDoctorProfileIdentifier(item);

  ngOnInit(): void {
    this.loadCurrentAccount();
    this.initializeDropdownOptions();

    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();

    this.filters.filterChanges.subscribe(filterOptions => this.handleNavigation(1, this.sortState(), filterOptions));
  }

  loadCurrentAccount(): void {
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
  }

  initializeDropdownOptions(): void {
    // Status options
    this.statusOptions = [
      {
        label: this.translateService.instant('gdpApp.doctorProfile.filter.allStatuses'),
        value: '',
      },
      ...Object.keys(DoctorStatus).map(key => ({
        label: this.translateService.instant(`gdpApp.DoctorStatus.${key}`),
        value: key,
      })),
    ];

    // Specialty options
    this.specialtyOptions = [
      {
        label: this.translateService.instant('gdpApp.doctorProfile.filter.allSpecialties'),
        value: '',
      },
      ...Object.keys(MedicalSpecialty).map(key => ({
        label: this.translateService.instant(`gdpApp.MedicalSpecialty.${key}`),
        value: key,
      })),
    ];
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  // delete(doctorProfile: IDoctorProfile): void {
  //   const modalRef = this.modalService.open(DoctorProfileDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
  //   modalRef.componentInstance.doctorProfile = doctorProfile;
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

  loadDoctors(): void {
    this.load();
  }

  clearSearch(): void {
    this.selectedStatus = undefined;
    this.selectedSpecialty = undefined;
    this.globalSearchTerm = '';
    this.loadDoctors();
  }

  hasActiveFilters(): boolean {
    return !!(this.selectedStatus ?? this.selectedSpecialty ?? this.globalSearchTerm.trim());
  }

  /**
   * PrimeNG lazy load handler
   */
  loadPage(event: TableLazyLoadEvent): void {
    const pageNumber = event.first! / event.rows! + 1;

    // Update sort state if sorting is applied
    if (event.sortField) {
      const sortField = event.sortField as string;
      const sortOrder = event.sortOrder === 1 ? 'asc' : 'desc';

      // Build new sort state using object spread
      const newSortState: SortState = {
        [sortField]: sortOrder,
      } as SortState;

      this.sortState.set(newSortState);
      this.handleNavigation(pageNumber, newSortState, this.filters.filterOptions);
    } else {
      // No sorting, use current sort state
      this.handleNavigation(pageNumber, this.sortState(), this.filters.filterOptions);
    }
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(this.page, event, this.filters.filterOptions);
  }

  navigateToPage(page: number): void {
    this.handleNavigation(page, this.sortState(), this.filters.filterOptions);
  }

  onFilterChange(): void {
    // Reset to first page when filters change
    this.handleNavigation(1, this.sortState(), this.filters.filterOptions);
  }

  onSearch(): void {
    // Reset to first page when searching
    this.handleNavigation(1, this.sortState(), this.filters.filterOptions);
  }

  canVerify(doctorProfile: IDoctorProfile): boolean {
    if (doctorProfile.status !== 'PENDING_APPROVAL') {
      return false;
    }

    return (
      this.currentAccount?.authorities.includes('ROLE_ADMIN') ??
      this.currentAccount?.authorities.includes('ROLE_RH') ??
      this.currentAccount?.authorities.includes('ROLE_DIRECTOR') ??
      false
    );
  }

  openVerifyDialog(doctorProfile: IDoctorProfile): void {
    this.confirmationService.confirm({
      message: this.translateService.instant('gdpApp.doctorProfile.verify.question', {
        name: `${doctorProfile.firstName} ${doctorProfile.lastName}`,
      }),
      header: this.translateService.instant('gdpApp.doctorProfile.verify.title'),
      icon: 'pi pi-exclamation-triangle',
      acceptIcon: 'pi pi-check',
      rejectIcon: 'pi pi-times',
      acceptLabel: this.translateService.instant('gdpApp.doctorProfile.verify.approve'),
      rejectLabel: this.translateService.instant('gdpApp.doctorProfile.verify.reject'),
      accept: () => {
        this.verifyDoctor(doctorProfile, true);
      },
      reject: () => {
        this.promptRejectionReason(doctorProfile);
      },
    });
  }

  promptRejectionReason(doctorProfile: IDoctorProfile): void {
    this.confirmationService.confirm({
      message: this.translateService.instant('gdpApp.doctorProfile.verify.rejectConfirm'),
      header: this.translateService.instant('gdpApp.doctorProfile.verify.rejectTitle'),
      icon: 'pi pi-times-circle',
      acceptIcon: 'pi pi-check',
      rejectIcon: 'pi pi-times',
      acceptLabel: this.translateService.instant('entity.action.confirm'),
      rejectLabel: this.translateService.instant('entity.action.cancel'),
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => {
        this.verifyDoctor(doctorProfile, false, 'Rejected by administrator');
      },
    });
  }

  verifyDoctor(doctorProfile: IDoctorProfile, approved: boolean, comment?: string): void {
    if (!doctorProfile.uid) {
      return;
    }

    this.doctorProfileService.verify(doctorProfile.uid, approved, comment).subscribe({
      next: () => {
        this.load();
      },
      error() {
        // Error handling is done by the alert service
      },
    });
  }

  openCreateDialog(): void {
    this.selectedDoctor = undefined;
    this.dialogTitle = this.translateService.instant('gdpApp.doctorProfile.home.dialogCreateTitle');
    this.displayDialog = true;
    this.mode = 'CREATE';
  }

  openEditDialog(doctorProfile: IDoctorProfile): void {
    this.selectedDoctor = doctorProfile;
    this.dialogTitle = this.translateService.instant('gdpApp.doctorProfile.home.dialogEditTitle', { codeClinic: doctorProfile.codeClinic });
    this.displayDialog = true;
    this.mode = 'EDIT';
  }

  closeDialog(): void {
    this.displayDialog = false;
    this.selectedDoctor = undefined;
    this.mode = 'CREATE';
  }

  openDetailDialog(doctorProfile: IDoctorProfile): void {
    this.selectedDoctor = doctorProfile;
    this.displayDetailDialog = true;
  }

  closeDetailDialog(): void {
    this.displayDetailDialog = false;
    this.selectedDoctor = undefined;
  }

  exportToExcel(): void {
    // TODO: Implement export functionality
    console.error('Export to Excel');
  }

  getStatusLabel(status: string | null | undefined): string {
    return status ? this.translateService.instant(`gdpApp.DoctorStatus.${status}`) : '';
  }

  getStatusSeverity(status: string | null | undefined): 'success' | 'warning' | 'danger' | 'info' {
    switch (status) {
      case 'ACTIVE':
        return 'success';
      case 'PENDING_APPROVAL':
        return 'warning';
      case 'REJECTED':
      case 'SUSPENDED':
        return 'danger';
      case 'ON_LEAVE':
        return 'info';
      default:
        return 'info';
    }
  }

  getSpecialtyLabel(specialty: string | null | undefined): string {
    return specialty ? this.translateService.instant(`gdpApp.MedicalSpecialty.${specialty}`) : '';
  }

  onDoctorSaved(): void {
    this.displayDialog = false;
    this.selectedDoctor = undefined;
    this.loadDoctors();
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
    this.filters.initializeFromParams(params);
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.doctorProfiles = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IDoctorProfile[] | null): IDoctorProfile[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    const { page, filters } = this;

    this.isLoading = true;
    this.loading = true;
    const pageToLoad: number = page;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      eagerload: true,
      sort: this.sortService.buildSortParam(this.sortState()),
    };

    // Add JHipster filters
    filters.filterOptions.forEach(filterOption => {
      queryObject[filterOption.name] = filterOption.values;
    });

    // Add PrimeNG custom filters
    if (this.selectedStatus) {
      queryObject['status.equals'] = this.selectedStatus;
    }

    if (this.selectedSpecialty) {
      queryObject['primarySpecialty.equals'] = this.selectedSpecialty;
    }

    if (this.globalSearchTerm.trim()) {
      const searchTerm = this.globalSearchTerm.trim();
      queryObject['globalFilter.contains'] = searchTerm;
      // queryObject['lastName.contains'] = searchTerm;
    }

    return this.doctorProfileService.query(queryObject).pipe(
      tap(() => {
        this.isLoading = false;
        this.loading = false;
      }),
    );
  }

  protected handleNavigation(page: number, sortState: SortState, filterOptions?: IFilterOption[]): void {
    const queryParamsObj: any = {
      page,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(sortState),
    };

    filterOptions?.forEach(filterOption => {
      queryParamsObj[filterOption.nameAsQueryParam()] = filterOption.values;
    });

    // Add custom filters to URL
    if (this.selectedStatus) {
      queryParamsObj['status.equals'] = this.selectedStatus;
    }

    if (this.selectedSpecialty) {
      queryParamsObj['primarySpecialty.equals'] = this.selectedSpecialty;
    }

    if (this.globalSearchTerm.trim()) {
      queryParamsObj['search'] = this.globalSearchTerm.trim();
    }

    this.ngZone.run(() => {
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    });
  }
}
