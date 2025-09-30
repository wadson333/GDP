import { Injectable } from '@angular/core';
import { FormControl, FormGroup, MinLengthValidator, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
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

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ILabTestCatalog | NewLabTestCatalog> = Omit<T, 'validFrom' | 'validTo'> & {
  validFrom?: string | null;
  validTo?: string | null;
};

type LabTestCatalogFormRawValue = FormValueOf<ILabTestCatalog>;

type NewLabTestCatalogFormRawValue = FormValueOf<NewLabTestCatalog>;

type LabTestCatalogFormDefaults = Pick<NewLabTestCatalog, 'id' | 'validFrom' | 'validTo' | 'active'>;

type LabTestCatalogFormGroupContent = {
  id: FormControl<LabTestCatalogFormRawValue['id'] | NewLabTestCatalog['id']>;
  name: FormControl<LabTestCatalogFormRawValue['name']>;
  unit: FormControl<LabTestCatalogFormRawValue['unit']>;
  description: FormControl<LabTestCatalogFormRawValue['description']>;
  version: FormControl<LabTestCatalogFormRawValue['version']>;
  validFrom: FormControl<LabTestCatalogFormRawValue['validFrom']>;
  validTo: FormControl<LabTestCatalogFormRawValue['validTo']>;
  method: FormControl<LabTestCatalogFormRawValue['method']>;
  sampleType: FormControl<LabTestCatalogFormRawValue['sampleType']>;
  referenceRangeLow: FormControl<LabTestCatalogFormRawValue['referenceRangeLow']>;
  referenceRangeHigh: FormControl<LabTestCatalogFormRawValue['referenceRangeHigh']>;
  active: FormControl<LabTestCatalogFormRawValue['active']>;
  type: FormControl<LabTestCatalogFormRawValue['type']>;
  loincCode: FormControl<LabTestCatalogFormRawValue['loincCode']>;
  cost: FormControl<LabTestCatalogFormRawValue['cost']>;
  turnaroundTime: FormControl<LabTestCatalogFormRawValue['turnaroundTime']>;
};

export type LabTestCatalogFormGroup = FormGroup<LabTestCatalogFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LabTestCatalogFormService {
  createLabTestCatalogFormGroup(labTestCatalog: LabTestCatalogFormGroupInput = { id: null }): LabTestCatalogFormGroup {
    const labTestCatalogRawValue = this.convertLabTestCatalogToLabTestCatalogRawValue({
      ...this.getFormDefaults(),
      ...labTestCatalog,
    });
    const loincCodePattern = /^\d{1,5}-\d{1,2}$/;

    return new FormGroup<LabTestCatalogFormGroupContent>({
      id: new FormControl(
        { value: labTestCatalogRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(labTestCatalogRawValue.name, {
        validators: [Validators.required, Validators.minLength(3)],
      }),
      unit: new FormControl(labTestCatalogRawValue.unit, {
        validators: [Validators.required, Validators.minLength(2)],
      }),
      description: new FormControl(labTestCatalogRawValue.description, {
        validators: [Validators.minLength(10)],
      }),
      version: new FormControl(labTestCatalogRawValue.version, {
        validators: [Validators.min(1)],
      }),
      validFrom: new FormControl(labTestCatalogRawValue.validFrom),
      validTo: new FormControl(labTestCatalogRawValue.validTo),
      method: new FormControl(labTestCatalogRawValue.method),
      sampleType: new FormControl(labTestCatalogRawValue.sampleType),
      referenceRangeLow: new FormControl(labTestCatalogRawValue.referenceRangeLow),
      referenceRangeHigh: new FormControl(labTestCatalogRawValue.referenceRangeHigh),
      active: new FormControl(labTestCatalogRawValue.active),
      type: new FormControl(labTestCatalogRawValue.type),
      loincCode: new FormControl(labTestCatalogRawValue.loincCode, {
        validators: [Validators.pattern(loincCodePattern)],
      }),
      cost: new FormControl(labTestCatalogRawValue.cost, { validators: [Validators.min(1)] }),
      turnaroundTime: new FormControl(labTestCatalogRawValue.turnaroundTime, { validators: [Validators.min(30)] }),
    });
  }

  getLabTestCatalog(form: LabTestCatalogFormGroup): ILabTestCatalog | NewLabTestCatalog {
    return this.convertLabTestCatalogRawValueToLabTestCatalog(
      form.getRawValue() as LabTestCatalogFormRawValue | NewLabTestCatalogFormRawValue,
    );
  }

  resetForm(form: LabTestCatalogFormGroup, labTestCatalog: LabTestCatalogFormGroupInput): void {
    const labTestCatalogRawValue = this.convertLabTestCatalogToLabTestCatalogRawValue({ ...this.getFormDefaults(), ...labTestCatalog });
    form.reset(
      {
        ...labTestCatalogRawValue,
        id: { value: labTestCatalogRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LabTestCatalogFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      validFrom: currentTime,
      validTo: currentTime,
      active: false,
    };
  }

  private convertLabTestCatalogRawValueToLabTestCatalog(
    rawLabTestCatalog: LabTestCatalogFormRawValue | NewLabTestCatalogFormRawValue,
  ): ILabTestCatalog | NewLabTestCatalog {
    return {
      ...rawLabTestCatalog,
      validFrom: dayjs(rawLabTestCatalog.validFrom, DATE_TIME_FORMAT),
      validTo: dayjs(rawLabTestCatalog.validTo, DATE_TIME_FORMAT),
    };
  }

  private convertLabTestCatalogToLabTestCatalogRawValue(
    labTestCatalog: ILabTestCatalog | (Partial<NewLabTestCatalog> & LabTestCatalogFormDefaults),
  ): LabTestCatalogFormRawValue | PartialWithRequiredKeyOf<NewLabTestCatalogFormRawValue> {
    return {
      ...labTestCatalog,
      validFrom: labTestCatalog.validFrom ? labTestCatalog.validFrom.format(DATE_TIME_FORMAT) : undefined,
      validTo: labTestCatalog.validTo ? labTestCatalog.validTo.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
