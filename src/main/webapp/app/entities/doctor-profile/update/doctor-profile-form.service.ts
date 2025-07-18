import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDoctorProfile, NewDoctorProfile } from '../doctor-profile.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDoctorProfile for edit and NewDoctorProfileFormGroupInput for create.
 */
type DoctorProfileFormGroupInput = IDoctorProfile | PartialWithRequiredKeyOf<NewDoctorProfile>;

type DoctorProfileFormDefaults = Pick<NewDoctorProfile, 'id'>;

type DoctorProfileFormGroupContent = {
  id: FormControl<IDoctorProfile['id'] | NewDoctorProfile['id']>;
  specialty: FormControl<IDoctorProfile['specialty']>;
  licenseNumber: FormControl<IDoctorProfile['licenseNumber']>;
  university: FormControl<IDoctorProfile['university']>;
  startDateOfPractice: FormControl<IDoctorProfile['startDateOfPractice']>;
  user: FormControl<IDoctorProfile['user']>;
};

export type DoctorProfileFormGroup = FormGroup<DoctorProfileFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DoctorProfileFormService {
  createDoctorProfileFormGroup(doctorProfile: DoctorProfileFormGroupInput = { id: null }): DoctorProfileFormGroup {
    const doctorProfileRawValue = {
      ...this.getFormDefaults(),
      ...doctorProfile,
    };
    return new FormGroup<DoctorProfileFormGroupContent>({
      id: new FormControl(
        { value: doctorProfileRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      specialty: new FormControl(doctorProfileRawValue.specialty, {
        validators: [Validators.required],
      }),
      licenseNumber: new FormControl(doctorProfileRawValue.licenseNumber, {
        validators: [Validators.required],
      }),
      university: new FormControl(doctorProfileRawValue.university),
      startDateOfPractice: new FormControl(doctorProfileRawValue.startDateOfPractice, {
        validators: [Validators.required],
      }),
      user: new FormControl(doctorProfileRawValue.user, {
        validators: [Validators.required],
      }),
    });
  }

  getDoctorProfile(form: DoctorProfileFormGroup): IDoctorProfile | NewDoctorProfile {
    return form.getRawValue() as IDoctorProfile | NewDoctorProfile;
  }

  resetForm(form: DoctorProfileFormGroup, doctorProfile: DoctorProfileFormGroupInput): void {
    const doctorProfileRawValue = { ...this.getFormDefaults(), ...doctorProfile };
    form.reset(
      {
        ...doctorProfileRawValue,
        id: { value: doctorProfileRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DoctorProfileFormDefaults {
    return {
      id: null,
    };
  }
}
