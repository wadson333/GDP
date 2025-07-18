import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPatient } from 'app/entities/patient/patient.model';
import { PatientService } from 'app/entities/patient/service/patient.service';
import { ILabTestCatalog } from 'app/entities/lab-test-catalog/lab-test-catalog.model';
import { LabTestCatalogService } from 'app/entities/lab-test-catalog/service/lab-test-catalog.service';
import { IConsultation } from 'app/entities/consultation/consultation.model';
import { ConsultationService } from 'app/entities/consultation/service/consultation.service';
import { LabTestResultService } from '../service/lab-test-result.service';
import { ILabTestResult } from '../lab-test-result.model';
import { LabTestResultFormGroup, LabTestResultFormService } from './lab-test-result-form.service';

@Component({
  standalone: true,
  selector: 'jhi-lab-test-result-update',
  templateUrl: './lab-test-result-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LabTestResultUpdateComponent implements OnInit {
  isSaving = false;
  labTestResult: ILabTestResult | null = null;

  patientsSharedCollection: IPatient[] = [];
  labTestCatalogsSharedCollection: ILabTestCatalog[] = [];
  consultationsSharedCollection: IConsultation[] = [];

  protected labTestResultService = inject(LabTestResultService);
  protected labTestResultFormService = inject(LabTestResultFormService);
  protected patientService = inject(PatientService);
  protected labTestCatalogService = inject(LabTestCatalogService);
  protected consultationService = inject(ConsultationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LabTestResultFormGroup = this.labTestResultFormService.createLabTestResultFormGroup();

  comparePatient = (o1: IPatient | null, o2: IPatient | null): boolean => this.patientService.comparePatient(o1, o2);

  compareLabTestCatalog = (o1: ILabTestCatalog | null, o2: ILabTestCatalog | null): boolean =>
    this.labTestCatalogService.compareLabTestCatalog(o1, o2);

  compareConsultation = (o1: IConsultation | null, o2: IConsultation | null): boolean =>
    this.consultationService.compareConsultation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ labTestResult }) => {
      this.labTestResult = labTestResult;
      if (labTestResult) {
        this.updateForm(labTestResult);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const labTestResult = this.labTestResultFormService.getLabTestResult(this.editForm);
    if (labTestResult.id !== null) {
      this.subscribeToSaveResponse(this.labTestResultService.update(labTestResult));
    } else {
      this.subscribeToSaveResponse(this.labTestResultService.create(labTestResult));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILabTestResult>>): void {
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

  protected updateForm(labTestResult: ILabTestResult): void {
    this.labTestResult = labTestResult;
    this.labTestResultFormService.resetForm(this.editForm, labTestResult);

    this.patientsSharedCollection = this.patientService.addPatientToCollectionIfMissing<IPatient>(
      this.patientsSharedCollection,
      labTestResult.patient,
    );
    this.labTestCatalogsSharedCollection = this.labTestCatalogService.addLabTestCatalogToCollectionIfMissing<ILabTestCatalog>(
      this.labTestCatalogsSharedCollection,
      labTestResult.labTest,
    );
    this.consultationsSharedCollection = this.consultationService.addConsultationToCollectionIfMissing<IConsultation>(
      this.consultationsSharedCollection,
      labTestResult.consultation,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.patientService
      .query()
      .pipe(map((res: HttpResponse<IPatient[]>) => res.body ?? []))
      .pipe(
        map((patients: IPatient[]) => this.patientService.addPatientToCollectionIfMissing<IPatient>(patients, this.labTestResult?.patient)),
      )
      .subscribe((patients: IPatient[]) => (this.patientsSharedCollection = patients));

    this.labTestCatalogService
      .query()
      .pipe(map((res: HttpResponse<ILabTestCatalog[]>) => res.body ?? []))
      .pipe(
        map((labTestCatalogs: ILabTestCatalog[]) =>
          this.labTestCatalogService.addLabTestCatalogToCollectionIfMissing<ILabTestCatalog>(labTestCatalogs, this.labTestResult?.labTest),
        ),
      )
      .subscribe((labTestCatalogs: ILabTestCatalog[]) => (this.labTestCatalogsSharedCollection = labTestCatalogs));

    this.consultationService
      .query()
      .pipe(map((res: HttpResponse<IConsultation[]>) => res.body ?? []))
      .pipe(
        map((consultations: IConsultation[]) =>
          this.consultationService.addConsultationToCollectionIfMissing<IConsultation>(consultations, this.labTestResult?.consultation),
        ),
      )
      .subscribe((consultations: IConsultation[]) => (this.consultationsSharedCollection = consultations));
  }
}
