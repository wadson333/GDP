import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPrescription } from 'app/entities/prescription/prescription.model';
import { PrescriptionService } from 'app/entities/prescription/service/prescription.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IPatient } from 'app/entities/patient/patient.model';
import { PatientService } from 'app/entities/patient/service/patient.service';
import { ConsultationService } from '../service/consultation.service';
import { IConsultation } from '../consultation.model';
import { ConsultationFormGroup, ConsultationFormService } from './consultation-form.service';

@Component({
  standalone: true,
  selector: 'jhi-consultation-update',
  templateUrl: './consultation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ConsultationUpdateComponent implements OnInit {
  isSaving = false;
  consultation: IConsultation | null = null;

  prescriptionsCollection: IPrescription[] = [];
  usersSharedCollection: IUser[] = [];
  patientsSharedCollection: IPatient[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected consultationService = inject(ConsultationService);
  protected consultationFormService = inject(ConsultationFormService);
  protected prescriptionService = inject(PrescriptionService);
  protected userService = inject(UserService);
  protected patientService = inject(PatientService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ConsultationFormGroup = this.consultationFormService.createConsultationFormGroup();

  comparePrescription = (o1: IPrescription | null, o2: IPrescription | null): boolean =>
    this.prescriptionService.comparePrescription(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  comparePatient = (o1: IPatient | null, o2: IPatient | null): boolean => this.patientService.comparePatient(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ consultation }) => {
      this.consultation = consultation;
      if (consultation) {
        this.updateForm(consultation);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('gdpApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const consultation = this.consultationFormService.getConsultation(this.editForm);
    if (consultation.id !== null) {
      this.subscribeToSaveResponse(this.consultationService.update(consultation));
    } else {
      this.subscribeToSaveResponse(this.consultationService.create(consultation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConsultation>>): void {
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

  protected updateForm(consultation: IConsultation): void {
    this.consultation = consultation;
    this.consultationFormService.resetForm(this.editForm, consultation);

    this.prescriptionsCollection = this.prescriptionService.addPrescriptionToCollectionIfMissing<IPrescription>(
      this.prescriptionsCollection,
      consultation.prescription,
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, consultation.doctor);
    this.patientsSharedCollection = this.patientService.addPatientToCollectionIfMissing<IPatient>(
      this.patientsSharedCollection,
      consultation.patient,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.prescriptionService
      .query({ filter: 'consultation-is-null' })
      .pipe(map((res: HttpResponse<IPrescription[]>) => res.body ?? []))
      .pipe(
        map((prescriptions: IPrescription[]) =>
          this.prescriptionService.addPrescriptionToCollectionIfMissing<IPrescription>(prescriptions, this.consultation?.prescription),
        ),
      )
      .subscribe((prescriptions: IPrescription[]) => (this.prescriptionsCollection = prescriptions));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.consultation?.doctor)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.patientService
      .query()
      .pipe(map((res: HttpResponse<IPatient[]>) => res.body ?? []))
      .pipe(
        map((patients: IPatient[]) => this.patientService.addPatientToCollectionIfMissing<IPatient>(patients, this.consultation?.patient)),
      )
      .subscribe((patients: IPatient[]) => (this.patientsSharedCollection = patients));
  }
}
