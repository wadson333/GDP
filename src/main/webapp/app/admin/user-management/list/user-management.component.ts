/* eslint-disable prettier/prettier */
import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { combineLatest, merge } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortService, SortState, sortStateSignal } from 'app/shared/sort';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { SORT } from 'app/config/navigation.constants';
import { AccountService } from 'app/core/auth/account.service';
import { UserManagementService } from '../service/user-management.service';
import { User } from '../user-management.model';
import UserManagementDeleteDialogComponent from '../delete/user-management-delete-dialog.component';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { MultiSelectModule } from 'primeng/multiselect';
import { FormsModule } from '@angular/forms';
import { InputSwitchModule } from 'primeng/inputswitch';
import { AvatarModule } from 'primeng/avatar';
import { DialogModule } from 'primeng/dialog';
import { TagModule } from 'primeng/tag';
import UserManagementDetailComponent from '../detail/user-management-detail.component';
import UserManagementUpadteComponent from '../update/user-management-update.component';

@Component({
  standalone: true,
  selector: 'jhi-user-mgmt',
  templateUrl: './user-management.component.html',
  imports: [
    RouterModule,
    SharedModule,
    TableModule,
    ButtonModule,
    DropdownModule,
    MultiSelectModule,
    FormsModule,
    InputSwitchModule,
    AvatarModule,
    DialogModule,
    TagModule,
    UserManagementDetailComponent,
    UserManagementUpadteComponent,
  ],
})
export default class UserManagementComponent implements OnInit {
  currentAccount = inject(AccountService).trackCurrentAccount();
  users = signal<User[] | null>(null);
  isLoading = signal(false);
  totalItems = signal(0);
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  sortState = sortStateSignal({});
  statusOptions = [
    { label: 'Active', value: true },
    { label: 'Inactive', value: false },
  ];

  displayUserDetailDialog = false;
  displayUserFormDialog = false;
  selectedUser: User | null = null;
  isNewUser = false;

  private userService = inject(UserManagementService);
  private activatedRoute = inject(ActivatedRoute);
  private router = inject(Router);
  private sortService = inject(SortService);
  private modalService = inject(NgbModal);

  constructor() {
    this.activatedRoute.queryParams.subscribe(params => {
      this.displayUserDetailDialog = params['dialog'] === 'user-details' && this.selectedUser !== null;
      this.displayUserFormDialog = params['dialog'] === 'add-user' || (params['dialog'] === 'edit-user' && this.selectedUser !== null);
      // this.displayUserDialog = params['dialog'] === 'add-user';
      if (!this.displayUserDetailDialog && !this.displayUserFormDialog) {
        this.mergeParameters();
      }
    });
  }

  ngOnInit(): void {
    this.handleNavigation();
  }

  setActive(user: User, isActivated: boolean): void {
    this.userService.update({ ...user, activated: isActivated }).subscribe(() => this.loadAll());
  }

  trackIdentity(item: User): number {
    return item.id!;
  }

  deleteUser(user: User): void {
    const modalRef = this.modalService.open(UserManagementDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.user = user;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  loadAll(): void {
    this.isLoading.set(true);
    this.userService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sortService.buildSortParam(this.sortState(), 'id'),
      })
      .subscribe({
        next: (res: HttpResponse<User[]>) => {
          this.isLoading.set(false);
          this.onSuccess(res.body, res.headers);
        },
        error: () => this.isLoading.set(false),
      });
  }

  transition(sortState?: SortState): void {
    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute.parent,
      queryParams: {
        page: this.page,
        sort: this.sortService.buildSortParam(sortState ?? this.sortState()),
      },
    });
  }

  showUserDetails(user: User): void {
    this.selectedUser = user;
    this.displayUserDetailDialog = true;
    this.router.navigate([], {
      queryParams: { dialog: 'user-details', 'selected-user': user.id },
      queryParamsHandling: 'merge',
    });
  }

  closeDetailDialog(): void {
    this.displayUserDetailDialog = false;
    this.selectedUser = null;
    // Optionally, you can also clear the query parameter if you used it to open the dialog
    this.mergeParameters();
  }

  showAddUserDialog(): void {
    this.selectedUser = null;
    this.displayUserFormDialog = true;
    this.isNewUser = true;
    this.router.navigate([], {
      queryParams: { dialog: 'add-user', 'selected-user': 'new', 'is-new': true },
      queryParamsHandling: 'merge',
    });
  }

  showEditUserDialog(user: User): void {
    this.selectedUser = structuredClone(user);
    this.isNewUser = false;
    this.displayUserFormDialog = true;
    this.router.navigate([], {
      queryParams: { dialog: 'edit-user', 'selected-user': user.id, 'is-new': false },
      queryParamsHandling: 'merge',
    });
  }

  closeFormDialog(success: boolean): void {
    this.displayUserFormDialog = false;
    this.selectedUser = null;
    this.isNewUser = false;
    // Optionally, you can also clear the query parameter if you used it to open the dialog
    this.mergeParameters();
    if (success) this.loadAll();
  }

  private mergeParameters(): void {
    this.router.navigate([], {
      queryParams: { dialog: null, 'selected-user': null, 'is-new': null },
      queryParamsHandling: 'merge',
    });
  }
  private handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      this.page = +(page ?? 1);
      this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data.defaultSort));
      this.loadAll();
    });
  }

  private onSuccess(users: User[] | null, headers: HttpHeaders): void {
    this.totalItems.set(Number(headers.get('X-Total-Count')));
    this.users.set(users);
  }
}
