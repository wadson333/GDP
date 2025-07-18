import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../lab-test-result.test-samples';

import { LabTestResultFormService } from './lab-test-result-form.service';

describe('LabTestResult Form Service', () => {
  let service: LabTestResultFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LabTestResultFormService);
  });

  describe('Service methods', () => {
    describe('createLabTestResultFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLabTestResultFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            resultValue: expect.any(Object),
            resultDate: expect.any(Object),
            isAbnormal: expect.any(Object),
            patient: expect.any(Object),
            labTest: expect.any(Object),
            consultation: expect.any(Object),
          }),
        );
      });

      it('passing ILabTestResult should create a new form with FormGroup', () => {
        const formGroup = service.createLabTestResultFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            resultValue: expect.any(Object),
            resultDate: expect.any(Object),
            isAbnormal: expect.any(Object),
            patient: expect.any(Object),
            labTest: expect.any(Object),
            consultation: expect.any(Object),
          }),
        );
      });
    });

    describe('getLabTestResult', () => {
      it('should return NewLabTestResult for default LabTestResult initial value', () => {
        const formGroup = service.createLabTestResultFormGroup(sampleWithNewData);

        const labTestResult = service.getLabTestResult(formGroup) as any;

        expect(labTestResult).toMatchObject(sampleWithNewData);
      });

      it('should return NewLabTestResult for empty LabTestResult initial value', () => {
        const formGroup = service.createLabTestResultFormGroup();

        const labTestResult = service.getLabTestResult(formGroup) as any;

        expect(labTestResult).toMatchObject({});
      });

      it('should return ILabTestResult', () => {
        const formGroup = service.createLabTestResultFormGroup(sampleWithRequiredData);

        const labTestResult = service.getLabTestResult(formGroup) as any;

        expect(labTestResult).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILabTestResult should not enable id FormControl', () => {
        const formGroup = service.createLabTestResultFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLabTestResult should disable id FormControl', () => {
        const formGroup = service.createLabTestResultFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
