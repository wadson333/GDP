import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ILabTestResult, NewLabTestResult } from '../lab-test-result.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILabTestResult for edit and NewLabTestResultFormGroupInput for create.
 */
type LabTestResultFormGroupInput = ILabTestResult | PartialWithRequiredKeyOf<NewLabTestResult>;

type LabTestResultFormDefaults = Pick<NewLabTestResult, 'id' | 'isAbnormal'>;

type LabTestResultFormGroupContent = {
  id: FormControl<ILabTestResult['id'] | NewLabTestResult['id']>;
  resultValue: FormControl<ILabTestResult['resultValue']>;
  resultDate: FormControl<ILabTestResult['resultDate']>;
  isAbnormal: FormControl<ILabTestResult['isAbnormal']>;
  patient: FormControl<ILabTestResult['patient']>;
  labTest: FormControl<ILabTestResult['labTest']>;
  consultation: FormControl<ILabTestResult['consultation']>;
};

export type LabTestResultFormGroup = FormGroup<LabTestResultFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LabTestResultFormService {
  createLabTestResultFormGroup(labTestResult: LabTestResultFormGroupInput = { id: null }): LabTestResultFormGroup {
    const labTestResultRawValue = {
      ...this.getFormDefaults(),
      ...labTestResult,
    };
    return new FormGroup<LabTestResultFormGroupContent>({
      id: new FormControl(
        { value: labTestResultRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      resultValue: new FormControl(labTestResultRawValue.resultValue, {
        validators: [Validators.required],
      }),
      resultDate: new FormControl(labTestResultRawValue.resultDate, {
        validators: [Validators.required],
      }),
      isAbnormal: new FormControl(labTestResultRawValue.isAbnormal, {
        validators: [Validators.required],
      }),
      patient: new FormControl(labTestResultRawValue.patient, {
        validators: [Validators.required],
      }),
      labTest: new FormControl(labTestResultRawValue.labTest, {
        validators: [Validators.required],
      }),
      consultation: new FormControl(labTestResultRawValue.consultation),
    });
  }

  getLabTestResult(form: LabTestResultFormGroup): ILabTestResult | NewLabTestResult {
    return form.getRawValue() as ILabTestResult | NewLabTestResult;
  }

  resetForm(form: LabTestResultFormGroup, labTestResult: LabTestResultFormGroupInput): void {
    const labTestResultRawValue = { ...this.getFormDefaults(), ...labTestResult };
    form.reset(
      {
        ...labTestResultRawValue,
        id: { value: labTestResultRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LabTestResultFormDefaults {
    return {
      id: null,
      isAbnormal: false,
    };
  }
}
