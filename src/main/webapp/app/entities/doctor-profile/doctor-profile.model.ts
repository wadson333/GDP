import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IDoctorProfile {
  id: number;
  specialty?: string | null;
  licenseNumber?: string | null;
  university?: string | null;
  startDateOfPractice?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewDoctorProfile = Omit<IDoctorProfile, 'id'> & { id: null };
