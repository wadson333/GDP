import dayjs from 'dayjs/esm';

import { IPatient, NewPatient } from './patient.model';

export const sampleWithRequiredData: IPatient = {
  id: 2300,
  firstName: 'Raoul',
  lastName: 'Lemoine',
  birthDate: dayjs('2025-07-15'),
  phone1: 'selon pressentir',
};

export const sampleWithPartialData: IPatient = {
  id: 3330,
  firstName: 'Muriel',
  lastName: 'Guyot',
  birthDate: dayjs('2025-07-16'),
  address: '../fake-data/blob/hipster.txt',
  phone1: 'coac coac agiter hors',
  email: 'Gautier_Noel70@yahoo.fr',
  ninu: 'antagoniste malgré boum',
  contactPersonName: 'vlan quant à adepte',
  contactPersonPhone: 'émérite tchou tchouu grâce à',
  antecedents: '../fake-data/blob/hipster.txt',
  allergies: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IPatient = {
  id: 27347,
  firstName: 'Xénophon',
  lastName: 'Laurent',
  birthDate: dayjs('2025-07-16'),
  gender: 'FEMALE',
  bloodType: 'au prix de éliminer cot cot',
  address: '../fake-data/blob/hipster.txt',
  phone1: 'actionnaire énergique à moins de',
  phone2: 'biathlète',
  email: 'Jude19@hotmail.fr',
  nif: 'résigner',
  ninu: 'à condition que',
  heightCm: 22788,
  weightKg: 31110.96,
  passportNumber: 'sur défier mal',
  contactPersonName: 'parce que cocorico',
  contactPersonPhone: 'loin davantage suivant',
  antecedents: '../fake-data/blob/hipster.txt',
  allergies: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewPatient = {
  firstName: 'Alcidie',
  lastName: 'David',
  birthDate: dayjs('2025-07-16'),
  phone1: 'chut',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
