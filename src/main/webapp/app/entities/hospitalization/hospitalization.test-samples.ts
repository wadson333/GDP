import dayjs from 'dayjs/esm';

import { IHospitalization, NewHospitalization } from './hospitalization.model';

export const sampleWithRequiredData: IHospitalization = {
  id: 16293,
  admissionDate: dayjs('2025-07-16T10:30'),
};

export const sampleWithPartialData: IHospitalization = {
  id: 27531,
  admissionDate: dayjs('2025-07-16T02:16'),
  dischargeDate: dayjs('2025-07-15T20:40'),
};

export const sampleWithFullData: IHospitalization = {
  id: 11149,
  admissionDate: dayjs('2025-07-16T09:40'),
  dischargeDate: dayjs('2025-07-16T08:51'),
  reason: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewHospitalization = {
  admissionDate: dayjs('2025-07-16T02:47'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
