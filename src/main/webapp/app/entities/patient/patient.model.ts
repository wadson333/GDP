import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { Gender } from 'app/entities/enumerations/gender.model';

export interface IPatient {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  birthDate?: dayjs.Dayjs | null;
  gender?: keyof typeof Gender | null;
  bloodType?: string | null;
  address?: string | null;
  phone1?: string | null;
  phone2?: string | null;
  email?: string | null;
  nif?: string | null;
  ninu?: string | null;
  heightCm?: number | null;
  weightKg?: number | null;
  passportNumber?: string | null;
  contactPersonName?: string | null;
  contactPersonPhone?: string | null;
  antecedents?: string | null;
  allergies?: string | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewPatient = Omit<IPatient, 'id'> & { id: null };
