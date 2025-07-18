import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../doctor-profile.test-samples';

import { DoctorProfileFormService } from './doctor-profile-form.service';

describe('DoctorProfile Form Service', () => {
  let service: DoctorProfileFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DoctorProfileFormService);
  });

  describe('Service methods', () => {
    describe('createDoctorProfileFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDoctorProfileFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            specialty: expect.any(Object),
            licenseNumber: expect.any(Object),
            university: expect.any(Object),
            startDateOfPractice: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing IDoctorProfile should create a new form with FormGroup', () => {
        const formGroup = service.createDoctorProfileFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            specialty: expect.any(Object),
            licenseNumber: expect.any(Object),
            university: expect.any(Object),
            startDateOfPractice: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getDoctorProfile', () => {
      it('should return NewDoctorProfile for default DoctorProfile initial value', () => {
        const formGroup = service.createDoctorProfileFormGroup(sampleWithNewData);

        const doctorProfile = service.getDoctorProfile(formGroup) as any;

        expect(doctorProfile).toMatchObject(sampleWithNewData);
      });

      it('should return NewDoctorProfile for empty DoctorProfile initial value', () => {
        const formGroup = service.createDoctorProfileFormGroup();

        const doctorProfile = service.getDoctorProfile(formGroup) as any;

        expect(doctorProfile).toMatchObject({});
      });

      it('should return IDoctorProfile', () => {
        const formGroup = service.createDoctorProfileFormGroup(sampleWithRequiredData);

        const doctorProfile = service.getDoctorProfile(formGroup) as any;

        expect(doctorProfile).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDoctorProfile should not enable id FormControl', () => {
        const formGroup = service.createDoctorProfileFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDoctorProfile should disable id FormControl', () => {
        const formGroup = service.createDoctorProfileFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
