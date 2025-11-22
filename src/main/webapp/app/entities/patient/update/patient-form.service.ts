import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPatient, NewPatient } from '../patient.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPatient for edit and NewPatientFormGroupInput for create.
 */
type PatientFormGroupInput = IPatient | PartialWithRequiredKeyOf<NewPatient>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPatient | NewPatient> = Omit<T, 'gdprConsentDate' | 'deceasedDate'> & {
  gdprConsentDate?: string | null;
  deceasedDate?: string | null;
};

type PatientFormRawValue = FormValueOf<IPatient>;

type NewPatientFormRawValue = FormValueOf<NewPatient>;

type PatientFormDefaults = Pick<NewPatient, 'id' | 'gdprConsentDate' | 'deceasedDate'>;

type PatientFormGroupContent = {
  id: FormControl<PatientFormRawValue['id'] | NewPatient['id']>;
  uid: FormControl<PatientFormRawValue['uid']>;
  firstName: FormControl<PatientFormRawValue['firstName']>;
  lastName: FormControl<PatientFormRawValue['lastName']>;
  birthDate: FormControl<PatientFormRawValue['birthDate']>;
  gender: FormControl<PatientFormRawValue['gender']>;
  bloodType: FormControl<PatientFormRawValue['bloodType']>;
  address: FormControl<PatientFormRawValue['address']>;
  phone1: FormControl<PatientFormRawValue['phone1']>;
  phone2: FormControl<PatientFormRawValue['phone2']>;
  nif: FormControl<PatientFormRawValue['nif']>;
  ninu: FormControl<PatientFormRawValue['ninu']>;
  medicalRecordNumber: FormControl<PatientFormRawValue['medicalRecordNumber']>;
  heightCm: FormControl<PatientFormRawValue['heightCm']>;
  weightKg: FormControl<PatientFormRawValue['weightKg']>;
  passportNumber: FormControl<PatientFormRawValue['passportNumber']>;
  contactPersonName: FormControl<PatientFormRawValue['contactPersonName']>;
  contactPersonPhone: FormControl<PatientFormRawValue['contactPersonPhone']>;
  antecedents: FormControl<PatientFormRawValue['antecedents']>;
  allergies: FormControl<PatientFormRawValue['allergies']>;
  clinicalNotes: FormControl<PatientFormRawValue['clinicalNotes']>;
  smokingStatus: FormControl<PatientFormRawValue['smokingStatus']>;
  gdprConsentDate: FormControl<PatientFormRawValue['gdprConsentDate']>;
  status: FormControl<PatientFormRawValue['status']>;
  deceasedDate: FormControl<PatientFormRawValue['deceasedDate']>;
  insuranceCompanyName: FormControl<PatientFormRawValue['insuranceCompanyName']>;
  patientInsuranceId: FormControl<PatientFormRawValue['patientInsuranceId']>;
  insurancePolicyNumber: FormControl<PatientFormRawValue['insurancePolicyNumber']>;
  insuranceCoverageType: FormControl<PatientFormRawValue['insuranceCoverageType']>;
  insuranceValidFrom: FormControl<PatientFormRawValue['insuranceValidFrom']>;
  insuranceValidTo: FormControl<PatientFormRawValue['insuranceValidTo']>;
  user: FormControl<PatientFormRawValue['user']>;
};

export type PatientFormGroup = FormGroup<PatientFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PatientFormService {
  createPatientFormGroup(patient: PatientFormGroupInput = { id: null }): PatientFormGroup {
    const patientRawValue = this.convertPatientToPatientRawValue({
      ...this.getFormDefaults(),
      ...patient,
    });
    return new FormGroup<PatientFormGroupContent>({
      id: new FormControl(
        { value: patientRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      uid: new FormControl(patientRawValue.uid),
      firstName: new FormControl(patientRawValue.firstName, {
        validators: [Validators.required],
      }),
      lastName: new FormControl(patientRawValue.lastName, {
        validators: [Validators.required],
      }),
      birthDate: new FormControl(patientRawValue.birthDate, {
        validators: [Validators.required],
      }),
      gender: new FormControl(patientRawValue.gender),
      bloodType: new FormControl(patientRawValue.bloodType),
      address: new FormControl(patientRawValue.address),
      phone1: new FormControl(patientRawValue.phone1, {
        validators: [Validators.required, Validators.pattern('^\\+?[1-9]\\d{1,14}$')],
      }),
      phone2: new FormControl(patientRawValue.phone2, {
        validators: [Validators.pattern('^\\+?[1-9]\\d{1,14}$')],
      }),
      nif: new FormControl(patientRawValue.nif, {
        validators: [Validators.minLength(10), Validators.maxLength(10)],
      }),
      ninu: new FormControl(patientRawValue.ninu, {
        validators: [Validators.minLength(10), Validators.maxLength(10)],
      }),
      medicalRecordNumber: new FormControl(patientRawValue.medicalRecordNumber),
      heightCm: new FormControl(patientRawValue.heightCm, {
        validators: [Validators.min(0), Validators.max(300)],
      }),
      weightKg: new FormControl(patientRawValue.weightKg, {
        validators: [Validators.min(0), Validators.max(500)],
      }),
      passportNumber: new FormControl(patientRawValue.passportNumber, {
        validators: [Validators.minLength(3), Validators.maxLength(15)],
      }),
      contactPersonName: new FormControl(patientRawValue.contactPersonName, {
        validators: [Validators.maxLength(100)],
      }),
      contactPersonPhone: new FormControl(patientRawValue.contactPersonPhone, {
        validators: [Validators.pattern('^\\+?[1-9]\\d{1,14}$')],
      }),
      antecedents: new FormControl(patientRawValue.antecedents),
      allergies: new FormControl(patientRawValue.allergies),
      clinicalNotes: new FormControl(patientRawValue.clinicalNotes),
      smokingStatus: new FormControl(patientRawValue.smokingStatus),
      gdprConsentDate: new FormControl(patientRawValue.gdprConsentDate),
      status: new FormControl(patientRawValue.status),
      deceasedDate: new FormControl(patientRawValue.deceasedDate),
      insuranceCompanyName: new FormControl(patientRawValue.insuranceCompanyName, {
        validators: [Validators.maxLength(200)],
      }),
      patientInsuranceId: new FormControl(patientRawValue.patientInsuranceId, {
        validators: [Validators.maxLength(100)],
      }),
      insurancePolicyNumber: new FormControl(patientRawValue.insurancePolicyNumber, {
        validators: [Validators.maxLength(100)],
      }),
      insuranceCoverageType: new FormControl(patientRawValue.insuranceCoverageType, {
        validators: [Validators.maxLength(100)],
      }),
      insuranceValidFrom: new FormControl(patientRawValue.insuranceValidFrom),
      insuranceValidTo: new FormControl(patientRawValue.insuranceValidTo),
      user: new FormControl(patientRawValue.user, {
        validators: [Validators.required],
      }),
    });
  }

  getPatient(form: PatientFormGroup): IPatient | NewPatient {
    return this.convertPatientRawValueToPatient(form.getRawValue() as PatientFormRawValue | NewPatientFormRawValue);
  }

  resetForm(form: PatientFormGroup, patient: PatientFormGroupInput): void {
    const patientRawValue = this.convertPatientToPatientRawValue({ ...this.getFormDefaults(), ...patient });
    form.reset(
      {
        ...patientRawValue,
        id: { value: patientRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PatientFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      gdprConsentDate: currentTime,
      deceasedDate: currentTime,
    };
  }

  private convertPatientRawValueToPatient(rawPatient: PatientFormRawValue | NewPatientFormRawValue): IPatient | NewPatient {
    return {
      ...rawPatient,
      gdprConsentDate: dayjs(rawPatient.gdprConsentDate, DATE_TIME_FORMAT),
      deceasedDate: dayjs(rawPatient.deceasedDate, DATE_TIME_FORMAT),
    };
  }

  private convertPatientToPatientRawValue(
    patient: IPatient | (Partial<NewPatient> & PatientFormDefaults),
  ): PatientFormRawValue | PartialWithRequiredKeyOf<NewPatientFormRawValue> {
    return {
      ...patient,
      gdprConsentDate: patient.gdprConsentDate ? patient.gdprConsentDate.format(DATE_TIME_FORMAT) : undefined,
      deceasedDate: patient.deceasedDate ? patient.deceasedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
