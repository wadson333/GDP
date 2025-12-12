import { Component, Input, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { HttpResponse } from '@angular/common/http';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';

import { IDoctorProfile } from '../doctor-profile.model';
import { DoctorProfileService } from '../service/doctor-profile.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';

import { TabViewModule } from 'primeng/tabview';
import { ButtonModule } from 'primeng/button';
import { AvatarModule } from 'primeng/avatar';
import { TagModule } from 'primeng/tag';
import { ChipModule } from 'primeng/chip';
import { CardModule } from 'primeng/card';
import { DividerModule } from 'primeng/divider';
import { TooltipModule } from 'primeng/tooltip';
import { DialogModule } from 'primeng/dialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import { TranslateService } from '@ngx-translate/core';
import dayjs from 'dayjs/esm';

@Component({
  standalone: true,
  selector: 'jhi-doctor-profile-detail',
  templateUrl: './doctor-profile-detail.component.html',
  imports: [
    SharedModule,
    RouterModule,
    DurationPipe,
    FormatMediumDatePipe,
    FormatMediumDatetimePipe,
    TabViewModule,
    ButtonModule,
    AvatarModule,
    TagModule,
    ChipModule,
    CardModule,
    DividerModule,
    TooltipModule,
    DialogModule,
    ConfirmDialogModule,
    ToastModule,
  ],
  providers: [ConfirmationService, MessageService],
})
export class DoctorProfileDetailComponent implements OnInit {
  @Input() doctorProfile: IDoctorProfile | null = null;
  currentAccount: Account | null = null;
  displayEditDialog = false;
  loading = true;

  protected doctorProfileService = inject(DoctorProfileService);
  protected activatedRoute = inject(ActivatedRoute);
  protected router = inject(Router);
  protected accountService = inject(AccountService);
  protected confirmationService = inject(ConfirmationService);
  protected messageService = inject(MessageService);
  protected translateService = inject(TranslateService);

  ngOnInit(): void {
    this.loadCurrentAccount();
    this.loadDoctorProfile();
  }

  loadCurrentAccount(): void {
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
  }

  loadDoctorProfile(): void {
    if (this.doctorProfile) {
      this.loading = false;
      return;
    }
    // this.loading = true;
    // this.activatedRoute.data.subscribe(({ doctorProfile }) => {
    //   this.doctorProfile = doctorProfile;
    //   this.loading = false;
    // });
  }

  previousState(): void {
    window.history.back();
  }

  openEditDialog(): void {
    if (this.doctorProfile?.uid) {
      this.router.navigate(['/doctor-profile', this.doctorProfile.id, 'edit']);
    }
  }

  openVerifyDialog(): void {
    if (!this.doctorProfile) {
      return;
    }

    this.confirmationService.confirm({
      message: this.translateService.instant('gdpApp.doctorProfile.verify.question', {
        name: `${this.doctorProfile.firstName} ${this.doctorProfile.lastName}`,
      }),
      header: this.translateService.instant('gdpApp.doctorProfile.verify.title'),
      icon: 'pi pi-exclamation-triangle',
      acceptIcon: 'pi pi-check',
      rejectIcon: 'pi pi-times',
      acceptLabel: this.translateService.instant('gdpApp.doctorProfile.verify.approve'),
      rejectLabel: this.translateService.instant('gdpApp.doctorProfile.verify.reject'),
      accept: () => {
        this.verifyDoctor(true);
      },
      reject: () => {
        this.promptRejectionReason();
      },
    });
  }

  promptRejectionReason(): void {
    if (!this.doctorProfile) {
      return;
    }

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
        this.verifyDoctor(false, 'Rejected by administrator');
      },
    });
  }

  verifyDoctor(approved: boolean, comment?: string): void {
    if (!this.doctorProfile?.uid) {
      return;
    }

    this.doctorProfileService.verify(this.doctorProfile.uid, approved, comment).subscribe({
      next: (res: HttpResponse<IDoctorProfile>) => {
        this.doctorProfile = res.body;
        this.showSuccess(approved ? 'gdpApp.doctorProfile.verify.approved' : 'gdpApp.doctorProfile.verify.rejected');
      },
      error: () => {
        this.showError('gdpApp.doctorProfile.error.verifyFailed');
      },
    });
  }

  canVerify(): boolean {
    if (!this.doctorProfile || this.doctorProfile.status !== 'PENDING_APPROVAL') {
      return false;
    }

    return (
      this.currentAccount?.authorities.includes('ROLE_ADMIN') ??
      this.currentAccount?.authorities.includes('ROLE_RH') ??
      this.currentAccount?.authorities.includes('ROLE_DIRECTOR') ??
      false
    );
  }

  canViewAdministrative(): boolean {
    return (
      this.currentAccount?.authorities.includes('ROLE_ADMIN') ??
      this.currentAccount?.authorities.includes('ROLE_RH') ??
      this.currentAccount?.authorities.includes('ROLE_NURSE') ??
      false
    );
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

  getStatusLabel(status: string | null | undefined): string {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return status ? this.translateService.instant(`gdpApp.DoctorStatus.${status}`) : '';
  }

  getSpecialtyLabel(specialty: string | null | undefined): string {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return specialty ? this.translateService.instant(`gdpApp.MedicalSpecialty.${specialty}`) : '';
  }

  getYearsOfExperience(): number {
    if (!this.doctorProfile?.startDateOfPractice) {
      return 0;
    }
    return dayjs().diff(this.doctorProfile.startDateOfPractice, 'year');
  }

  getSpokenLanguagesArray(): string[] {
    if (!this.doctorProfile?.spokenLanguages) {
      return [];
    }
    return this.doctorProfile.spokenLanguages.split(',').map(lang => lang.trim());
  }

  copyToClipboard(text: string | null | undefined): void {
    if (!text) {
      return;
    }

    navigator.clipboard.writeText(text).then(
      () => {
        this.showSuccess('gdpApp.doctorProfile.detail.copiedToClipboard');
      },
      () => {
        this.showError('gdpApp.doctorProfile.detail.copyFailed');
      },
    );
  }

  showSuccess(messageKey: string): void {
    this.messageService.add({
      severity: 'success',
      summary: this.translateService.instant('global.messages.success'),
      detail: this.translateService.instant(messageKey),
      life: 5000,
    });
  }

  showError(messageKey: string): void {
    this.messageService.add({
      severity: 'error',
      summary: this.translateService.instant('global.messages.error'),
      detail: this.translateService.instant(messageKey),
      life: 5000,
    });
  }
}
