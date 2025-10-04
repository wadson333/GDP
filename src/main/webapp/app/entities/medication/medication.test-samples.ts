import dayjs from 'dayjs/esm';

import { IMedication, NewMedication } from './medication.model';

export const sampleWithRequiredData: IMedication = {
  id: 7368,
  name: 'adorable maigre',
  routeOfAdministration: 'OTHER',
  prescriptionStatus: 'OTC',
  active: false,
};

export const sampleWithPartialData: IMedication = {
  id: 21298,
  name: 'ding en plus de apte',
  internationalName: 'sans doute au-dessus de corner',
  codeAtc: 'parlementaire advers',
  formulation: 'premièrement amener innombrable',
  strength: 'équipe',
  routeOfAdministration: 'INTRAVENOUS',
  marketingAuthorizationNumber: 'large',
  marketingAuthorizationDate: dayjs('2025-07-15T22:13'),
  prescriptionStatus: 'OTC',
  description: 'couvrir drelin jeune enfant',
  barcode: 'ferme équipe de recherche',
  storageCondition: 'cuicui loufoque à cause de',
  contraindications: 'toc-toc',
  active: true,
  isGeneric: false,
};

export const sampleWithFullData: IMedication = {
  id: 2314,
  name: 'aux alentours de en faveur de moderne',
  internationalName: 'porte-parole glouglou',
  codeAtc: 'à peu près',
  formulation: 'atchoum de la part de',
  strength: 'tant recourir',
  routeOfAdministration: 'OTHER',
  manufacturer: 'si rigoler conseil d’administration',
  marketingAuthorizationNumber: 'malade gâcher extra',
  marketingAuthorizationDate: dayjs('2025-07-16T03:54'),
  packaging: 'insuffisamment pff',
  prescriptionStatus: 'HOSPITAL_USE_ONLY',
  description: 'insipide cadre',
  expiryDate: dayjs('2025-07-16T14:23'),
  barcode: 'toc-toc',
  storageCondition: 'triathlète combattre',
  unitPrice: 30555.44,
  image: 'administration mairie lectorat',
  composition: 'équipe ci',
  contraindications: 'constituer',
  sideEffects: 'sauvage gestionnaire',
  active: true,
  isGeneric: true,
  riskLevel: 'UNKNOWN',
};

export const sampleWithNewData: NewMedication = {
  name: 'patientèle pourvu que en dehors de',
  routeOfAdministration: 'OTHER',
  prescriptionStatus: 'HOSPITAL_USE_ONLY',
  active: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
