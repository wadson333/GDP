import dayjs from 'dayjs/esm';
import { IPrescription } from 'app/entities/prescription/prescription.model';
import { IUser } from 'app/entities/user/user.model';
import { IPatient } from 'app/entities/patient/patient.model';

export interface IConsultation {
  id: number;
  consultationDate?: dayjs.Dayjs | null;
  symptoms?: string | null;
  diagnosis?: string | null;
  prescription?: Pick<IPrescription, 'id'> | null;
  doctor?: Pick<IUser, 'id' | 'login'> | null;
  patient?: Pick<IPatient, 'id' | 'firstName'> | null;
}

export type NewConsultation = Omit<IConsultation, 'id'> & { id: null };
