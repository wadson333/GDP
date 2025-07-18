import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IConsultation, NewConsultation } from '../consultation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IConsultation for edit and NewConsultationFormGroupInput for create.
 */
type ConsultationFormGroupInput = IConsultation | PartialWithRequiredKeyOf<NewConsultation>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IConsultation | NewConsultation> = Omit<T, 'consultationDate'> & {
  consultationDate?: string | null;
};

type ConsultationFormRawValue = FormValueOf<IConsultation>;

type NewConsultationFormRawValue = FormValueOf<NewConsultation>;

type ConsultationFormDefaults = Pick<NewConsultation, 'id' | 'consultationDate'>;

type ConsultationFormGroupContent = {
  id: FormControl<ConsultationFormRawValue['id'] | NewConsultation['id']>;
  consultationDate: FormControl<ConsultationFormRawValue['consultationDate']>;
  symptoms: FormControl<ConsultationFormRawValue['symptoms']>;
  diagnosis: FormControl<ConsultationFormRawValue['diagnosis']>;
  prescription: FormControl<ConsultationFormRawValue['prescription']>;
  doctor: FormControl<ConsultationFormRawValue['doctor']>;
  patient: FormControl<ConsultationFormRawValue['patient']>;
};

export type ConsultationFormGroup = FormGroup<ConsultationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ConsultationFormService {
  createConsultationFormGroup(consultation: ConsultationFormGroupInput = { id: null }): ConsultationFormGroup {
    const consultationRawValue = this.convertConsultationToConsultationRawValue({
      ...this.getFormDefaults(),
      ...consultation,
    });
    return new FormGroup<ConsultationFormGroupContent>({
      id: new FormControl(
        { value: consultationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      consultationDate: new FormControl(consultationRawValue.consultationDate, {
        validators: [Validators.required],
      }),
      symptoms: new FormControl(consultationRawValue.symptoms),
      diagnosis: new FormControl(consultationRawValue.diagnosis),
      prescription: new FormControl(consultationRawValue.prescription),
      doctor: new FormControl(consultationRawValue.doctor, {
        validators: [Validators.required],
      }),
      patient: new FormControl(consultationRawValue.patient, {
        validators: [Validators.required],
      }),
    });
  }

  getConsultation(form: ConsultationFormGroup): IConsultation | NewConsultation {
    return this.convertConsultationRawValueToConsultation(form.getRawValue() as ConsultationFormRawValue | NewConsultationFormRawValue);
  }

  resetForm(form: ConsultationFormGroup, consultation: ConsultationFormGroupInput): void {
    const consultationRawValue = this.convertConsultationToConsultationRawValue({ ...this.getFormDefaults(), ...consultation });
    form.reset(
      {
        ...consultationRawValue,
        id: { value: consultationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ConsultationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      consultationDate: currentTime,
    };
  }

  private convertConsultationRawValueToConsultation(
    rawConsultation: ConsultationFormRawValue | NewConsultationFormRawValue,
  ): IConsultation | NewConsultation {
    return {
      ...rawConsultation,
      consultationDate: dayjs(rawConsultation.consultationDate, DATE_TIME_FORMAT),
    };
  }

  private convertConsultationToConsultationRawValue(
    consultation: IConsultation | (Partial<NewConsultation> & ConsultationFormDefaults),
  ): ConsultationFormRawValue | PartialWithRequiredKeyOf<NewConsultationFormRawValue> {
    return {
      ...consultation,
      consultationDate: consultation.consultationDate ? consultation.consultationDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
