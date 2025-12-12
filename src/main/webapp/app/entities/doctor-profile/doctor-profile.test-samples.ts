import dayjs from 'dayjs/esm';

import { IDoctorProfile, NewDoctorProfile } from './doctor-profile.model';

export const sampleWithRequiredData: IDoctorProfile = {
  id: 18034,
  uid: '57b0f43c-ee27-4f0f-bf9b-fc1f973b5b7b',
  medicalLicenseNumber: 'finir guide spécialiste',
  firstName: 'Claude',
  lastName: 'Dupuis',
  birthDate: dayjs('2025-07-16'),
  primarySpecialty: 'OTHER',
  startDateOfPractice: dayjs('2025-07-16'),
  consultationDurationMinutes: 8,
  acceptingNewPatients: true,
  allowsTeleconsultation: true,
  status: 'RETIRED',
};

export const sampleWithPartialData: IDoctorProfile = {
  id: 15582,
  codeClinic: 'touriste p',
  uid: 'c95a51f2-e5f3-4c80-b977-8a7a45ca59ef',
  medicalLicenseNumber: 'cyan pourpre bang',
  firstName: 'Ange',
  lastName: 'Prevost',
  birthDate: dayjs('2025-07-16'),
  bloodType: 'B_NEG',
  primarySpecialty: 'OTHER',
  university: 'ça vouh',
  startDateOfPractice: dayjs('2025-07-16'),
  consultationDurationMinutes: 102,
  acceptingNewPatients: false,
  allowsTeleconsultation: true,
  bio: '../fake-data/blob/hipster.txt',
  officePhone: '+117',
  status: 'PENDING_APPROVAL',
  verifiedAt: dayjs('2025-07-16T05:38'),
  nif: 'débileXXXX',
  version: 15973,
};

export const sampleWithFullData: IDoctorProfile = {
  id: 23855,
  codeClinic: 'flotter en',
  uid: '456ed152-580c-476d-9401-0dd8c069a9d7',
  medicalLicenseNumber: 'pin-pon toc-toc',
  firstName: 'Amour',
  lastName: 'Jacquet',
  birthDate: dayjs('2025-07-16'),
  gender: 'OTHER',
  bloodType: 'B_NEG',
  primarySpecialty: 'DENTIST',
  otherSpecialties: '../fake-data/blob/hipster.txt',
  university: 'avancer',
  graduationYear: 2079,
  startDateOfPractice: dayjs('2025-07-16'),
  consultationDurationMinutes: 97,
  acceptingNewPatients: true,
  allowsTeleconsultation: false,
  consultationFee: 20353.61,
  teleconsultationFee: 17076.41,
  bio: '../fake-data/blob/hipster.txt',
  spokenLanguages: 'quand sédentaire',
  websiteUrl: 'beaucoup',
  officePhone: '+605304964389704',
  officeAddress: '../fake-data/blob/hipster.txt',
  status: 'PENDING_APPROVAL',
  isVerified: false,
  verifiedAt: dayjs('2025-07-16T06:35'),
  nif: 'ébranler é',
  ninu: 'oh pour bi',
  averageRating: 3.29,
  reviewCount: 14719,
  version: 27365,
};

export const sampleWithNewData: NewDoctorProfile = {
  uid: '9ed24874-5f03-469e-bf3f-50ae3f3836db',
  medicalLicenseNumber: 'au cas où',
  firstName: 'Macaire',
  lastName: 'Marty',
  birthDate: dayjs('2025-07-15'),
  primarySpecialty: 'ORTHOPEDIST',
  startDateOfPractice: dayjs('2025-07-15'),
  consultationDurationMinutes: 93,
  acceptingNewPatients: false,
  allowsTeleconsultation: false,
  status: 'SUSPENDED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
