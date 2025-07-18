import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../user-configuration.test-samples';

import { UserConfigurationFormService } from './user-configuration-form.service';

describe('UserConfiguration Form Service', () => {
  let service: UserConfigurationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserConfigurationFormService);
  });

  describe('Service methods', () => {
    describe('createUserConfigurationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserConfigurationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            twoFactorEnabled: expect.any(Object),
            twoFactorSecret: expect.any(Object),
            receiveEmailNotifs: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing IUserConfiguration should create a new form with FormGroup', () => {
        const formGroup = service.createUserConfigurationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            twoFactorEnabled: expect.any(Object),
            twoFactorSecret: expect.any(Object),
            receiveEmailNotifs: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getUserConfiguration', () => {
      it('should return NewUserConfiguration for default UserConfiguration initial value', () => {
        const formGroup = service.createUserConfigurationFormGroup(sampleWithNewData);

        const userConfiguration = service.getUserConfiguration(formGroup) as any;

        expect(userConfiguration).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserConfiguration for empty UserConfiguration initial value', () => {
        const formGroup = service.createUserConfigurationFormGroup();

        const userConfiguration = service.getUserConfiguration(formGroup) as any;

        expect(userConfiguration).toMatchObject({});
      });

      it('should return IUserConfiguration', () => {
        const formGroup = service.createUserConfigurationFormGroup(sampleWithRequiredData);

        const userConfiguration = service.getUserConfiguration(formGroup) as any;

        expect(userConfiguration).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserConfiguration should not enable id FormControl', () => {
        const formGroup = service.createUserConfigurationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserConfiguration should disable id FormControl', () => {
        const formGroup = service.createUserConfigurationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
