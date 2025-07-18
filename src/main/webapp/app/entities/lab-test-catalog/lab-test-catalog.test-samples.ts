import { ILabTestCatalog, NewLabTestCatalog } from './lab-test-catalog.model';

export const sampleWithRequiredData: ILabTestCatalog = {
  id: 25855,
  name: 'fourbe',
  unit: 'coac coac',
};

export const sampleWithPartialData: ILabTestCatalog = {
  id: 10990,
  name: 'adepte céans zzzz',
  unit: 'chef de cuisine demain',
  referenceRangeLow: 17685.92,
  referenceRangeHigh: 1673.35,
};

export const sampleWithFullData: ILabTestCatalog = {
  id: 6483,
  name: 'pacifique',
  unit: 'tant que minuscule alors que',
  referenceRangeLow: 14285.95,
  referenceRangeHigh: 3889.39,
};

export const sampleWithNewData: NewLabTestCatalog = {
  name: 'garder mal vraiment',
  unit: 'fouiller hors de',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
