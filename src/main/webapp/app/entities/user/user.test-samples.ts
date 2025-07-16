import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 14291,
  login: 'eCGn5',
};

export const sampleWithPartialData: IUser = {
  id: 14962,
  login: 'h7@Yh2M\\Xb',
};

export const sampleWithFullData: IUser = {
  id: 32619,
  login: 'p',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
