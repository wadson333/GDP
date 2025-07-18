import dayjs from 'dayjs/esm';

import { IPrescription, NewPrescription } from './prescription.model';

export const sampleWithRequiredData: IPrescription = {
  id: 10657,
  prescriptionDate: dayjs('2025-07-16'),
};

export const sampleWithPartialData: IPrescription = {
  id: 4851,
  prescriptionDate: dayjs('2025-07-16'),
};

export const sampleWithFullData: IPrescription = {
  id: 27968,
  prescriptionDate: dayjs('2025-07-16'),
};

export const sampleWithNewData: NewPrescription = {
  prescriptionDate: dayjs('2025-07-16'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
