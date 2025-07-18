import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../prescription-item.test-samples';

import { PrescriptionItemFormService } from './prescription-item-form.service';

describe('PrescriptionItem Form Service', () => {
  let service: PrescriptionItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PrescriptionItemFormService);
  });

  describe('Service methods', () => {
    describe('createPrescriptionItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPrescriptionItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dosage: expect.any(Object),
            frequency: expect.any(Object),
            duration: expect.any(Object),
            medication: expect.any(Object),
            prescription: expect.any(Object),
          }),
        );
      });

      it('passing IPrescriptionItem should create a new form with FormGroup', () => {
        const formGroup = service.createPrescriptionItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dosage: expect.any(Object),
            frequency: expect.any(Object),
            duration: expect.any(Object),
            medication: expect.any(Object),
            prescription: expect.any(Object),
          }),
        );
      });
    });

    describe('getPrescriptionItem', () => {
      it('should return NewPrescriptionItem for default PrescriptionItem initial value', () => {
        const formGroup = service.createPrescriptionItemFormGroup(sampleWithNewData);

        const prescriptionItem = service.getPrescriptionItem(formGroup) as any;

        expect(prescriptionItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewPrescriptionItem for empty PrescriptionItem initial value', () => {
        const formGroup = service.createPrescriptionItemFormGroup();

        const prescriptionItem = service.getPrescriptionItem(formGroup) as any;

        expect(prescriptionItem).toMatchObject({});
      });

      it('should return IPrescriptionItem', () => {
        const formGroup = service.createPrescriptionItemFormGroup(sampleWithRequiredData);

        const prescriptionItem = service.getPrescriptionItem(formGroup) as any;

        expect(prescriptionItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPrescriptionItem should not enable id FormControl', () => {
        const formGroup = service.createPrescriptionItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPrescriptionItem should disable id FormControl', () => {
        const formGroup = service.createPrescriptionItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
