import dayjs from 'dayjs/esm';

import { INotification, NewNotification } from './notification.model';

export const sampleWithRequiredData: INotification = {
  id: 4930,
  message: '../fake-data/blob/hipster.txt',
  isRead: false,
  notificationType: 'APPOINTMENT_CONFIRMED',
  creationDate: dayjs('2025-07-16T18:03'),
};

export const sampleWithPartialData: INotification = {
  id: 14327,
  message: '../fake-data/blob/hipster.txt',
  isRead: true,
  notificationType: 'NEW_DOCUMENT',
  creationDate: dayjs('2025-07-15T21:18'),
  relatedEntityId: 331,
};

export const sampleWithFullData: INotification = {
  id: 1479,
  message: '../fake-data/blob/hipster.txt',
  isRead: false,
  notificationType: 'NEW_DOCUMENT',
  creationDate: dayjs('2025-07-16T05:49'),
  relatedEntityId: 20939,
};

export const sampleWithNewData: NewNotification = {
  message: '../fake-data/blob/hipster.txt',
  isRead: false,
  notificationType: 'APPOINTMENT_REMINDER',
  creationDate: dayjs('2025-07-16T19:06'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
