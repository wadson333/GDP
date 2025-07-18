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
import { IPatient } from 'app/entities/patient/patient.model';
import { PatientService } from 'app/entities/patient/service/patient.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { HospitalizationService } from '../service/hospitalization.service';
import { IHospitalization } from '../hospitalization.model';
import { HospitalizationFormGroup, HospitalizationFormService } from './hospitalization-form.service';

@Component({
  standalone: true,
  selector: 'jhi-hospitalization-update',
  templateUrl: './hospitalization-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class HospitalizationUpdateComponent implements OnInit {
  isSaving = false;
  hospitalization: IHospitalization | null = null;

  patientsSharedCollection: IPatient[] = [];
  usersSharedCollection: IUser[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected hospitalizationService = inject(HospitalizationService);
  protected hospitalizationFormService = inject(HospitalizationFormService);
  protected patientService = inject(PatientService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: HospitalizationFormGroup = this.hospitalizationFormService.createHospitalizationFormGroup();

  comparePatient = (o1: IPatient | null, o2: IPatient | null): boolean => this.patientService.comparePatient(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hospitalization }) => {
      this.hospitalization = hospitalization;
      if (hospitalization) {
        this.updateForm(hospitalization);
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
    const hospitalization = this.hospitalizationFormService.getHospitalization(this.editForm);
    if (hospitalization.id !== null) {
      this.subscribeToSaveResponse(this.hospitalizationService.update(hospitalization));
    } else {
      this.subscribeToSaveResponse(this.hospitalizationService.create(hospitalization));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHospitalization>>): void {
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

  protected updateForm(hospitalization: IHospitalization): void {
    this.hospitalization = hospitalization;
    this.hospitalizationFormService.resetForm(this.editForm, hospitalization);

    this.patientsSharedCollection = this.patientService.addPatientToCollectionIfMissing<IPatient>(
      this.patientsSharedCollection,
      hospitalization.patient,
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      hospitalization.attendingDoctor,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.patientService
      .query()
      .pipe(map((res: HttpResponse<IPatient[]>) => res.body ?? []))
      .pipe(
        map((patients: IPatient[]) =>
          this.patientService.addPatientToCollectionIfMissing<IPatient>(patients, this.hospitalization?.patient),
        ),
      )
      .subscribe((patients: IPatient[]) => (this.patientsSharedCollection = patients));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.hospitalization?.attendingDoctor)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
