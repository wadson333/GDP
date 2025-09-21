import { Component, EventEmitter, OnInit, Output, Input, inject, signal } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { LANGUAGES } from 'app/config/language.constants';
import { IUser, User } from '../user-management.model';
import { UserManagementService } from '../service/user-management.service';

import { InputTextModule } from 'primeng/inputtext';
import { CheckboxModule } from 'primeng/checkbox';
import { DropdownModule } from 'primeng/dropdown';
import { MultiSelectModule } from 'primeng/multiselect';
import { ButtonModule } from 'primeng/button';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { TranslateService } from '@ngx-translate/core';

const userTemplate = {} as IUser;

const newUser: IUser = {
  langKey: 'fr',
  activated: true,
} as IUser;

@Component({
  standalone: true,
  selector: 'jhi-user-mgmt-update',
  templateUrl: './user-management-update.component.html',
  imports: [
    SharedModule,
    FormsModule,
    ReactiveFormsModule,
    InputTextModule,
    CheckboxModule,
    DropdownModule,
    MultiSelectModule,
    ButtonModule,
    ToastModule,
    ConfirmDialogModule,
  ],
  styleUrls: ['./user-management-update.component.scss'],
  providers: [MessageService, ConfirmationService],
})
export default class UserManagementUpdateComponent implements OnInit {
  @Input() user: User | null = null;
  @Output() closeEvent = new EventEmitter<boolean>();

  languages = LANGUAGES;
  authorities = signal<string[]>([]);
  isSaving = signal(false);

  editForm = new FormGroup({
    id: new FormControl(userTemplate.id),
    login: new FormControl(userTemplate.login, {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(50),
        Validators.pattern('^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$'),
      ],
    }),
    firstName: new FormControl(userTemplate.firstName, { validators: [Validators.maxLength(50)] }),
    lastName: new FormControl(userTemplate.lastName, { validators: [Validators.maxLength(50)] }),
    email: new FormControl(userTemplate.email, {
      nonNullable: true,
      validators: [Validators.minLength(5), Validators.maxLength(254), Validators.email],
    }),
    activated: new FormControl(userTemplate.activated, { nonNullable: true }),
    langKey: new FormControl(userTemplate.langKey, { nonNullable: true }),
    authorities: new FormControl(userTemplate.authorities, { nonNullable: true }),
  });

  private userService = inject(UserManagementService);
  private route = inject(ActivatedRoute);
  private confirmationService = inject(ConfirmationService);
  private messageService = inject(MessageService);
  private translateService = inject(TranslateService);

  ngOnInit(): void {
    //  this.route.data.subscribe(({ user }) => {
    if (this.user) {
      this.editForm.reset(this.user);
    } else {
      this.editForm.reset(newUser);
    }
    // });
    this.userService.authorities().subscribe(authorities => this.authorities.set(authorities));
  }

  previousState(): void {
    window.history.back();
  }

  // save(): void {
  //   this.isSaving.set(true);
  //   const user = this.editForm.getRawValue();
  //   if (user.id !== null) {
  //     this.userService.update(user).subscribe({
  //       next: () => this.onSaveSuccess(),
  //       error: () => this.onSaveError(),
  //     });
  //   } else {
  //     this.userService.create(user).subscribe({
  //       next: () => this.onSaveSuccess(),
  //       error: () => this.onSaveError(),
  //     });
  //   }
  // }
  save(): void {
    this.confirmationService.confirm({
      message: this.translateService.instant('userManagement.messages.confirm.save'),
      header: this.translateService.instant('userManagement.messages.confirm.title'),
      icon: 'pi pi-exclamation-triangle',
      acceptIcon: 'none',
      rejectIcon: 'none',
      rejectButtonStyleClass: 'p-button-text',
      rejectLabel: this.translateService.instant('userManagement.messages.confirm.cancel'),
      acceptLabel: this.translateService.instant('userManagement.messages.confirm.confirm'),
      accept: () => {
        this.isSaving.set(true);
        const user = this.editForm.getRawValue();
        if (user.id !== null) {
          this.userService.update(user).subscribe({
            next: () => this.onSaveSuccess(),
            error: () => this.onSaveError(),
          });
        } else {
          this.userService.create(user).subscribe({
            next: () => this.onSaveSuccess(),
            error: () => this.onSaveError(),
          });
        }
      },
    });
  }
  cancel(): void {
    this.closeEvent.emit(false);
  }

  private onSaveSuccess(): void {
    this.isSaving.set(false);
    this.messageService.add({
      severity: 'success',
      summary: this.translateService.instant('global.messages.success'),
      detail: this.translateService.instant('userManagement.messages.success.saved'),
    });
    this.closeEvent.emit(true);
  }

  private onSaveError(): void {
    this.isSaving.set(false);
    this.messageService.add({
      severity: 'error',
      summary: this.translateService.instant('global.messages.fail'),
      detail: this.translateService.instant('userManagement.messages.error.default'),
    });
  }
}
