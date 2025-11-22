import dayjs from 'dayjs/esm';

import { IPatient, NewPatient } from './patient.model';

export const sampleWithRequiredData: IPatient = {
  id: 13725,
  firstName: 'Célestine',
  lastName: 'Fleury',
  birthDate: dayjs('2025-07-16'),
  phone1: '+303380189721329',
};

export const sampleWithPartialData: IPatient = {
  id: 13653,
  uid: '64da56f2-ee12-4cf3-a814-0b4bcb8ca0b3',
  firstName: 'Mélodie',
  lastName: 'Gauthier',
  birthDate: dayjs('2025-07-16'),
  gender: 'FEMALE',
  bloodType: 'O_NEG',
  phone1: '1909',
  ninu: 'areu areu ',
  medicalRecordNumber: 'appuyer comme pin-pon',
  heightCm: 201,
  passportNumber: 'partenaire',
  antecedents: '../fake-data/blob/hipster.txt',
  allergies: '../fake-data/blob/hipster.txt',
  clinicalNotes: '../fake-data/blob/hipster.txt',
  gdprConsentDate: dayjs('2025-07-16T13:22'),
  status: 'ARCHIVED',
  deceasedDate: dayjs('2025-07-15T20:55'),
  insuranceCompanyName: 'efficace gestionnaire',
  patientInsuranceId: 'au-devant circulaire à la merci',
  insurancePolicyNumber: 'à partir de ouch',
};

export const sampleWithFullData: IPatient = {
  id: 12371,
  uid: '2f2d2aaf-c9f1-41c9-96bd-f631a9af89ae',
  firstName: 'Alain',
  lastName: 'Breton',
  birthDate: dayjs('2025-07-16'),
  gender: 'FEMALE',
  bloodType: 'B_POS',
  address: '../fake-data/blob/hipster.txt',
  phone1: '+534564095152452',
  phone2: '+875493589817',
  nif: 'de façon q',
  ninu: 'afin que a',
  medicalRecordNumber: 'assez',
  heightCm: 97,
  weightKg: 353.14,
  passportNumber: 'fouiller ressen',
  contactPersonName: 'dispenser',
  contactPersonPhone: '2898',
  antecedents: '../fake-data/blob/hipster.txt',
  allergies: '../fake-data/blob/hipster.txt',
  clinicalNotes: '../fake-data/blob/hipster.txt',
  smokingStatus: 'CURRENT',
  gdprConsentDate: dayjs('2025-07-16T11:25'),
  status: 'INACTIVE',
  deceasedDate: dayjs('2025-07-16T09:43'),
  insuranceCompanyName: 'si bien que membre titulaire hors',
  patientInsuranceId: 'du fait que aigre secouriste',
  insurancePolicyNumber: 'insolite au-dessous',
  insuranceCoverageType: 'de manière à ce que pour que',
  insuranceValidFrom: dayjs('2025-07-15'),
  insuranceValidTo: dayjs('2025-07-16'),
};

export const sampleWithNewData: NewPatient = {
  firstName: 'Corinne',
  lastName: 'Durand',
  birthDate: dayjs('2025-07-15'),
  phone1: '+585',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
