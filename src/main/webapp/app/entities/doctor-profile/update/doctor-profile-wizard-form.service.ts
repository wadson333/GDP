/* eslint-disable prettier/prettier */
import { Injectable } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import dayjs from 'dayjs/esm';
import { IDoctorProfileUser } from '../doctor-profile-user.model';

/**
 * Service for managing the Doctor Profile Wizard form.
 * Handles form creation, validation, and data transformation.
 */
@Injectable({ providedIn: 'root' })
export class DoctorProfileWizardFormService {
  constructor(private fb: FormBuilder) {}

  /**
   * Creates a reactive form group for doctor profile creation.
   * Organized by wizard steps but using a flattened structure.
   */
  createDoctorForm(): FormGroup {
    return this.fb.group({
      uid: [null],
      // Step 1: Account & Personal
      // Account fields
      login: [
        '',
        [
          Validators.required,
          Validators.minLength(1),
          Validators.maxLength(50),
          Validators.pattern('^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$'),
        ],
      ],
      email: ['', [Validators.required, Validators.email, Validators.minLength(5), Validators.maxLength(254)]],
      langKey: ['en', [Validators.minLength(2), Validators.maxLength(10)]],
      sendActivationEmail: [true],
      activatedOnCreate: [false],

      // Identity
      firstName: ['', [Validators.required, Validators.maxLength(50)]],
      lastName: ['', [Validators.required, Validators.maxLength(50)]],
      birthDate: [null, [Validators.required]],

      // Demographics
      gender: [null],
      bloodType: [null],

      // IDs
      nif: ['', [Validators.minLength(10), Validators.maxLength(10)]],
      ninu: ['', [Validators.minLength(10), Validators.maxLength(10)]],

      // Step 2: Professional Info
      medicalLicenseNumber: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(50)]],
      primarySpecialty: [null, [Validators.required]],
      startDateOfPractice: [null, [Validators.required]],
      university: ['', [Validators.maxLength(100)]],
      graduationYear: [null, [Validators.min(1950), Validators.max(2100)]],
      otherSpecialties: [''],
      spokenLanguages: [''],
      bio: [''],

      // Step 3: Practice & Fees
      consultationDurationMinutes: [30, [Validators.required, Validators.min(5), Validators.max(120)]],
      acceptingNewPatients: [true, [Validators.required]],
      allowsTeleconsultation: [false, [Validators.required]],
      consultationFee: [null, [Validators.min(0)]],
      teleconsultationFee: [null, [Validators.min(0)]],
      officePhone: ['', [Validators.pattern('^\\+?[1-9]\\d{1,14}$')]],
      officeAddress: [''],
      websiteUrl: ['', [Validators.maxLength(255)]],
    });
  }

  /**
   * Resets the form to its initial state.
   */
  resetForm(form: FormGroup): void {
    form.reset({
      langKey: 'en',
      sendActivationEmail: true,
      activatedOnCreate: false,
      consultationDurationMinutes: 30,
      acceptingNewPatients: true,
      allowsTeleconsultation: false,
    });
  }

  /**
   * Extracts and transforms form data to IDoctorProfileUser format.
   * Converts Date objects to Dayjs instances.
   */
  getDoctorFromForm(form: FormGroup): IDoctorProfileUser {
    const rawValue = form.getRawValue();
    // Convert spokenLanguages array to comma-separated string
    const spokenLanguages = Array.isArray(rawValue.spokenLanguages) ? rawValue.spokenLanguages.join(', ') : rawValue.spokenLanguages;
    const otherSpecialties = Array.isArray(rawValue.otherSpecialties) ? rawValue.otherSpecialties.join(', ') : rawValue.otherSpecialties;

    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return {
      ...rawValue,
      birthDate: rawValue.birthDate ? dayjs(rawValue.birthDate) : dayjs(),
      startDateOfPractice: rawValue.startDateOfPractice ? dayjs(rawValue.startDateOfPractice) : dayjs(),
      spokenLanguages,
      otherSpecialties,
    };
  }

  /**
   * Populates form with existing doctor data for editing.
   */
  populateForm(form: FormGroup, doctor: IDoctorProfileUser): void {
    // Convert comma-separated string to array for p-chips
    const spokenLanguages = doctor.spokenLanguages ? doctor.spokenLanguages.split(',').map(lang => lang.trim()) : [];

    const otherSpecialties = doctor.otherSpecialties ? doctor.otherSpecialties.split(',').map(spec => spec.trim()) : [];

    form.patchValue({
      ...doctor,
      birthDate: doctor.birthDate.toDate(),
      startDateOfPractice: doctor.startDateOfPractice.toDate(),
      spokenLanguages,
      otherSpecialties,
    });
  }

  /**
   * Validates a specific step of the wizard.
   */
  isStepValid(form: FormGroup, stepIndex: number, mode: 'CREATE' | 'EDIT'): boolean {
    const stepFields = this.getStepFields(stepIndex, mode);
    return stepFields.every(field => {
      const control = form.get(field);
      return control?.valid;
    });
  }

  /**
   * Finds the step index that contains a field with errors.
   */
  findStepWithErrors(form: FormGroup, mode: 'CREATE' | 'EDIT'): number {
    for (let i = 0; i < 3; i++) {
      const fields = this.getStepFields(i, mode);
      if (fields.some(field => form.get(field)?.invalid)) {
        return i;
      }
    }
    return 0;
  }

  /**
   * Gets the list of fields for a specific step.
   */
  private getStepFields(stepIndex: number, mode: 'CREATE' | 'EDIT' = 'EDIT'): string[] {
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

    if (mode === 'CREATE' && stepIndex === 0) {
      defaultFields.push('login', 'email'); // Add control flags in CREATE mode
    }
    return defaultFields;
  }
}
