import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'd19d6b04-745d-438f-9a0b-4d9d8c01ed62',
};

export const sampleWithPartialData: IAuthority = {
  name: '0b07b44d-1c9c-453d-8895-b6cc8af13ea8',
};

export const sampleWithFullData: IAuthority = {
  name: '456aec77-a41d-4da9-a521-c0218bad6d33',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
