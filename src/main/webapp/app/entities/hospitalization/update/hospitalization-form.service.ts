import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IHospitalization, NewHospitalization } from '../hospitalization.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHospitalization for edit and NewHospitalizationFormGroupInput for create.
 */
type HospitalizationFormGroupInput = IHospitalization | PartialWithRequiredKeyOf<NewHospitalization>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IHospitalization | NewHospitalization> = Omit<T, 'admissionDate' | 'dischargeDate'> & {
  admissionDate?: string | null;
  dischargeDate?: string | null;
};

type HospitalizationFormRawValue = FormValueOf<IHospitalization>;

type NewHospitalizationFormRawValue = FormValueOf<NewHospitalization>;

type HospitalizationFormDefaults = Pick<NewHospitalization, 'id' | 'admissionDate' | 'dischargeDate'>;

type HospitalizationFormGroupContent = {
  id: FormControl<HospitalizationFormRawValue['id'] | NewHospitalization['id']>;
  admissionDate: FormControl<HospitalizationFormRawValue['admissionDate']>;
  dischargeDate: FormControl<HospitalizationFormRawValue['dischargeDate']>;
  reason: FormControl<HospitalizationFormRawValue['reason']>;
  patient: FormControl<HospitalizationFormRawValue['patient']>;
  attendingDoctor: FormControl<HospitalizationFormRawValue['attendingDoctor']>;
};

export type HospitalizationFormGroup = FormGroup<HospitalizationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HospitalizationFormService {
  createHospitalizationFormGroup(hospitalization: HospitalizationFormGroupInput = { id: null }): HospitalizationFormGroup {
    const hospitalizationRawValue = this.convertHospitalizationToHospitalizationRawValue({
      ...this.getFormDefaults(),
      ...hospitalization,
    });
    return new FormGroup<HospitalizationFormGroupContent>({
      id: new FormControl(
        { value: hospitalizationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      admissionDate: new FormControl(hospitalizationRawValue.admissionDate, {
        validators: [Validators.required],
      }),
      dischargeDate: new FormControl(hospitalizationRawValue.dischargeDate),
      reason: new FormControl(hospitalizationRawValue.reason),
      patient: new FormControl(hospitalizationRawValue.patient, {
        validators: [Validators.required],
      }),
      attendingDoctor: new FormControl(hospitalizationRawValue.attendingDoctor),
    });
  }

  getHospitalization(form: HospitalizationFormGroup): IHospitalization | NewHospitalization {
    return this.convertHospitalizationRawValueToHospitalization(
      form.getRawValue() as HospitalizationFormRawValue | NewHospitalizationFormRawValue,
    );
  }

  resetForm(form: HospitalizationFormGroup, hospitalization: HospitalizationFormGroupInput): void {
    const hospitalizationRawValue = this.convertHospitalizationToHospitalizationRawValue({ ...this.getFormDefaults(), ...hospitalization });
    form.reset(
      {
        ...hospitalizationRawValue,
        id: { value: hospitalizationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): HospitalizationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      admissionDate: currentTime,
      dischargeDate: currentTime,
    };
  }

  private convertHospitalizationRawValueToHospitalization(
    rawHospitalization: HospitalizationFormRawValue | NewHospitalizationFormRawValue,
  ): IHospitalization | NewHospitalization {
    return {
      ...rawHospitalization,
      admissionDate: dayjs(rawHospitalization.admissionDate, DATE_TIME_FORMAT),
      dischargeDate: dayjs(rawHospitalization.dischargeDate, DATE_TIME_FORMAT),
    };
  }

  private convertHospitalizationToHospitalizationRawValue(
    hospitalization: IHospitalization | (Partial<NewHospitalization> & HospitalizationFormDefaults),
  ): HospitalizationFormRawValue | PartialWithRequiredKeyOf<NewHospitalizationFormRawValue> {
    return {
      ...hospitalization,
      admissionDate: hospitalization.admissionDate ? hospitalization.admissionDate.format(DATE_TIME_FORMAT) : undefined,
      dischargeDate: hospitalization.dischargeDate ? hospitalization.dischargeDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
