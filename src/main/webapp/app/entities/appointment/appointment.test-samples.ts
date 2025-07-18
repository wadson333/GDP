import dayjs from 'dayjs/esm';

import { IAppointment, NewAppointment } from './appointment.model';

export const sampleWithRequiredData: IAppointment = {
  id: 13731,
  startTime: dayjs('2025-07-16T09:52'),
  endTime: dayjs('2025-07-15T20:52'),
  status: 'CONFIRMED',
};

export const sampleWithPartialData: IAppointment = {
  id: 31634,
  startTime: dayjs('2025-07-16T17:03'),
  endTime: dayjs('2025-07-16T14:22'),
  status: 'CANCELLED_BY_PATIENT',
  reason: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IAppointment = {
  id: 8150,
  startTime: dayjs('2025-07-16T16:34'),
  endTime: dayjs('2025-07-16T11:09'),
  status: 'CANCELLED_BY_CLINIC',
  reason: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewAppointment = {
  startTime: dayjs('2025-07-16T02:34'),
  endTime: dayjs('2025-07-16T18:42'),
  status: 'CANCELLED_BY_PATIENT',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
