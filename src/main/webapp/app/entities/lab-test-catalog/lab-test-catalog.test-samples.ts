import dayjs from 'dayjs/esm';

import { ILabTestCatalog, NewLabTestCatalog } from './lab-test-catalog.model';

export const sampleWithRequiredData: ILabTestCatalog = {
  id: 25063,
  name: 'touriste direction',
  unit: 'imiter comme parce que',
  version: 13745,
};

export const sampleWithPartialData: ILabTestCatalog = {
  id: 16186,
  name: 'prout disparaître',
  unit: "d'avec turquoise loin de",
  description: 'en plus de grâce à vorace',
  version: 25670,
  validFrom: dayjs('2025-07-16T07:42'),
  validTo: dayjs('2025-07-16T11:19'),
  sampleType: 'OTHER',
  referenceRangeHigh: 1953.81,
  active: true,
  loincCode: 'au point que tandis que',
  cost: 25307.04,
};

export const sampleWithFullData: ILabTestCatalog = {
  id: 3932,
  name: 'commis de cuisine sage',
  unit: 'dense dormir',
  description: 'exagérer dominer',
  version: 14391,
  validFrom: dayjs('2025-07-16T12:56'),
  validTo: dayjs('2025-07-16T04:15'),
  method: 'SPECTROMETRY',
  sampleType: 'OTHER',
  referenceRangeLow: 11391.58,
  referenceRangeHigh: 13507.69,
  active: false,
  type: 'BIOCHEMISTRY',
  loincCode: 'malgré',
  cost: 21154.74,
  turnaroundTime: 14123,
};

export const sampleWithNewData: NewLabTestCatalog = {
  name: 'en outre de deçà',
  unit: 'partout loin de émérite',
  version: 625,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
