import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IDoctorProfile } from '../doctor-profile.model';
import { DoctorProfileService } from '../service/doctor-profile.service';
import { DoctorProfileFormGroup, DoctorProfileFormService } from './doctor-profile-form.service';

@Component({
  standalone: true,
  selector: 'jhi-doctor-profile-update',
  templateUrl: './doctor-profile-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DoctorProfileUpdateComponent implements OnInit {
  isSaving = false;
  doctorProfile: IDoctorProfile | null = null;

  usersSharedCollection: IUser[] = [];

  protected doctorProfileService = inject(DoctorProfileService);
  protected doctorProfileFormService = inject(DoctorProfileFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DoctorProfileFormGroup = this.doctorProfileFormService.createDoctorProfileFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ doctorProfile }) => {
      this.doctorProfile = doctorProfile;
      if (doctorProfile) {
        this.updateForm(doctorProfile);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const doctorProfile = this.doctorProfileFormService.getDoctorProfile(this.editForm);
    if (doctorProfile.id !== null) {
      this.subscribeToSaveResponse(this.doctorProfileService.update(doctorProfile));
    } else {
      this.subscribeToSaveResponse(this.doctorProfileService.create(doctorProfile));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDoctorProfile>>): void {
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

  protected updateForm(doctorProfile: IDoctorProfile): void {
    this.doctorProfile = doctorProfile;
    this.doctorProfileFormService.resetForm(this.editForm, doctorProfile);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, doctorProfile.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.doctorProfile?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
