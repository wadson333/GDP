import dayjs from 'dayjs/esm';
import { IPatient } from 'app/entities/patient/patient.model';
import { IUser } from 'app/entities/user/user.model';
import { AppointmentStatus } from 'app/entities/enumerations/appointment-status.model';

export interface IAppointment {
  id: number;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  status?: keyof typeof AppointmentStatus | null;
  reason?: string | null;
  patient?: Pick<IPatient, 'id' | 'firstName'> | null;
  doctor?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewAppointment = Omit<IAppointment, 'id'> & { id: null };
