import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../patient.test-samples';

import { PatientFormService } from './patient-form.service';

describe('Patient Form Service', () => {
  let service: PatientFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PatientFormService);
  });

  describe('Service methods', () => {
    describe('createPatientFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPatientFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            uid: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            birthDate: expect.any(Object),
            gender: expect.any(Object),
            bloodType: expect.any(Object),
            address: expect.any(Object),
            phone1: expect.any(Object),
            phone2: expect.any(Object),
            nif: expect.any(Object),
            ninu: expect.any(Object),
            medicalRecordNumber: expect.any(Object),
            heightCm: expect.any(Object),
            weightKg: expect.any(Object),
            passportNumber: expect.any(Object),
            contactPersonName: expect.any(Object),
            contactPersonPhone: expect.any(Object),
            antecedents: expect.any(Object),
            allergies: expect.any(Object),
            clinicalNotes: expect.any(Object),
            smokingStatus: expect.any(Object),
            gdprConsentDate: expect.any(Object),
            status: expect.any(Object),
            deceasedDate: expect.any(Object),
            insuranceCompanyName: expect.any(Object),
            patientInsuranceId: expect.any(Object),
            insurancePolicyNumber: expect.any(Object),
            insuranceCoverageType: expect.any(Object),
            insuranceValidFrom: expect.any(Object),
            insuranceValidTo: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing IPatient should create a new form with FormGroup', () => {
        const formGroup = service.createPatientFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            uid: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            birthDate: expect.any(Object),
            gender: expect.any(Object),
            bloodType: expect.any(Object),
            address: expect.any(Object),
            phone1: expect.any(Object),
            phone2: expect.any(Object),
            nif: expect.any(Object),
            ninu: expect.any(Object),
            medicalRecordNumber: expect.any(Object),
            heightCm: expect.any(Object),
            weightKg: expect.any(Object),
            passportNumber: expect.any(Object),
            contactPersonName: expect.any(Object),
            contactPersonPhone: expect.any(Object),
            antecedents: expect.any(Object),
            allergies: expect.any(Object),
            clinicalNotes: expect.any(Object),
            smokingStatus: expect.any(Object),
            gdprConsentDate: expect.any(Object),
            status: expect.any(Object),
            deceasedDate: expect.any(Object),
            insuranceCompanyName: expect.any(Object),
            patientInsuranceId: expect.any(Object),
            insurancePolicyNumber: expect.any(Object),
            insuranceCoverageType: expect.any(Object),
            insuranceValidFrom: expect.any(Object),
            insuranceValidTo: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getPatient', () => {
      it('should return NewPatient for default Patient initial value', () => {
        const formGroup = service.createPatientFormGroup(sampleWithNewData);

        const patient = service.getPatient(formGroup) as any;

        expect(patient).toMatchObject(sampleWithNewData);
      });

      it('should return NewPatient for empty Patient initial value', () => {
        const formGroup = service.createPatientFormGroup();

        const patient = service.getPatient(formGroup) as any;

        expect(patient).toMatchObject({});
      });

      it('should return IPatient', () => {
        const formGroup = service.createPatientFormGroup(sampleWithRequiredData);

        const patient = service.getPatient(formGroup) as any;

        expect(patient).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPatient should not enable id FormControl', () => {
        const formGroup = service.createPatientFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPatient should disable id FormControl', () => {
        const formGroup = service.createPatientFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
