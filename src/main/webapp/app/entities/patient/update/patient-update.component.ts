/* eslint-disable @typescript-eslint/no-unsafe-return */
import { Component, OnInit, inject, Output, EventEmitter, Input, OnChanges, SimpleChanges } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { Gender } from 'app/entities/enumerations/gender.model';
import { BloodType } from 'app/entities/enumerations/blood-type.model';
import { SmokingStatus } from 'app/entities/enumerations/smoking-status.model';
import { PatientStatus } from 'app/entities/enumerations/patient-status.model';
import { IPatient } from '../patient.model';
import { PatientService } from '../service/patient.service';
import { IPatientUser } from '../patient-user.model';
import { ConfirmationService, MessageService } from 'primeng/api';

// PrimeNG Imports
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { InputNumberModule } from 'primeng/inputnumber';
import { CheckboxModule } from 'primeng/checkbox';
import { StepperModule } from 'primeng/stepper';
import { ToastModule } from 'primeng/toast';
import { DividerModule } from 'primeng/divider';
import { TranslateService } from '@ngx-translate/core';
import { TooltipModule } from 'primeng/tooltip';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { FormatMediumDatePipe } from 'app/shared/date';

@Component({
  standalone: true,
  selector: 'jhi-patient-update',
  templateUrl: './patient-update.component.html',
  styleUrls: ['./patient-update.component.scss'],
  imports: [
    SharedModule,
    FormsModule,
    ReactiveFormsModule,
    FormatMediumDatePipe,
    ButtonModule,
    InputTextModule,
    CalendarModule,
    DropdownModule,
    InputTextareaModule,
    InputNumberModule,
    CheckboxModule,
    StepperModule,
    ToastModule,
    DividerModule,
    TooltipModule,
    ConfirmDialogModule,
    ProgressSpinnerModule,
  ],
  providers: [],
})
export class PatientUpdateComponent implements OnInit {
  @Input() isDialog = false;
  @Input() mode: 'CREATE' | 'EDIT' = 'CREATE';
  @Input() patientUid?: string | null;
  @Output() patientSaved = new EventEmitter<IPatient>();
  @Output() cancelled = new EventEmitter<void>();

  isSaving = false;
  patient: IPatient | null = null;
  currentStep = 0;

  usersSharedCollection: IUser[] = [];

  minBirthDate = new Date(1900, 0, 1);
  maxBirthDate = new Date();
  today = new Date();

  protected patientService = inject(PatientService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);
  protected messageService = inject(MessageService);
  protected confirmationService = inject(ConfirmationService);
  // Helper for translate service
  private translateService = inject(TranslateService);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FormGroup = new FormGroup({
    // System Fields
    uid: new FormControl({ value: '', disabled: true }),

    // User Fields
    login: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.pattern(/^[_.@A-Za-z0-9-]+$/)],
    }),
    email: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.email],
    }),
    firstName: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    lastName: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    langKey: new FormControl('en'),

    // Patient Basic Info
    birthDate: new FormControl(null, {
      validators: [Validators.required],
    }),
    gender: new FormControl(null),
    bloodType: new FormControl(null),
    status: new FormControl(PatientStatus.ACTIVE),

    // Contact Information
    address: new FormControl(''),
    phone1: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.pattern(/^\+?[1-9]\d{1,14}$/)],
    }),
    phone2: new FormControl('', {
      validators: [Validators.pattern(/^\+?[1-9]\d{1,14}$/)],
    }),

    // Identity Documents
    nif: new FormControl('', {
      validators: [Validators.minLength(10), Validators.maxLength(10)],
    }),
    ninu: new FormControl('', {
      validators: [Validators.minLength(10), Validators.maxLength(10)],
    }),
    passportNumber: new FormControl('', {
      validators: [Validators.minLength(3), Validators.maxLength(15)],
    }),

    // Physical Parameters
    heightCm: new FormControl(null, {
      validators: [Validators.min(0), Validators.max(300)],
    }),
    weightKg: new FormControl(null, {
      validators: [Validators.min(0), Validators.max(500)],
    }),

    // Emergency Contact
    contactPersonName: new FormControl(''),
    contactPersonPhone: new FormControl('', {
      validators: [Validators.pattern(/^\+?[1-9]\d{1,14}$/)],
    }),

    // Medical Information
    antecedents: new FormControl(''),
    allergies: new FormControl(''),
    clinicalNotes: new FormControl(''),
    smokingStatus: new FormControl(null),

    // GDPR & Legal
    gdprConsentDate: new FormControl(null),
    deceasedDate: new FormControl(null),

    // Insurance Information
    insuranceCompanyName: new FormControl(''),
    patientInsuranceId: new FormControl(''),
    insurancePolicyNumber: new FormControl(''),
    insuranceCoverageType: new FormControl(''),
    insuranceValidFrom: new FormControl(null),
    insuranceValidTo: new FormControl(null),

    // Control Flags
    sendActivationEmail: new FormControl(true),
    activatedOnCreate: new FormControl(false),
  });

  ngOnInit(): void {
    if (this.mode === 'EDIT' && this.patientUid) {
      this.loadPatientForEdit(this.patientUid);
    } else if (!this.isDialog) {
      this.activatedRoute.data.subscribe(({ patient }) => {
        this.patient = patient;
        if (patient) {
          this.updateForm(patient);
        }
        this.loadRelationshipsOptions();
      });
    }
    this.currentStep = 0;
    // Apply field restrictions based on mode
    this.applyModeRestrictions();
  }

  get blodTypeOptions(): { label: string; value: string }[] {
    return [
      { label: this.translateService.instant('gdpApp.patient.enumValues.bloodType.aPositive'), value: 'A_POS' },
      { label: this.translateService.instant('gdpApp.patient.enumValues.bloodType.aNegative'), value: 'A_NEG' },
      { label: this.translateService.instant('gdpApp.patient.enumValues.bloodType.bPositive'), value: 'B_POS' },
      { label: this.translateService.instant('gdpApp.patient.enumValues.bloodType.bNegative'), value: 'B_NEG' },
      { label: this.translateService.instant('gdpApp.patient.enumValues.bloodType.abPositive'), value: 'AB_POS' },
      { label: this.translateService.instant('gdpApp.patient.enumValues.bloodType.abNegative'), value: 'AB_NEG' },
      { label: this.translateService.instant('gdpApp.patient.enumValues.bloodType.oPositive'), value: 'O_POS' },
      { label: this.translateService.instant('gdpApp.patient.enumValues.bloodType.oNegative'), value: 'O_NEG' },
      { label: this.translateService.instant('gdpApp.patient.enumValues.bloodType.unknown'), value: 'UNKNOWN' },
    ];
  }

  get genderOptions(): { label: string; value: string }[] {
    return [
      { label: this.translateService.instant('gdpApp.patient.enumValues.gender.male'), value: 'MALE' },
      { label: this.translateService.instant('gdpApp.patient.enumValues.gender.female'), value: 'FEMALE' },
      { label: this.translateService.instant('gdpApp.patient.enumValues.gender.other'), value: 'OTHER' },
    ];
  }

  get smokingStatusOptions(): { label: string; value: string }[] {
    return [
      { label: this.translateService.instant('gdpApp.patient.enumValues.smokingStatus.never'), value: 'NEVER' },
      { label: this.translateService.instant('gdpApp.patient.enumValues.smokingStatus.current'), value: 'CURRENT' },
      { label: this.translateService.instant('gdpApp.patient.enumValues.smokingStatus.former'), value: 'FORMER' },
    ];
  }

  previousState(): void {
    if (this.isDialog) {
      this.cancelled.emit();
    } else {
      window.history.back();
    }
  }

  save(): void {
    this.isSaving = true;
    const patientUser = this.createFromForm();
    const cleanedPatientUser = this.cleanEmptyStrings(patientUser);
    this.confirmationService.confirm({
      message: this.translateService.instant('gdpApp.patient.confirmation.header'),
      header: this.translateService.instant('gdpApp.patient.confirmation.save'),
      rejectButtonStyleClass: 'p-button-sm p-button-text',
      acceptButtonStyleClass: 'p-button-sm',
      accept: () => {
        if (this.mode === 'EDIT') {
          this.subscribeToSaveResponse(this.patientService.updatePatientWithUser(cleanedPatientUser));
        } else {
          this.subscribeToSaveResponse(this.patientService.createPatientWithUser(cleanedPatientUser));
        }
      },
      reject: () => {
        this.isSaving = false;
      },
    });
  }

  // Wizard Navigation
  nextStep(): void {
    if (this.isCurrentStepValid()) {
      this.currentStep++;
    } else {
      this.editForm.markAllAsTouched(); // This forces red borders to appear on invalid fields

      // SHOW ERROR TOAST
      this.messageService.add({
        severity: 'warn',
        summary: this.translateService.instant('entity.validation.required'),
        detail: this.translateService.instant('gdpApp.patient.validation.stepInvalid'),
        life: 3000,
      });
    }
  }

  previousStep(): void {
    if (this.currentStep > 0) {
      this.currentStep--;
    }
  }

  goToStep(index: number): void {
    // Allow navigation to previous steps or current step only
    if (index <= this.currentStep) {
      this.currentStep = index;
    }
  }

  isCurrentStepValid(): boolean {
    const stepFields = this.getStepFields(this.currentStep);
    return stepFields.every(fieldName => {
      const control = this.editForm.get(fieldName);
      return control ? control.valid : true;
    });
  }

  isStepValid(stepIndex: number): boolean {
    const stepFields = this.getStepFields(stepIndex);
    return stepFields.every(fieldName => {
      const control = this.editForm.get(fieldName);
      return control ? control.valid : true;
    });
  }

  getStepFields(stepIndex: number): string[] {
    const stepFieldsMap: Record<number, string[]> = {
      0: ['email', 'firstName', 'lastName', 'langKey', 'birthDate', 'gender', 'bloodType', 'status'], // User Info
      1: ['phone1', 'phone2', 'address', 'nif', 'ninu', 'passportNumber'], // Contact & Identity
      2: ['antecedents', 'allergies', 'clinicalNotes', 'smokingStatus', 'heightCm', 'weightKg'], // Medical
      3: [
        'insuranceCompanyName',
        'patientInsuranceId',
        'insurancePolicyNumber',
        'insuranceCoverageType',
        'insuranceValidFrom',
        'insuranceValidTo',
      ], // Insurance
      4: [], // Review (all fields)
    };
    if (this.mode === 'CREATE') {
      stepFieldsMap[0].push('login'); // Add control flags in CREATE mode
    }
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    return stepFieldsMap[stepIndex] || [];
  }

  navigateToFieldStep(fieldName: string): void {
    for (let i = 0; i < 6; i++) {
      if (this.getStepFields(i).includes(fieldName)) {
        this.currentStep = i;
        break;
      }
    }
  }

  hasFieldError(fieldName: string): boolean {
    const control = this.editForm.get(fieldName);
    return !!(control && control.invalid && (control.dirty || control.touched));
  }

  getFieldError(fieldName: string): string | null {
    const control = this.editForm.get(fieldName);
    if (!control?.errors) {
      return null;
    }

    if (control.errors['required']) {
      return this.translateService.instant(`gdpApp.patient.validation.${fieldName}Required`);
    }
    if (control.errors['email']) {
      return this.translateService.instant('gdpApp.patient.validation.emailInvalid');
    }
    if (control.errors['pattern']) {
      return this.translateService.instant(`gdpApp.patient.validation.${fieldName}Pattern`);
    }
    if (control.errors['minlength'] || control.errors['maxlength']) {
      return this.translateService.instant(`gdpApp.patient.validation.${fieldName}Length`);
    }
    if (control.errors['duplicate']) {
      return this.translateService.instant(`gdpApp.patient.errors.${fieldName}AlreadyUsed`);
    }

    return null;
  }

  getDialogTitle(): string {
    return this.mode === 'EDIT'
      ? this.translateService.instant('gdpApp.patient.wizard.titleEdit')
      : this.translateService.instant('gdpApp.patient.wizard.title');
  }
  /**
   * Get the translated label for gender
   */
  getGenderLabel(): string | undefined {
    const genderValue = this.editForm.get('gender')?.value;
    if (!genderValue) return '-';
    return this.genderOptions.find(option => option.value === genderValue)?.label;
    // return this.translateService.instant(`gdpApp.patient.enumValues.gender.${genderValue}`);
  }

  /**
   * Get the translated label for blood type
   */
  getBloodTypeLabel(): string | undefined {
    const bloodTypeValue = this.editForm.get('bloodType')?.value;
    if (!bloodTypeValue) return '-';
    return this.blodTypeOptions.find(option => option.value === bloodTypeValue)?.label;
  }

  /**
   * Get the translated label for smoking status
   */
  getSmokingStatusLabel(): string | undefined {
    const smokingStatusValue = this.editForm.get('smokingStatus')?.value;
    if (!smokingStatusValue) return '-';
    return this.smokingStatusOptions.find(option => option.value === smokingStatusValue)?.label;
  }

  public onOpen(): void {
    if (this.mode === 'CREATE') {
      this.editForm.reset();
    }
    this.currentStep = 0;
  }

  protected applyModeRestrictions(): void {
    if (this.mode === 'EDIT') {
      // Disable immutable fields
      this.editForm.get('login')?.disable();
      this.editForm.get('email')?.markAsTouched();
    }
  }

  protected loadPatientForEdit(uid: string): void {
    this.isSaving = true;
    this.patientService.findPatientWithUser(uid).subscribe({
      next: response => {
        const patientUser = response.body;
        if (patientUser) {
          this.updateFormWithPatientUser(patientUser);
        }
        this.isSaving = false;
      },
      error: error => {
        console.error('Error loading patient:', error);
        this.messageService.add({
          severity: 'error',
          summary: this.translateService.instant('error.title'),
          detail: this.translateService.instant('gdpApp.patient.errors.loadFailed'),
          life: 5000,
        });
        this.isSaving = false;
      },
    });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPatient>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: response => this.onSaveSuccess(response.body),
      error: error => this.onSaveError(error),
    });
  }

  protected updateFormWithPatientUser(patientUser: IPatientUser): void {
    this.editForm.patchValue({
      uid: patientUser.uid,
      login: patientUser.login,
      email: patientUser.email,
      firstName: patientUser.firstName,
      lastName: patientUser.lastName,
      langKey: patientUser.langKey,
      birthDate: patientUser.birthDate ? new Date(patientUser.birthDate.toString()) : null,
      gender: patientUser.gender,
      bloodType: patientUser.bloodType,
      status: patientUser.status,
      address: patientUser.address,
      phone1: patientUser.phone1,
      phone2: patientUser.phone2,
      nif: patientUser.nif,
      ninu: patientUser.ninu,
      passportNumber: patientUser.passportNumber,
      heightCm: patientUser.heightCm,
      weightKg: patientUser.weightKg,
      contactPersonName: patientUser.contactPersonName,
      contactPersonPhone: patientUser.contactPersonPhone,
      antecedents: patientUser.antecedents,
      allergies: patientUser.allergies,
      clinicalNotes: patientUser.clinicalNotes,
      smokingStatus: patientUser.smokingStatus,
      gdprConsentDate: patientUser.gdprConsentDate ? new Date(patientUser.gdprConsentDate.toString()) : null,
      deceasedDate: patientUser.deceasedDate ? new Date(patientUser.deceasedDate.toString()) : null,
      insuranceCompanyName: patientUser.insuranceCompanyName,
      patientInsuranceId: patientUser.patientInsuranceId,
      insurancePolicyNumber: patientUser.insurancePolicyNumber,
      insuranceCoverageType: patientUser.insuranceCoverageType,
      insuranceValidFrom: patientUser.insuranceValidFrom ? new Date(patientUser.insuranceValidFrom.toString()) : null,
      insuranceValidTo: patientUser.insuranceValidTo ? new Date(patientUser.insuranceValidTo.toString()) : null,
    });
  }

  protected onSaveSuccess(patient: IPatient | null): void {
    if (patient) {
      const fullName = `${patient.firstName} ${patient.lastName}`;
      const messageKey = this.mode === 'EDIT' ? 'gdpApp.patient.success.updated' : 'gdpApp.patient.success.created';
      const detailKey = this.mode === 'EDIT' ? 'gdpApp.patient.success.updatedDetail' : 'gdpApp.patient.success.createdDetail';

      this.messageService.add({
        severity: 'success',
        summary: this.translateService.instant(messageKey),
        detail: this.translateService.instant(detailKey, {
          name: fullName,
          mrn: patient.medicalRecordNumber,
        }),
        life: 5000,
      });

      if (this.isDialog) {
        this.patientSaved.emit(patient);
        this.editForm.reset();
        this.currentStep = 0;
      } else {
        this.previousState();
      }
    }
  }

  protected onSaveError(error: any): void {
    let errorMessage = this.translateService.instant(
      this.mode === 'EDIT' ? 'gdpApp.patient.errors.updateFailed' : 'gdpApp.patient.errors.createFailed',
    );
    let fieldError: string | null = null;

    // Map backend error codes to form fields
    if (error.error) {
      const errorKey = error.error.errorKey || error.error.message;

      if (errorKey) {
        if (errorKey.includes('nifexists') || errorKey.includes('NIF')) {
          errorMessage = this.translateService.instant('gdpApp.patient.errors.nifAlreadyUsed');
          fieldError = 'nif';
        } else if (errorKey.includes('ninuexists') || errorKey.includes('NINU')) {
          errorMessage = this.translateService.instant('gdpApp.patient.errors.ninuAlreadyUsed');
          fieldError = 'ninu';
        } else if (errorKey.includes('passportexists') || errorKey.includes('Passport')) {
          errorMessage = this.translateService.instant('gdpApp.patient.errors.passportAlreadyUsed');
          fieldError = 'passportNumber';
        } else if (errorKey.includes('insuranceidexists') || errorKey.includes('Insurance')) {
          errorMessage = this.translateService.instant('gdpApp.patient.errors.insuranceIdAlreadyUsed');
          fieldError = 'patientInsuranceId';
        } else if (errorKey.includes('userexists') || errorKey.includes('Login')) {
          errorMessage = this.translateService.instant('gdpApp.patient.errors.usernameAlreadyUsed');
          fieldError = 'login';
        } else if (errorKey.includes('emailexists') || errorKey.includes('Email')) {
          errorMessage = this.translateService.instant('gdpApp.patient.errors.emailAlreadyUsed');
          fieldError = 'email';
        }
      }
    }

    // Set field-specific error if identified
    if (fieldError) {
      const control = this.editForm.get(fieldError);
      if (control) {
        control.setErrors({ duplicate: true });
        control.markAsTouched();
        this.navigateToFieldStep(fieldError);
      }
    }

    this.messageService.add({
      severity: 'error',
      summary: this.translateService.instant('error.title'),
      detail: errorMessage,
      life: 5000,
    });
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(patient: IPatient): void {
    this.patient = patient;
    // This would be for edit mode - not applicable for creation dialog
  }

  protected createFromForm(): IPatientUser {
    const formValue = this.editForm.getRawValue();
    return {
      uid: formValue.uid || undefined,
      ...formValue,
    };
  }

  protected loadRelationshipsOptions(): void {
    // Load any dropdown options if needed
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
