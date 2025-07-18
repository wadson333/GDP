import dayjs from 'dayjs/esm';

import { IMedicalDocument, NewMedicalDocument } from './medical-document.model';

export const sampleWithRequiredData: IMedicalDocument = {
  id: 201,
  documentName: 'entre',
  filePath: 'entièrement',
  fileType: "touriste d'après",
};

export const sampleWithPartialData: IMedicalDocument = {
  id: 14170,
  documentName: 'gai administration plic',
  filePath: 'dans la mesure où rédiger',
  fileType: 'minuscule',
  desc: 'complètement prout agréable',
};

export const sampleWithFullData: IMedicalDocument = {
  id: 4924,
  documentName: 'coin-coin',
  documentDate: dayjs('2025-07-16'),
  filePath: 'extatique',
  fileType: 'garantir considérable',
  desc: 'sans doute',
};

export const sampleWithNewData: NewMedicalDocument = {
  documentName: 'pour que accommoder',
  filePath: 'tellement',
  fileType: 'déceler avant que propre',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
