import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IUserConfiguration, NewUserConfiguration } from '../user-configuration.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserConfiguration for edit and NewUserConfigurationFormGroupInput for create.
 */
type UserConfigurationFormGroupInput = IUserConfiguration | PartialWithRequiredKeyOf<NewUserConfiguration>;

type UserConfigurationFormDefaults = Pick<NewUserConfiguration, 'id' | 'twoFactorEnabled' | 'receiveEmailNotifs'>;

type UserConfigurationFormGroupContent = {
  id: FormControl<IUserConfiguration['id'] | NewUserConfiguration['id']>;
  twoFactorEnabled: FormControl<IUserConfiguration['twoFactorEnabled']>;
  twoFactorSecret: FormControl<IUserConfiguration['twoFactorSecret']>;
  receiveEmailNotifs: FormControl<IUserConfiguration['receiveEmailNotifs']>;
  user: FormControl<IUserConfiguration['user']>;
};

export type UserConfigurationFormGroup = FormGroup<UserConfigurationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserConfigurationFormService {
  createUserConfigurationFormGroup(userConfiguration: UserConfigurationFormGroupInput = { id: null }): UserConfigurationFormGroup {
    const userConfigurationRawValue = {
      ...this.getFormDefaults(),
      ...userConfiguration,
    };
    return new FormGroup<UserConfigurationFormGroupContent>({
      id: new FormControl(
        { value: userConfigurationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      twoFactorEnabled: new FormControl(userConfigurationRawValue.twoFactorEnabled, {
        validators: [Validators.required],
      }),
      twoFactorSecret: new FormControl(userConfigurationRawValue.twoFactorSecret),
      receiveEmailNotifs: new FormControl(userConfigurationRawValue.receiveEmailNotifs, {
        validators: [Validators.required],
      }),
      user: new FormControl(userConfigurationRawValue.user, {
        validators: [Validators.required],
      }),
    });
  }

  getUserConfiguration(form: UserConfigurationFormGroup): IUserConfiguration | NewUserConfiguration {
    return form.getRawValue() as IUserConfiguration | NewUserConfiguration;
  }

  resetForm(form: UserConfigurationFormGroup, userConfiguration: UserConfigurationFormGroupInput): void {
    const userConfigurationRawValue = { ...this.getFormDefaults(), ...userConfiguration };
    form.reset(
      {
        ...userConfigurationRawValue,
        id: { value: userConfigurationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserConfigurationFormDefaults {
    return {
      id: null,
      twoFactorEnabled: false,
      receiveEmailNotifs: false,
    };
  }
}
