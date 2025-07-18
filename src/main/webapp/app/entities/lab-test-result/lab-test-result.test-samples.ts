import dayjs from 'dayjs/esm';

import { ILabTestResult, NewLabTestResult } from './lab-test-result.model';

export const sampleWithRequiredData: ILabTestResult = {
  id: 1044,
  resultValue: 22828.95,
  resultDate: dayjs('2025-07-16'),
  isAbnormal: false,
};

export const sampleWithPartialData: ILabTestResult = {
  id: 11752,
  resultValue: 9143.63,
  resultDate: dayjs('2025-07-16'),
  isAbnormal: false,
};

export const sampleWithFullData: ILabTestResult = {
  id: 13636,
  resultValue: 23109.61,
  resultDate: dayjs('2025-07-16'),
  isAbnormal: false,
};

export const sampleWithNewData: NewLabTestResult = {
  resultValue: 17166.76,
  resultDate: dayjs('2025-07-16'),
  isAbnormal: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
