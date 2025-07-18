import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPrescriptionItem, NewPrescriptionItem } from '../prescription-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPrescriptionItem for edit and NewPrescriptionItemFormGroupInput for create.
 */
type PrescriptionItemFormGroupInput = IPrescriptionItem | PartialWithRequiredKeyOf<NewPrescriptionItem>;

type PrescriptionItemFormDefaults = Pick<NewPrescriptionItem, 'id'>;

type PrescriptionItemFormGroupContent = {
  id: FormControl<IPrescriptionItem['id'] | NewPrescriptionItem['id']>;
  dosage: FormControl<IPrescriptionItem['dosage']>;
  frequency: FormControl<IPrescriptionItem['frequency']>;
  duration: FormControl<IPrescriptionItem['duration']>;
  medication: FormControl<IPrescriptionItem['medication']>;
  prescription: FormControl<IPrescriptionItem['prescription']>;
};

export type PrescriptionItemFormGroup = FormGroup<PrescriptionItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PrescriptionItemFormService {
  createPrescriptionItemFormGroup(prescriptionItem: PrescriptionItemFormGroupInput = { id: null }): PrescriptionItemFormGroup {
    const prescriptionItemRawValue = {
      ...this.getFormDefaults(),
      ...prescriptionItem,
    };
    return new FormGroup<PrescriptionItemFormGroupContent>({
      id: new FormControl(
        { value: prescriptionItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dosage: new FormControl(prescriptionItemRawValue.dosage),
      frequency: new FormControl(prescriptionItemRawValue.frequency, {
        validators: [Validators.required],
      }),
      duration: new FormControl(prescriptionItemRawValue.duration),
      medication: new FormControl(prescriptionItemRawValue.medication, {
        validators: [Validators.required],
      }),
      prescription: new FormControl(prescriptionItemRawValue.prescription, {
        validators: [Validators.required],
      }),
    });
  }

  getPrescriptionItem(form: PrescriptionItemFormGroup): IPrescriptionItem | NewPrescriptionItem {
    return form.getRawValue() as IPrescriptionItem | NewPrescriptionItem;
  }

  resetForm(form: PrescriptionItemFormGroup, prescriptionItem: PrescriptionItemFormGroupInput): void {
    const prescriptionItemRawValue = { ...this.getFormDefaults(), ...prescriptionItem };
    form.reset(
      {
        ...prescriptionItemRawValue,
        id: { value: prescriptionItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PrescriptionItemFormDefaults {
    return {
      id: null,
    };
  }
}
