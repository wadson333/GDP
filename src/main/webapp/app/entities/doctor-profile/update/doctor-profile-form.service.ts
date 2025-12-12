import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
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

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDoctorProfile | NewDoctorProfile> = Omit<T, 'verifiedAt'> & {
  verifiedAt?: string | null;
};

type DoctorProfileFormRawValue = FormValueOf<IDoctorProfile>;

type NewDoctorProfileFormRawValue = FormValueOf<NewDoctorProfile>;

type DoctorProfileFormDefaults = Pick<
  NewDoctorProfile,
  'id' | 'acceptingNewPatients' | 'allowsTeleconsultation' | 'isVerified' | 'verifiedAt'
>;

type DoctorProfileFormGroupContent = {
  id: FormControl<DoctorProfileFormRawValue['id'] | NewDoctorProfile['id']>;
  codeClinic: FormControl<DoctorProfileFormRawValue['codeClinic']>;
  uid: FormControl<DoctorProfileFormRawValue['uid']>;
  medicalLicenseNumber: FormControl<DoctorProfileFormRawValue['medicalLicenseNumber']>;
  firstName: FormControl<DoctorProfileFormRawValue['firstName']>;
  lastName: FormControl<DoctorProfileFormRawValue['lastName']>;
  birthDate: FormControl<DoctorProfileFormRawValue['birthDate']>;
  gender: FormControl<DoctorProfileFormRawValue['gender']>;
  bloodType: FormControl<DoctorProfileFormRawValue['bloodType']>;
  primarySpecialty: FormControl<DoctorProfileFormRawValue['primarySpecialty']>;
  otherSpecialties: FormControl<DoctorProfileFormRawValue['otherSpecialties']>;
  university: FormControl<DoctorProfileFormRawValue['university']>;
  graduationYear: FormControl<DoctorProfileFormRawValue['graduationYear']>;
  startDateOfPractice: FormControl<DoctorProfileFormRawValue['startDateOfPractice']>;
  consultationDurationMinutes: FormControl<DoctorProfileFormRawValue['consultationDurationMinutes']>;
  acceptingNewPatients: FormControl<DoctorProfileFormRawValue['acceptingNewPatients']>;
  allowsTeleconsultation: FormControl<DoctorProfileFormRawValue['allowsTeleconsultation']>;
  consultationFee: FormControl<DoctorProfileFormRawValue['consultationFee']>;
  teleconsultationFee: FormControl<DoctorProfileFormRawValue['teleconsultationFee']>;
  bio: FormControl<DoctorProfileFormRawValue['bio']>;
  spokenLanguages: FormControl<DoctorProfileFormRawValue['spokenLanguages']>;
  websiteUrl: FormControl<DoctorProfileFormRawValue['websiteUrl']>;
  officePhone: FormControl<DoctorProfileFormRawValue['officePhone']>;
  officeAddress: FormControl<DoctorProfileFormRawValue['officeAddress']>;
  status: FormControl<DoctorProfileFormRawValue['status']>;
  isVerified: FormControl<DoctorProfileFormRawValue['isVerified']>;
  verifiedAt: FormControl<DoctorProfileFormRawValue['verifiedAt']>;
  nif: FormControl<DoctorProfileFormRawValue['nif']>;
  ninu: FormControl<DoctorProfileFormRawValue['ninu']>;
  averageRating: FormControl<DoctorProfileFormRawValue['averageRating']>;
  reviewCount: FormControl<DoctorProfileFormRawValue['reviewCount']>;
  version: FormControl<DoctorProfileFormRawValue['version']>;
  user: FormControl<DoctorProfileFormRawValue['user']>;
};

export type DoctorProfileFormGroup = FormGroup<DoctorProfileFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DoctorProfileFormService {
  createDoctorProfileFormGroup(doctorProfile: DoctorProfileFormGroupInput = { id: null }): DoctorProfileFormGroup {
    const doctorProfileRawValue = this.convertDoctorProfileToDoctorProfileRawValue({
      ...this.getFormDefaults(),
      ...doctorProfile,
    });
    return new FormGroup<DoctorProfileFormGroupContent>({
      id: new FormControl(
        { value: doctorProfileRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      codeClinic: new FormControl(doctorProfileRawValue.codeClinic, {
        validators: [Validators.maxLength(10)],
      }),
      uid: new FormControl(doctorProfileRawValue.uid, {
        validators: [Validators.required],
      }),
      medicalLicenseNumber: new FormControl(doctorProfileRawValue.medicalLicenseNumber, {
        validators: [Validators.required, Validators.minLength(5), Validators.maxLength(50)],
      }),
      firstName: new FormControl(doctorProfileRawValue.firstName, {
        validators: [Validators.required],
      }),
      lastName: new FormControl(doctorProfileRawValue.lastName, {
        validators: [Validators.required],
      }),
      birthDate: new FormControl(doctorProfileRawValue.birthDate, {
        validators: [Validators.required],
      }),
      gender: new FormControl(doctorProfileRawValue.gender),
      bloodType: new FormControl(doctorProfileRawValue.bloodType),
      primarySpecialty: new FormControl(doctorProfileRawValue.primarySpecialty, {
        validators: [Validators.required],
      }),
      otherSpecialties: new FormControl(doctorProfileRawValue.otherSpecialties),
      university: new FormControl(doctorProfileRawValue.university, {
        validators: [Validators.maxLength(100)],
      }),
      graduationYear: new FormControl(doctorProfileRawValue.graduationYear, {
        validators: [Validators.min(1950), Validators.max(2100)],
      }),
      startDateOfPractice: new FormControl(doctorProfileRawValue.startDateOfPractice, {
        validators: [Validators.required],
      }),
      consultationDurationMinutes: new FormControl(doctorProfileRawValue.consultationDurationMinutes, {
        validators: [Validators.required, Validators.min(5), Validators.max(120)],
      }),
      acceptingNewPatients: new FormControl(doctorProfileRawValue.acceptingNewPatients, {
        validators: [Validators.required],
      }),
      allowsTeleconsultation: new FormControl(doctorProfileRawValue.allowsTeleconsultation, {
        validators: [Validators.required],
      }),
      consultationFee: new FormControl(doctorProfileRawValue.consultationFee, {
        validators: [Validators.min(0)],
      }),
      teleconsultationFee: new FormControl(doctorProfileRawValue.teleconsultationFee, {
        validators: [Validators.min(0)],
      }),
      bio: new FormControl(doctorProfileRawValue.bio),
      spokenLanguages: new FormControl(doctorProfileRawValue.spokenLanguages, {
        validators: [Validators.maxLength(255)],
      }),
      websiteUrl: new FormControl(doctorProfileRawValue.websiteUrl, {
        validators: [Validators.maxLength(255)],
      }),
      officePhone: new FormControl(doctorProfileRawValue.officePhone, {
        validators: [Validators.pattern('^\\+?[1-9]\\d{1,14}$')],
      }),
      officeAddress: new FormControl(doctorProfileRawValue.officeAddress),
      status: new FormControl(doctorProfileRawValue.status, {
        validators: [Validators.required],
      }),
      isVerified: new FormControl(doctorProfileRawValue.isVerified),
      verifiedAt: new FormControl(doctorProfileRawValue.verifiedAt),
      nif: new FormControl(doctorProfileRawValue.nif, {
        validators: [Validators.minLength(10), Validators.maxLength(10)],
      }),
      ninu: new FormControl(doctorProfileRawValue.ninu, {
        validators: [Validators.minLength(10), Validators.maxLength(10)],
      }),
      averageRating: new FormControl(doctorProfileRawValue.averageRating, {
        validators: [Validators.min(0), Validators.max(5)],
      }),
      reviewCount: new FormControl(doctorProfileRawValue.reviewCount, {
        validators: [Validators.min(0)],
      }),
      version: new FormControl(doctorProfileRawValue.version),
      user: new FormControl(doctorProfileRawValue.user, {
        validators: [Validators.required],
      }),
    });
  }

  getDoctorProfile(form: DoctorProfileFormGroup): IDoctorProfile | NewDoctorProfile {
    return this.convertDoctorProfileRawValueToDoctorProfile(form.getRawValue() as DoctorProfileFormRawValue | NewDoctorProfileFormRawValue);
  }

  resetForm(form: DoctorProfileFormGroup, doctorProfile: DoctorProfileFormGroupInput): void {
    const doctorProfileRawValue = this.convertDoctorProfileToDoctorProfileRawValue({ ...this.getFormDefaults(), ...doctorProfile });
    form.reset(
      {
        ...doctorProfileRawValue,
        id: { value: doctorProfileRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DoctorProfileFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      acceptingNewPatients: false,
      allowsTeleconsultation: false,
      isVerified: false,
      verifiedAt: currentTime,
    };
  }

  private convertDoctorProfileRawValueToDoctorProfile(
    rawDoctorProfile: DoctorProfileFormRawValue | NewDoctorProfileFormRawValue,
  ): IDoctorProfile | NewDoctorProfile {
    return {
      ...rawDoctorProfile,
      verifiedAt: dayjs(rawDoctorProfile.verifiedAt, DATE_TIME_FORMAT),
    };
  }

  private convertDoctorProfileToDoctorProfileRawValue(
    doctorProfile: IDoctorProfile | (Partial<NewDoctorProfile> & DoctorProfileFormDefaults),
  ): DoctorProfileFormRawValue | PartialWithRequiredKeyOf<NewDoctorProfileFormRawValue> {
    return {
      ...doctorProfile,
      verifiedAt: doctorProfile.verifiedAt ? doctorProfile.verifiedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
