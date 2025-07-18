import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMedicalDocument, NewMedicalDocument } from '../medical-document.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMedicalDocument for edit and NewMedicalDocumentFormGroupInput for create.
 */
type MedicalDocumentFormGroupInput = IMedicalDocument | PartialWithRequiredKeyOf<NewMedicalDocument>;

type MedicalDocumentFormDefaults = Pick<NewMedicalDocument, 'id'>;

type MedicalDocumentFormGroupContent = {
  id: FormControl<IMedicalDocument['id'] | NewMedicalDocument['id']>;
  documentName: FormControl<IMedicalDocument['documentName']>;
  documentDate: FormControl<IMedicalDocument['documentDate']>;
  filePath: FormControl<IMedicalDocument['filePath']>;
  fileType: FormControl<IMedicalDocument['fileType']>;
  desc: FormControl<IMedicalDocument['desc']>;
  patient: FormControl<IMedicalDocument['patient']>;
};

export type MedicalDocumentFormGroup = FormGroup<MedicalDocumentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MedicalDocumentFormService {
  createMedicalDocumentFormGroup(medicalDocument: MedicalDocumentFormGroupInput = { id: null }): MedicalDocumentFormGroup {
    const medicalDocumentRawValue = {
      ...this.getFormDefaults(),
      ...medicalDocument,
    };
    return new FormGroup<MedicalDocumentFormGroupContent>({
      id: new FormControl(
        { value: medicalDocumentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentName: new FormControl(medicalDocumentRawValue.documentName, {
        validators: [Validators.required],
      }),
      documentDate: new FormControl(medicalDocumentRawValue.documentDate),
      filePath: new FormControl(medicalDocumentRawValue.filePath, {
        validators: [Validators.required],
      }),
      fileType: new FormControl(medicalDocumentRawValue.fileType, {
        validators: [Validators.required],
      }),
      desc: new FormControl(medicalDocumentRawValue.desc),
      patient: new FormControl(medicalDocumentRawValue.patient, {
        validators: [Validators.required],
      }),
    });
  }

  getMedicalDocument(form: MedicalDocumentFormGroup): IMedicalDocument | NewMedicalDocument {
    return form.getRawValue() as IMedicalDocument | NewMedicalDocument;
  }

  resetForm(form: MedicalDocumentFormGroup, medicalDocument: MedicalDocumentFormGroupInput): void {
    const medicalDocumentRawValue = { ...this.getFormDefaults(), ...medicalDocument };
    form.reset(
      {
        ...medicalDocumentRawValue,
        id: { value: medicalDocumentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MedicalDocumentFormDefaults {
    return {
      id: null,
    };
  }
}
