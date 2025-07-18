import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPatient } from 'app/entities/patient/patient.model';
import { PatientService } from 'app/entities/patient/service/patient.service';
import { IMedicalDocument } from '../medical-document.model';
import { MedicalDocumentService } from '../service/medical-document.service';
import { MedicalDocumentFormGroup, MedicalDocumentFormService } from './medical-document-form.service';

@Component({
  standalone: true,
  selector: 'jhi-medical-document-update',
  templateUrl: './medical-document-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MedicalDocumentUpdateComponent implements OnInit {
  isSaving = false;
  medicalDocument: IMedicalDocument | null = null;

  patientsSharedCollection: IPatient[] = [];

  protected medicalDocumentService = inject(MedicalDocumentService);
  protected medicalDocumentFormService = inject(MedicalDocumentFormService);
  protected patientService = inject(PatientService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MedicalDocumentFormGroup = this.medicalDocumentFormService.createMedicalDocumentFormGroup();

  comparePatient = (o1: IPatient | null, o2: IPatient | null): boolean => this.patientService.comparePatient(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medicalDocument }) => {
      this.medicalDocument = medicalDocument;
      if (medicalDocument) {
        this.updateForm(medicalDocument);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const medicalDocument = this.medicalDocumentFormService.getMedicalDocument(this.editForm);
    if (medicalDocument.id !== null) {
      this.subscribeToSaveResponse(this.medicalDocumentService.update(medicalDocument));
    } else {
      this.subscribeToSaveResponse(this.medicalDocumentService.create(medicalDocument));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedicalDocument>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(medicalDocument: IMedicalDocument): void {
    this.medicalDocument = medicalDocument;
    this.medicalDocumentFormService.resetForm(this.editForm, medicalDocument);

    this.patientsSharedCollection = this.patientService.addPatientToCollectionIfMissing<IPatient>(
      this.patientsSharedCollection,
      medicalDocument.patient,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.patientService
      .query()
      .pipe(map((res: HttpResponse<IPatient[]>) => res.body ?? []))
      .pipe(
        map((patients: IPatient[]) =>
          this.patientService.addPatientToCollectionIfMissing<IPatient>(patients, this.medicalDocument?.patient),
        ),
      )
      .subscribe((patients: IPatient[]) => (this.patientsSharedCollection = patients));
  }
}
