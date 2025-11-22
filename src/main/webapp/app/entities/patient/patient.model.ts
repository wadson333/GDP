import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { Gender } from 'app/entities/enumerations/gender.model';
import { BloodType } from 'app/entities/enumerations/blood-type.model';
import { SmokingStatus } from 'app/entities/enumerations/smoking-status.model';
import { PatientStatus } from 'app/entities/enumerations/patient-status.model';

export interface IPatient {
  id: number;
  uid?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  birthDate?: dayjs.Dayjs | null;
  gender?: keyof typeof Gender | null;
  bloodType?: keyof typeof BloodType | null;
  address?: string | null;
  phone1?: string | null;
  phone2?: string | null;
  nif?: string | null;
  ninu?: string | null;
  medicalRecordNumber?: string | null;
  heightCm?: number | null;
  weightKg?: number | null;
  passportNumber?: string | null;
  contactPersonName?: string | null;
  contactPersonPhone?: string | null;
  antecedents?: string | null;
  allergies?: string | null;
  clinicalNotes?: string | null;
  smokingStatus?: keyof typeof SmokingStatus | null;
  gdprConsentDate?: dayjs.Dayjs | null;
  status?: keyof typeof PatientStatus | null;
  deceasedDate?: dayjs.Dayjs | null;
  insuranceCompanyName?: string | null;
  patientInsuranceId?: string | null;
  insurancePolicyNumber?: string | null;
  insuranceCoverageType?: string | null;
  insuranceValidFrom?: dayjs.Dayjs | null;
  insuranceValidTo?: dayjs.Dayjs | null;
  user?: IUser | null;
  createdBy?: string;
  createdDate?: Date;
  lastModifiedBy?: string;
  lastModifiedDate?: Date;
}

export type NewPatient = Omit<IPatient, 'id'> & { id: null };
