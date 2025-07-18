import dayjs from 'dayjs/esm';

import { IConsultation, NewConsultation } from './consultation.model';

export const sampleWithRequiredData: IConsultation = {
  id: 22451,
  consultationDate: dayjs('2025-07-16T12:00'),
};

export const sampleWithPartialData: IConsultation = {
  id: 24278,
  consultationDate: dayjs('2025-07-16T09:24'),
  symptoms: '../fake-data/blob/hipster.txt',
  diagnosis: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IConsultation = {
  id: 6069,
  consultationDate: dayjs('2025-07-16T10:29'),
  symptoms: '../fake-data/blob/hipster.txt',
  diagnosis: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewConsultation = {
  consultationDate: dayjs('2025-07-16T14:33'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
