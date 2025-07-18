import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPrescription, NewPrescription } from '../prescription.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPrescription for edit and NewPrescriptionFormGroupInput for create.
 */
type PrescriptionFormGroupInput = IPrescription | PartialWithRequiredKeyOf<NewPrescription>;

type PrescriptionFormDefaults = Pick<NewPrescription, 'id'>;

type PrescriptionFormGroupContent = {
  id: FormControl<IPrescription['id'] | NewPrescription['id']>;
  prescriptionDate: FormControl<IPrescription['prescriptionDate']>;
};

export type PrescriptionFormGroup = FormGroup<PrescriptionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PrescriptionFormService {
  createPrescriptionFormGroup(prescription: PrescriptionFormGroupInput = { id: null }): PrescriptionFormGroup {
    const prescriptionRawValue = {
      ...this.getFormDefaults(),
      ...prescription,
    };
    return new FormGroup<PrescriptionFormGroupContent>({
      id: new FormControl(
        { value: prescriptionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      prescriptionDate: new FormControl(prescriptionRawValue.prescriptionDate, {
        validators: [Validators.required],
      }),
    });
  }

  getPrescription(form: PrescriptionFormGroup): IPrescription | NewPrescription {
    return form.getRawValue() as IPrescription | NewPrescription;
  }

  resetForm(form: PrescriptionFormGroup, prescription: PrescriptionFormGroupInput): void {
    const prescriptionRawValue = { ...this.getFormDefaults(), ...prescription };
    form.reset(
      {
        ...prescriptionRawValue,
        id: { value: prescriptionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PrescriptionFormDefaults {
    return {
      id: null,
    };
  }
}
