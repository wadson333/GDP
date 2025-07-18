import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMedication, NewMedication } from '../medication.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMedication for edit and NewMedicationFormGroupInput for create.
 */
type MedicationFormGroupInput = IMedication | PartialWithRequiredKeyOf<NewMedication>;

type MedicationFormDefaults = Pick<NewMedication, 'id'>;

type MedicationFormGroupContent = {
  id: FormControl<IMedication['id'] | NewMedication['id']>;
  name: FormControl<IMedication['name']>;
  description: FormControl<IMedication['description']>;
};

export type MedicationFormGroup = FormGroup<MedicationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MedicationFormService {
  createMedicationFormGroup(medication: MedicationFormGroupInput = { id: null }): MedicationFormGroup {
    const medicationRawValue = {
      ...this.getFormDefaults(),
      ...medication,
    };
    return new FormGroup<MedicationFormGroupContent>({
      id: new FormControl(
        { value: medicationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(medicationRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(medicationRawValue.description),
    });
  }

  getMedication(form: MedicationFormGroup): IMedication | NewMedication {
    return form.getRawValue() as IMedication | NewMedication;
  }

  resetForm(form: MedicationFormGroup, medication: MedicationFormGroupInput): void {
    const medicationRawValue = { ...this.getFormDefaults(), ...medication };
    form.reset(
      {
        ...medicationRawValue,
        id: { value: medicationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MedicationFormDefaults {
    return {
      id: null,
    };
  }
}
