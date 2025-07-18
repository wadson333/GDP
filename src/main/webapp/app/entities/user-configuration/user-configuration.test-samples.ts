import { IUserConfiguration, NewUserConfiguration } from './user-configuration.model';

export const sampleWithRequiredData: IUserConfiguration = {
  id: 30251,
  twoFactorEnabled: false,
  receiveEmailNotifs: true,
};

export const sampleWithPartialData: IUserConfiguration = {
  id: 31657,
  twoFactorEnabled: false,
  twoFactorSecret: 'au moyen de arrière mal',
  receiveEmailNotifs: true,
};

export const sampleWithFullData: IUserConfiguration = {
  id: 16845,
  twoFactorEnabled: true,
  twoFactorSecret: 'horrible vorace prestataire de services',
  receiveEmailNotifs: false,
};

export const sampleWithNewData: NewUserConfiguration = {
  twoFactorEnabled: false,
  receiveEmailNotifs: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
