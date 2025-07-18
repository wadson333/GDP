import dayjs from 'dayjs/esm';

import { IDoctorProfile, NewDoctorProfile } from './doctor-profile.model';

export const sampleWithRequiredData: IDoctorProfile = {
  id: 24929,
  specialty: 'en habituer toc-toc',
  licenseNumber: 'membre à vie miam en',
  startDateOfPractice: dayjs('2025-07-16'),
};

export const sampleWithPartialData: IDoctorProfile = {
  id: 7338,
  specialty: 'établir puisque espiègle',
  licenseNumber: 'du fait que derrière sédentaire',
  university: 'au point que désormais membre à vie',
  startDateOfPractice: dayjs('2025-07-16'),
};

export const sampleWithFullData: IDoctorProfile = {
  id: 13980,
  specialty: 'bientôt chef de cuisine',
  licenseNumber: 'bzzz prêter',
  university: 'acheter',
  startDateOfPractice: dayjs('2025-07-16'),
};

export const sampleWithNewData: NewDoctorProfile = {
  specialty: 'certes obéir serviable',
  licenseNumber: 'prout dérouler debout',
  startDateOfPractice: dayjs('2025-07-16'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
