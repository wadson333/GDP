import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../lab-test-catalog.test-samples';

import { LabTestCatalogFormService } from './lab-test-catalog-form.service';

describe('LabTestCatalog Form Service', () => {
  let service: LabTestCatalogFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LabTestCatalogFormService);
  });

  describe('Service methods', () => {
    describe('createLabTestCatalogFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLabTestCatalogFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            unit: expect.any(Object),
            description: expect.any(Object),
            version: expect.any(Object),
            validFrom: expect.any(Object),
            validTo: expect.any(Object),
            method: expect.any(Object),
            sampleType: expect.any(Object),
            referenceRangeLow: expect.any(Object),
            referenceRangeHigh: expect.any(Object),
            active: expect.any(Object),
            type: expect.any(Object),
            loincCode: expect.any(Object),
            cost: expect.any(Object),
            turnaroundTime: expect.any(Object),
          }),
        );
      });

      it('passing ILabTestCatalog should create a new form with FormGroup', () => {
        const formGroup = service.createLabTestCatalogFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            unit: expect.any(Object),
            description: expect.any(Object),
            version: expect.any(Object),
            validFrom: expect.any(Object),
            validTo: expect.any(Object),
            method: expect.any(Object),
            sampleType: expect.any(Object),
            referenceRangeLow: expect.any(Object),
            referenceRangeHigh: expect.any(Object),
            active: expect.any(Object),
            type: expect.any(Object),
            loincCode: expect.any(Object),
            cost: expect.any(Object),
            turnaroundTime: expect.any(Object),
          }),
        );
      });
    });

    describe('getLabTestCatalog', () => {
      it('should return NewLabTestCatalog for default LabTestCatalog initial value', () => {
        const formGroup = service.createLabTestCatalogFormGroup(sampleWithNewData);

        const labTestCatalog = service.getLabTestCatalog(formGroup) as any;

        expect(labTestCatalog).toMatchObject(sampleWithNewData);
      });

      it('should return NewLabTestCatalog for empty LabTestCatalog initial value', () => {
        const formGroup = service.createLabTestCatalogFormGroup();

        const labTestCatalog = service.getLabTestCatalog(formGroup) as any;

        expect(labTestCatalog).toMatchObject({});
      });

      it('should return ILabTestCatalog', () => {
        const formGroup = service.createLabTestCatalogFormGroup(sampleWithRequiredData);

        const labTestCatalog = service.getLabTestCatalog(formGroup) as any;

        expect(labTestCatalog).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILabTestCatalog should not enable id FormControl', () => {
        const formGroup = service.createLabTestCatalogFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLabTestCatalog should disable id FormControl', () => {
        const formGroup = service.createLabTestCatalogFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
