import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { Gender } from 'app/entities/enumerations/gender.model';
import { BloodType } from 'app/entities/enumerations/blood-type.model';
import { MedicalSpecialty } from 'app/entities/enumerations/medical-specialty.model';
import { DoctorStatus } from 'app/entities/enumerations/doctor-status.model';

export interface IDoctorProfile {
  id: number;
  codeClinic?: string | null;
  uid?: string | null;
  medicalLicenseNumber?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  birthDate?: dayjs.Dayjs | null;
  gender?: keyof typeof Gender | null;
  bloodType?: keyof typeof BloodType | null;
  primarySpecialty?: keyof typeof MedicalSpecialty | null;
  otherSpecialties?: string | null;
  university?: string | null;
  graduationYear?: number | null;
  startDateOfPractice?: dayjs.Dayjs | null;
  consultationDurationMinutes?: number | null;
  acceptingNewPatients?: boolean | null;
  allowsTeleconsultation?: boolean | null;
  consultationFee?: number | null;
  teleconsultationFee?: number | null;
  bio?: string | null;
  spokenLanguages?: string | null;
  websiteUrl?: string | null;
  officePhone?: string | null;
  officeAddress?: string | null;
  status?: keyof typeof DoctorStatus | null;
  isVerified?: boolean | null;
  verifiedAt?: dayjs.Dayjs | null;
  nif?: string | null;
  ninu?: string | null;
  averageRating?: number | null;
  reviewCount?: number | null;
  version?: number | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
  createdBy?: string;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewDoctorProfile = Omit<IDoctorProfile, 'id'> & { id: null };
