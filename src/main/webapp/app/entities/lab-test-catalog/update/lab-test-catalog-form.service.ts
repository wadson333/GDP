import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ILabTestCatalog, NewLabTestCatalog } from '../lab-test-catalog.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILabTestCatalog for edit and NewLabTestCatalogFormGroupInput for create.
 */
type LabTestCatalogFormGroupInput = ILabTestCatalog | PartialWithRequiredKeyOf<NewLabTestCatalog>;

type LabTestCatalogFormDefaults = Pick<NewLabTestCatalog, 'id'>;

type LabTestCatalogFormGroupContent = {
  id: FormControl<ILabTestCatalog['id'] | NewLabTestCatalog['id']>;
  name: FormControl<ILabTestCatalog['name']>;
  unit: FormControl<ILabTestCatalog['unit']>;
  referenceRangeLow: FormControl<ILabTestCatalog['referenceRangeLow']>;
  referenceRangeHigh: FormControl<ILabTestCatalog['referenceRangeHigh']>;
};

export type LabTestCatalogFormGroup = FormGroup<LabTestCatalogFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LabTestCatalogFormService {
  createLabTestCatalogFormGroup(labTestCatalog: LabTestCatalogFormGroupInput = { id: null }): LabTestCatalogFormGroup {
    const labTestCatalogRawValue = {
      ...this.getFormDefaults(),
      ...labTestCatalog,
    };
    return new FormGroup<LabTestCatalogFormGroupContent>({
      id: new FormControl(
        { value: labTestCatalogRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(labTestCatalogRawValue.name, {
        validators: [Validators.required],
      }),
      unit: new FormControl(labTestCatalogRawValue.unit, {
        validators: [Validators.required],
      }),
      referenceRangeLow: new FormControl(labTestCatalogRawValue.referenceRangeLow),
      referenceRangeHigh: new FormControl(labTestCatalogRawValue.referenceRangeHigh),
    });
  }

  getLabTestCatalog(form: LabTestCatalogFormGroup): ILabTestCatalog | NewLabTestCatalog {
    return form.getRawValue() as ILabTestCatalog | NewLabTestCatalog;
  }

  resetForm(form: LabTestCatalogFormGroup, labTestCatalog: LabTestCatalogFormGroupInput): void {
    const labTestCatalogRawValue = { ...this.getFormDefaults(), ...labTestCatalog };
    form.reset(
      {
        ...labTestCatalogRawValue,
        id: { value: labTestCatalogRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LabTestCatalogFormDefaults {
    return {
      id: null,
    };
  }
}
