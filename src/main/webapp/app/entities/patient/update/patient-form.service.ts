import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

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

type PatientFormDefaults = Pick<NewPatient, 'id'>;

type PatientFormGroupContent = {
  id: FormControl<IPatient['id'] | NewPatient['id']>;
  firstName: FormControl<IPatient['firstName']>;
  lastName: FormControl<IPatient['lastName']>;
  birthDate: FormControl<IPatient['birthDate']>;
  gender: FormControl<IPatient['gender']>;
  bloodType: FormControl<IPatient['bloodType']>;
  address: FormControl<IPatient['address']>;
  phone1: FormControl<IPatient['phone1']>;
  phone2: FormControl<IPatient['phone2']>;
  email: FormControl<IPatient['email']>;
  nif: FormControl<IPatient['nif']>;
  ninu: FormControl<IPatient['ninu']>;
  heightCm: FormControl<IPatient['heightCm']>;
  weightKg: FormControl<IPatient['weightKg']>;
  passportNumber: FormControl<IPatient['passportNumber']>;
  contactPersonName: FormControl<IPatient['contactPersonName']>;
  contactPersonPhone: FormControl<IPatient['contactPersonPhone']>;
  antecedents: FormControl<IPatient['antecedents']>;
  allergies: FormControl<IPatient['allergies']>;
  user: FormControl<IPatient['user']>;
};

export type PatientFormGroup = FormGroup<PatientFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PatientFormService {
  createPatientFormGroup(patient: PatientFormGroupInput = { id: null }): PatientFormGroup {
    const patientRawValue = {
      ...this.getFormDefaults(),
      ...patient,
    };
    return new FormGroup<PatientFormGroupContent>({
      id: new FormControl(
        { value: patientRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
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
        validators: [Validators.required],
      }),
      phone2: new FormControl(patientRawValue.phone2),
      email: new FormControl(patientRawValue.email),
      nif: new FormControl(patientRawValue.nif),
      ninu: new FormControl(patientRawValue.ninu),
      heightCm: new FormControl(patientRawValue.heightCm),
      weightKg: new FormControl(patientRawValue.weightKg),
      passportNumber: new FormControl(patientRawValue.passportNumber),
      contactPersonName: new FormControl(patientRawValue.contactPersonName),
      contactPersonPhone: new FormControl(patientRawValue.contactPersonPhone),
      antecedents: new FormControl(patientRawValue.antecedents),
      allergies: new FormControl(patientRawValue.allergies),
      user: new FormControl(patientRawValue.user, {
        validators: [Validators.required],
      }),
    });
  }

  getPatient(form: PatientFormGroup): IPatient | NewPatient {
    return form.getRawValue() as IPatient | NewPatient;
  }

  resetForm(form: PatientFormGroup, patient: PatientFormGroupInput): void {
    const patientRawValue = { ...this.getFormDefaults(), ...patient };
    form.reset(
      {
        ...patientRawValue,
        id: { value: patientRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PatientFormDefaults {
    return {
      id: null,
    };
  }
}
