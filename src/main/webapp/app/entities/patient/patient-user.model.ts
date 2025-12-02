/* eslint-disable prettier/prettier */
import dayjs from 'dayjs/esm';
import { Gender } from 'app/entities/enumerations/gender.model';
import { BloodType } from 'app/entities/enumerations/blood-type.model';
import { SmokingStatus } from 'app/entities/enumerations/smoking-status.model';
import { PatientStatus } from 'app/entities/enumerations/patient-status.model';

export interface IPatientUser {
  // System Fields
  uid?: string | null;
  // User Fields
  login?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  langKey?: string | null;

  // Patient Basic Info
  birthDate?: dayjs.Dayjs | null;
  gender?: keyof typeof Gender | null;
  bloodType?: keyof typeof BloodType | null;
  status?: keyof typeof PatientStatus | null;

  // Contact Information
  address?: string | null;
  phone1?: string | null;
  phone2?: string | null;

  // Identity Documents
  nif?: string | null;
  ninu?: string | null;
  passportNumber?: string | null;

  // Physical Parameters
  heightCm?: number | null;
  weightKg?: number | null;

  // Emergency Contact
  contactPersonName?: string | null;
  contactPersonPhone?: string | null;

  // Medical Information
  antecedents?: string | null;
  allergies?: string | null;
  clinicalNotes?: string | null;
  smokingStatus?: keyof typeof SmokingStatus | null;

  // GDPR & Legal
  gdprConsentDate?: dayjs.Dayjs | null;
  deceasedDate?: dayjs.Dayjs | null;

  // Insurance Information
  insuranceCompanyName?: string | null;
  patientInsuranceId?: string | null;
  insurancePolicyNumber?: string | null;
  insuranceCoverageType?: string | null;
  insuranceValidFrom?: dayjs.Dayjs | null;
  insuranceValidTo?: dayjs.Dayjs | null;

  // Control Flags
  sendActivationEmail?: boolean;
  activatedOnCreate?: boolean;
}

export type NewPatientUser = Omit<IPatientUser, 'uid'>;
