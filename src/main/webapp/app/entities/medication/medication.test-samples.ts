import { IMedication, NewMedication } from './medication.model';

export const sampleWithRequiredData: IMedication = {
  id: 17113,
  name: 'cadre insuffisamment',
};

export const sampleWithPartialData: IMedication = {
  id: 14188,
  name: 'en guise de ah',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IMedication = {
  id: 11095,
  name: "d'entre tendre",
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewMedication = {
  name: 'alentour volontiers quoique',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
