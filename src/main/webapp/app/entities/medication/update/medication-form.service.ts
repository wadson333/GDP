import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
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

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMedication | NewMedication> = Omit<T, 'marketingAuthorizationDate' | 'expiryDate'> & {
  marketingAuthorizationDate?: string | null;
  expiryDate?: string | null;
};

type MedicationFormRawValue = FormValueOf<IMedication>;

type NewMedicationFormRawValue = FormValueOf<NewMedication>;

type MedicationFormDefaults = Pick<NewMedication, 'id' | 'marketingAuthorizationDate' | 'expiryDate' | 'active' | 'isGeneric'>;

type MedicationFormGroupContent = {
  id: FormControl<MedicationFormRawValue['id'] | NewMedication['id']>;
  name: FormControl<MedicationFormRawValue['name']>;
  internationalName: FormControl<MedicationFormRawValue['internationalName']>;
  codeAtc: FormControl<MedicationFormRawValue['codeAtc']>;
  formulation: FormControl<MedicationFormRawValue['formulation']>;
  strength: FormControl<MedicationFormRawValue['strength']>;
  routeOfAdministration: FormControl<MedicationFormRawValue['routeOfAdministration']>;
  manufacturer: FormControl<MedicationFormRawValue['manufacturer']>;
  marketingAuthorizationNumber: FormControl<MedicationFormRawValue['marketingAuthorizationNumber']>;
  marketingAuthorizationDate: FormControl<MedicationFormRawValue['marketingAuthorizationDate']>;
  packaging: FormControl<MedicationFormRawValue['packaging']>;
  prescriptionStatus: FormControl<MedicationFormRawValue['prescriptionStatus']>;
  description: FormControl<MedicationFormRawValue['description']>;
  expiryDate: FormControl<MedicationFormRawValue['expiryDate']>;
  barcode: FormControl<MedicationFormRawValue['barcode']>;
  storageCondition: FormControl<MedicationFormRawValue['storageCondition']>;
  unitPrice: FormControl<MedicationFormRawValue['unitPrice']>;
  image: FormControl<MedicationFormRawValue['image']>;
  composition: FormControl<MedicationFormRawValue['composition']>;
  contraindications: FormControl<MedicationFormRawValue['contraindications']>;
  sideEffects: FormControl<MedicationFormRawValue['sideEffects']>;
  active: FormControl<MedicationFormRawValue['active']>;
  isGeneric: FormControl<MedicationFormRawValue['isGeneric']>;
  riskLevel: FormControl<MedicationFormRawValue['riskLevel']>;
};

export type MedicationFormGroup = FormGroup<MedicationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MedicationFormService {
  createMedicationFormGroup(medication: MedicationFormGroupInput = { id: null }): MedicationFormGroup {
    const medicationRawValue = this.convertMedicationToMedicationRawValue({
      ...this.getFormDefaults(),
      ...medication,
    });
    return new FormGroup<MedicationFormGroupContent>({
      id: new FormControl(
        { value: medicationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(medicationRawValue.name, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(100)],
      }),
      internationalName: new FormControl(medicationRawValue.internationalName, {
        validators: [Validators.maxLength(100)],
      }),
      codeAtc: new FormControl(medicationRawValue.codeAtc, {
        validators: [Validators.maxLength(20)],
      }),
      formulation: new FormControl(medicationRawValue.formulation, {
        validators: [Validators.maxLength(50)],
      }),
      strength: new FormControl(medicationRawValue.strength, {
        validators: [Validators.maxLength(50)],
      }),
      routeOfAdministration: new FormControl(medicationRawValue.routeOfAdministration, {
        validators: [Validators.required],
      }),
      manufacturer: new FormControl(medicationRawValue.manufacturer, {
        validators: [Validators.maxLength(100)],
      }),
      marketingAuthorizationNumber: new FormControl(medicationRawValue.marketingAuthorizationNumber, {
        validators: [Validators.maxLength(30)],
      }),
      marketingAuthorizationDate: new FormControl(medicationRawValue.marketingAuthorizationDate),
      packaging: new FormControl(medicationRawValue.packaging, {
        validators: [Validators.maxLength(150)],
      }),
      prescriptionStatus: new FormControl(medicationRawValue.prescriptionStatus, {
        validators: [Validators.required],
      }),
      description: new FormControl(medicationRawValue.description),
      expiryDate: new FormControl(medicationRawValue.expiryDate),
      barcode: new FormControl(medicationRawValue.barcode, {
        validators: [Validators.maxLength(50)],
      }),
      storageCondition: new FormControl(medicationRawValue.storageCondition, {
        validators: [Validators.maxLength(100)],
      }),
      unitPrice: new FormControl(medicationRawValue.unitPrice),
      image: new FormControl(medicationRawValue.image),
      composition: new FormControl(medicationRawValue.composition),
      contraindications: new FormControl(medicationRawValue.contraindications),
      sideEffects: new FormControl(medicationRawValue.sideEffects),
      active: new FormControl(medicationRawValue.active, {
        validators: [Validators.required],
      }),
      isGeneric: new FormControl(medicationRawValue.isGeneric),
      riskLevel: new FormControl(medicationRawValue.riskLevel),
    });
  }

  getMedication(form: MedicationFormGroup): IMedication | NewMedication {
    return this.convertMedicationRawValueToMedication(form.getRawValue() as MedicationFormRawValue | NewMedicationFormRawValue);
  }

  resetForm(form: MedicationFormGroup, medication: MedicationFormGroupInput): void {
    const medicationRawValue = this.convertMedicationToMedicationRawValue({ ...this.getFormDefaults(), ...medication });
    form.reset(
      {
        ...medicationRawValue,
        id: { value: medicationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MedicationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      marketingAuthorizationDate: currentTime,
      expiryDate: currentTime,
      active: false,
      isGeneric: false,
    };
  }

  private convertMedicationRawValueToMedication(
    rawMedication: MedicationFormRawValue | NewMedicationFormRawValue,
  ): IMedication | NewMedication {
    return {
      ...rawMedication,
      marketingAuthorizationDate: dayjs(rawMedication.marketingAuthorizationDate, DATE_TIME_FORMAT),
      expiryDate: dayjs(rawMedication.expiryDate, DATE_TIME_FORMAT),
    };
  }

  private convertMedicationToMedicationRawValue(
    medication: IMedication | (Partial<NewMedication> & MedicationFormDefaults),
  ): MedicationFormRawValue | PartialWithRequiredKeyOf<NewMedicationFormRawValue> {
    return {
      ...medication,
      marketingAuthorizationDate: medication.marketingAuthorizationDate
        ? medication.marketingAuthorizationDate.format(DATE_TIME_FORMAT)
        : undefined,
      expiryDate: medication.expiryDate ? medication.expiryDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
