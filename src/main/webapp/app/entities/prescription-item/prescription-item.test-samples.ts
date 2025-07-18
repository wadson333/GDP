import { IPrescriptionItem, NewPrescriptionItem } from './prescription-item.model';

export const sampleWithRequiredData: IPrescriptionItem = {
  id: 31244,
  frequency: 'mal',
};

export const sampleWithPartialData: IPrescriptionItem = {
  id: 6752,
  frequency: 'miam badaboum',
  duration: 'aussitôt que prout',
};

export const sampleWithFullData: IPrescriptionItem = {
  id: 10873,
  dosage: 'menacer gémir en face de',
  frequency: 'recommander foule smack',
  duration: 'alentour',
};

export const sampleWithNewData: NewPrescriptionItem = {
  frequency: 'mince tant chanter',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
