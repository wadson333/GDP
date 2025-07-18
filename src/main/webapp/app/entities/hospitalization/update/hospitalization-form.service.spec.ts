import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../hospitalization.test-samples';

import { HospitalizationFormService } from './hospitalization-form.service';

describe('Hospitalization Form Service', () => {
  let service: HospitalizationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HospitalizationFormService);
  });

  describe('Service methods', () => {
    describe('createHospitalizationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHospitalizationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            admissionDate: expect.any(Object),
            dischargeDate: expect.any(Object),
            reason: expect.any(Object),
            patient: expect.any(Object),
            attendingDoctor: expect.any(Object),
          }),
        );
      });

      it('passing IHospitalization should create a new form with FormGroup', () => {
        const formGroup = service.createHospitalizationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            admissionDate: expect.any(Object),
            dischargeDate: expect.any(Object),
            reason: expect.any(Object),
            patient: expect.any(Object),
            attendingDoctor: expect.any(Object),
          }),
        );
      });
    });

    describe('getHospitalization', () => {
      it('should return NewHospitalization for default Hospitalization initial value', () => {
        const formGroup = service.createHospitalizationFormGroup(sampleWithNewData);

        const hospitalization = service.getHospitalization(formGroup) as any;

        expect(hospitalization).toMatchObject(sampleWithNewData);
      });

      it('should return NewHospitalization for empty Hospitalization initial value', () => {
        const formGroup = service.createHospitalizationFormGroup();

        const hospitalization = service.getHospitalization(formGroup) as any;

        expect(hospitalization).toMatchObject({});
      });

      it('should return IHospitalization', () => {
        const formGroup = service.createHospitalizationFormGroup(sampleWithRequiredData);

        const hospitalization = service.getHospitalization(formGroup) as any;

        expect(hospitalization).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHospitalization should not enable id FormControl', () => {
        const formGroup = service.createHospitalizationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHospitalization should disable id FormControl', () => {
        const formGroup = service.createHospitalizationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
