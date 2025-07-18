import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IUserConfiguration } from '../user-configuration.model';
import { UserConfigurationService } from '../service/user-configuration.service';
import { UserConfigurationFormGroup, UserConfigurationFormService } from './user-configuration-form.service';

@Component({
  standalone: true,
  selector: 'jhi-user-configuration-update',
  templateUrl: './user-configuration-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UserConfigurationUpdateComponent implements OnInit {
  isSaving = false;
  userConfiguration: IUserConfiguration | null = null;

  usersSharedCollection: IUser[] = [];

  protected userConfigurationService = inject(UserConfigurationService);
  protected userConfigurationFormService = inject(UserConfigurationFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UserConfigurationFormGroup = this.userConfigurationFormService.createUserConfigurationFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userConfiguration }) => {
      this.userConfiguration = userConfiguration;
      if (userConfiguration) {
        this.updateForm(userConfiguration);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userConfiguration = this.userConfigurationFormService.getUserConfiguration(this.editForm);
    if (userConfiguration.id !== null) {
      this.subscribeToSaveResponse(this.userConfigurationService.update(userConfiguration));
    } else {
      this.subscribeToSaveResponse(this.userConfigurationService.create(userConfiguration));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserConfiguration>>): void {
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

  protected updateForm(userConfiguration: IUserConfiguration): void {
    this.userConfiguration = userConfiguration;
    this.userConfigurationFormService.resetForm(this.editForm, userConfiguration);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, userConfiguration.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.userConfiguration?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
