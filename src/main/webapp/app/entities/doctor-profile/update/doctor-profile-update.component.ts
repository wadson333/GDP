/* eslint-disable @typescript-eslint/no-unnecessary-condition */
/* eslint-disable @typescript-eslint/no-unsafe-return */
import { Component, EventEmitter, Input, OnInit, Output, ViewChild, inject } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';

import SharedModule from 'app/shared/shared.module';
import { TranslateService } from '@ngx-translate/core';

import { Gender } from 'app/entities/enumerations/gender.model';
import { BloodType } from 'app/entities/enumerations/blood-type.model';
import { MedicalSpecialty } from 'app/entities/enumerations/medical-specialty.model';
import { DoctorProfileService } from '../service/doctor-profile.service';
import { DoctorProfileWizardFormService } from './doctor-profile-wizard-form.service';
import { IDoctorProfileUser } from '../doctor-profile-user.model';

// PrimeNG Imports
import { StepperModule, Stepper } from 'primeng/stepper';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { CalendarModule } from 'primeng/calendar';
import { InputNumberModule } from 'primeng/inputnumber';
import { CheckboxModule } from 'primeng/checkbox';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { ChipsModule } from 'primeng/chips';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { TooltipModule } from 'primeng/tooltip';
import { AvatarModule } from 'primeng/avatar';
import { TagModule } from 'primeng/tag';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { ConfirmDialogModule } from 'primeng/confirmdialog';

interface DropdownOption {
  label: string;
  value: any;
}

@Component({
  standalone: true,
  selector: 'jhi-doctor-profile-update',
  templateUrl: './doctor-profile-update.component.html',
  imports: [
    SharedModule,
    FormsModule,
    ReactiveFormsModule,
    StepperModule,
    ButtonModule,
    InputTextModule,
    DropdownModule,
    CalendarModule,
    InputNumberModule,
    CheckboxModule,
    InputTextareaModule,
    ChipsModule,
    ToastModule,
    ProgressSpinnerModule,
    TooltipModule,
    AvatarModule,
    TagModule,
    ConfirmDialogModule,
  ],
  providers: [MessageService],
})
export class DoctorProfileUpdateComponent implements OnInit {
  @ViewChild('stepper') stepper!: Stepper;

  // Input properties for mode control
  @Input() mode: 'CREATE' | 'EDIT' = 'CREATE';
  @Input() doctorUid?: string;
  @Output() saveSuccess = new EventEmitter<void>();
  @Output() saveError = new EventEmitter<void>();

  isSaving = false;
  isLoading = false;
  doctorForm!: FormGroup;
  currentStepIndex = 0;

  // Current user account
  currentAccount: Account | null = null;

  // Enum options
  genderOptions: DropdownOption[] = [];
  bloodTypeOptions: DropdownOption[] = [];
  specialtyOptions: DropdownOption[] = [];
  languageOptions: DropdownOption[] = [
    { label: 'English', value: 'en' },
    { label: 'Français', value: 'fr' },
  ];

  // Date constraints
  maxBirthDate = new Date();
  minPracticeDate = new Date(1950, 0, 1);
  maxPracticeDate = new Date();

  protected doctorProfileService = inject(DoctorProfileService);
  protected doctorFormService = inject(DoctorProfileWizardFormService);
  protected activatedRoute = inject(ActivatedRoute);
  protected messageService = inject(MessageService);
  protected translateService = inject(TranslateService);
  protected accountService = inject(AccountService);
  protected confirmationService = inject(ConfirmationService);

  get isEditMode(): boolean {
    return this.mode === 'EDIT';
  }

  // get dialogTitle(): string {
  //   return this.isEditMode
  //     ? this.translateService.instant('gdpApp.doctorProfile.home.dialogEditTitle', { codeClinic: this.d })
  //     : this.translateService.instant('gdpApp.doctorProfile.home.dialogCreateTitle');
  // }

  ngOnInit(): void {
    this.loadCurrentAccount();
    this.initializeForm();
    this.initializeDropdownOptions();

    if (this.isEditMode && this.doctorUid) {
      this.loadDoctorData(this.doctorUid);
    } else {
      this.loadDoctorDataFromRoute();
    }
  }

  loadCurrentAccount(): void {
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
      this.applyFieldProtectionRules();
    });
  }

  initializeForm(): void {
    this.doctorForm = this.doctorFormService.createDoctorForm();
  }

  initializeDropdownOptions(): void {
    // Gender options
    this.genderOptions = Object.keys(Gender).map(key => ({
      label: this.translateService.instant(`gdpApp.Gender.${key}`),
      value: key,
    }));

    // Blood type options
    this.bloodTypeOptions = Object.keys(BloodType).map(key => ({
      label: this.translateService.instant(`gdpApp.BloodType.${key}`),
      value: key,
    }));

    // Specialty options
    this.specialtyOptions = Object.keys(MedicalSpecialty).map(key => ({
      label: this.translateService.instant(`gdpApp.MedicalSpecialty.${key}`),
      value: key,
    }));
  }

  loadDoctorDataFromRoute(): void {
    this.activatedRoute.data.subscribe(({ doctorProfile }) => {
      if (doctorProfile) {
        this.mode = 'EDIT';
        this.doctorUid = doctorProfile.uid;
        this.loadDoctorData(doctorProfile.uid);
      }
    });
  }

  loadDoctorData(uid: string): void {
    this.isLoading = true;
    this.doctorProfileService.getWithUser(uid).subscribe({
      next: (res: HttpResponse<IDoctorProfileUser>) => {
        if (res.body) {
          this.populateFormWithData(res.body);
          this.applyFieldProtectionRules();
        }
        this.isLoading = false;
      },
      error: (err: HttpErrorResponse) => {
        console.error('Error loading doctor data:', err);
        this.showError('gdpApp.doctorProfile.messages.loadError');
        this.isLoading = false;
      },
    });
  }

  populateFormWithData(doctorData: IDoctorProfileUser): void {
    // Convert comma-separated strings to arrays for p-chips
    const spokenLanguages = doctorData.spokenLanguages
      ? doctorData.spokenLanguages
          .split(',')
          .map(lang => lang.trim())
          .filter(lang => lang.length > 0)
      : [];

    const otherSpecialties = doctorData.otherSpecialties
      ? doctorData.otherSpecialties
          .split(',')
          .map(spec => spec.trim())
          .filter(spec => spec.length > 0)
      : [];
    // Convert Dayjs dates to JavaScript Date objects for p-calendar
    const formData = {
      ...doctorData,
      uid: doctorData.uid,
      birthDate: doctorData.birthDate ? doctorData.birthDate.toDate() : null,
      startDateOfPractice: doctorData.startDateOfPractice ? doctorData.startDateOfPractice.toDate() : null,
      spokenLanguages,
      otherSpecialties,
    };

    this.doctorForm.patchValue(formData);
  }

  /**
   * Apply role-based field protection rules.
   * - Always disable: login, email (identity fixed)
   * - If user is NOT (ROLE_ADMIN or ROLE_NURSE):
   *   - Disable: medicalLicenseNumber, nif, ninu
   *   - Disable: firstName, lastName (identity fixed)
   */
  applyFieldProtectionRules(): void {
    if (!this.isEditMode || !this.currentAccount) {
      return;
    }

    const authorities = this.currentAccount.authorities ?? [];
    const isAdminOrNurse = authorities.includes('ROLE_ADMIN') || authorities.includes('ROLE_NURSE');

    // Always disable identity fields
    if (this.mode === 'CREATE') {
      this.disableField('login');
      this.disableField('email');
    }

    // If not admin or nurse, disable additional fields
    if (!isAdminOrNurse) {
      this.disableField('medicalLicenseNumber');
      this.disableField('nif');
      this.disableField('ninu');
      this.disableField('firstName');
      this.disableField('lastName');
    }
  }

  disableField(fieldName: string): void {
    const control = this.doctorForm.get(fieldName);
    if (control) {
      control.disable();
    }
  }

  onStepChange(event: any): void {
    this.currentStepIndex = event.index;
  }

  canProceedToNextStep(): boolean {
    return this.doctorFormService.isStepValid(this.doctorForm, this.currentStepIndex, this.mode);
  }

  previousStep(): void {
    if (this.stepper && this.currentStepIndex > 0) {
      this.currentStepIndex--;
      this.stepper.activeStep = this.currentStepIndex;
    }
  }

  nextStep(): void {
    if (this.canProceedToNextStep() && this.stepper) {
      this.currentStepIndex++;
      this.stepper.activeStep = this.currentStepIndex;
      // this.stepper.nextCallback({}, this.currentStepIndex);
    } else {
      this.markStepFieldsAsTouched(this.currentStepIndex);
      this.showWarning('gdpApp.doctorProfile.validation.completeStep');
    }
  }

  markStepFieldsAsTouched(stepIndex: number): void {
    const fields = this.getStepFields(stepIndex);
    fields.forEach(field => {
      const control = this.doctorForm.get(field);
      if (control) {
        control.markAsTouched();
        control.markAsDirty();
      }
    });
  }

  getStepFields(stepIndex: number): string[] {
    let defaultFields: string[] = [];
    switch (stepIndex) {
      case 0:
        defaultFields = ['langKey', 'firstName', 'lastName', 'birthDate', 'gender', 'bloodType', 'nif', 'ninu'];
        break;
      case 1:
        defaultFields = [
          'medicalLicenseNumber',
          'primarySpecialty',
          'startDateOfPractice',
          'university',
          'graduationYear',
          'otherSpecialties',
          'spokenLanguages',
          'bio',
        ];
        break;
      case 2:
        defaultFields = [
          'consultationDurationMinutes',
          'acceptingNewPatients',
          'allowsTeleconsultation',
          'consultationFee',
          'teleconsultationFee',
          'officePhone',
          'officeAddress',
          'websiteUrl',
        ];
        break;
      default:
        defaultFields = [];
    }
    if (this.mode === 'CREATE' && stepIndex === 0) {
      defaultFields.push('login', 'email'); // Add control flags in CREATE mode
    }
    return defaultFields;
  }

  save(): void {
    if (this.doctorForm.invalid) {
      this.markAllFieldsAsTouched();
      const errorStepIndex = this.doctorFormService.findStepWithErrors(this.doctorForm, this.mode);
      if (this.stepper && errorStepIndex !== this.currentStepIndex) {
        this.currentStepIndex = errorStepIndex;
      }
      this.showError('gdpApp.doctorProfile.validation.formInvalid');
      return;
    }

    this.confirmationService.confirm({
      message:
        this.mode === 'EDIT'
          ? this.translateService.instant('gdpApp.doctorProfile.updateConfirmationDialogTitleQuestion')
          : this.translateService.instant('gdpApp.doctorProfile.createConfirmationDialogTitleQuestion'),
      header:
        this.mode === 'EDIT'
          ? this.translateService.instant('gdpApp.doctorProfile.updateConfirmationDialogTitle')
          : this.translateService.instant('gdpApp.doctorProfile.createConfirmationDialogTitle'),
      icon: 'pi pi-times-circle',
      acceptIcon: 'pi pi-check',
      rejectIcon: 'pi pi-times',
      acceptLabel:
        this.mode === 'EDIT' ? this.translateService.instant('entity.action.update') : this.translateService.instant('entity.action.save'),
      rejectLabel: this.translateService.instant('entity.action.cancel'),
      accept: () => {
        this.isSaving = true;
        const doctor = this.cleanEmptyStrings(this.doctorFormService.getDoctorFromForm(this.doctorForm));

        const saveObservable = this.isEditMode
          ? this.doctorProfileService.updateWithUser(doctor)
          : this.doctorProfileService.createWithUser(doctor);

        this.subscribeToSaveResponse(saveObservable);
      },
      reject: () => {
        this.isSaving = false;
      },
    });
  }

  handleFieldErrors(fieldErrors: any[]): void {
    let errorStepIndex = -1;

    fieldErrors.forEach((fieldError: any) => {
      const fieldName = fieldError.field;
      const control = this.doctorForm.get(fieldName);

      if (control) {
        control.setErrors({ server: fieldError.message });
        control.markAsTouched();

        // Find which step contains this field
        if (errorStepIndex === -1) {
          for (let i = 0; i < 3; i++) {
            if (this.getStepFields(i).includes(fieldName)) {
              errorStepIndex = i;
              break;
            }
          }
        }
      }
    });

    // Navigate to the first step with errors
    if (errorStepIndex !== -1 && this.stepper && errorStepIndex !== this.currentStepIndex) {
      this.currentStepIndex = errorStepIndex;
    }

    this.showError('gdpApp.doctorProfile.validation.serverError');
  }

  markAllFieldsAsTouched(): void {
    Object.keys(this.doctorForm.controls).forEach(key => {
      const control = this.doctorForm.get(key);
      if (control) {
        control.markAsTouched();
        control.markAsDirty();
      }
    });
  }

  reset(): void {
    this.doctorFormService.resetForm(this.doctorForm);
    if (this.stepper) {
      this.currentStepIndex = 0;
    }
  }

  canDeactivate(): boolean {
    return !this.doctorForm.dirty || confirm(this.translateService.instant('gdpApp.doctorProfile.messages.confirmDiscard'));
  }

  // Toast helpers
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

  showWarning(messageKey: string): void {
    this.messageService.add({
      severity: 'warn',
      summary: this.translateService.instant('global.messages.warning'),
      detail: this.translateService.instant(messageKey),
      life: 3000,
    });
  }

  // Utility methods for template
  isFieldInvalid(fieldName: string): boolean {
    const field = this.doctorForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }

  isFieldDisabled(fieldName: string): boolean {
    const field = this.doctorForm.get(fieldName);
    return field ? field.disabled : false;
  }

  getFieldError(fieldName: string): string {
    const field = this.doctorForm.get(fieldName);
    if (field?.errors) {
      if (field.errors['required']) return this.translateService.instant('gdpApp.doctorProfile.validation.required');
      if (field.errors['email']) return this.translateService.instant('gdpApp.doctorProfile.validation.email');
      if (field.errors['minlength']) return this.translateService.instant('gdpApp.doctorProfile.validation.minlength');
      if (field.errors['maxlength']) return this.translateService.instant('gdpApp.doctorProfile.validation.maxlength');
      if (field.errors['pattern']) return this.translateService.instant('gdpApp.doctorProfile.validation.pattern');
      if (field.errors['min']) return this.translateService.instant('gdpApp.doctorProfile.validation.min');
      if (field.errors['max']) return this.translateService.instant('gdpApp.doctorProfile.validation.max');
      if (field.errors['server']) return field.errors['server'];
    }
    return '';
  }

  getInitials(firstName: string | null | undefined, lastName: string | null | undefined): string {
    const firstInitial = firstName ? firstName.trim().charAt(0).toUpperCase() : '';
    const lastInitial = lastName ? lastName.trim().charAt(0).toUpperCase() : '';

    if (firstInitial === '' || lastInitial === '') {
      return 'N/A';
    }
    return (firstInitial + lastInitial).toUpperCase();
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<any>>): void {
    result.subscribe({
      next: () => this.onSaveSuccess(),
      error: (error: HttpErrorResponse) => this.onSaveError(error),
      complete: () => this.onSaveFinalize(),
    });
  }

  protected onSaveSuccess(): void {
    const messageKey = this.isEditMode ? 'gdpApp.doctorProfile.updated' : 'gdpApp.doctorProfile.created';
    this.showSuccess(messageKey);
    this.saveSuccess.emit();
  }

  protected onSaveError(error: HttpErrorResponse): void {
    if (error.error?.fieldErrors) {
      this.handleFieldErrors(error.error.fieldErrors);
    } else {
      this.showError('error.internalServerError');
    }
    this.saveError.emit();
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected cleanEmptyStrings(obj: any): any {
    const cleaned = { ...obj };
    Object.keys(cleaned).forEach(key => {
      if (cleaned[key] === '') {
        cleaned[key] = null;
      }
    });
    return cleaned;
  }
}
