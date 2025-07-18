import dayjs from 'dayjs/esm';
import { IPatient } from 'app/entities/patient/patient.model';
import { IUser } from 'app/entities/user/user.model';

export interface IHospitalization {
  id: number;
  admissionDate?: dayjs.Dayjs | null;
  dischargeDate?: dayjs.Dayjs | null;
  reason?: string | null;
  patient?: Pick<IPatient, 'id' | 'firstName'> | null;
  attendingDoctor?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewHospitalization = Omit<IHospitalization, 'id'> & { id: null };
