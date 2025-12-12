/* eslint-disable prettier/prettier */

import dayjs from 'dayjs';
import { BloodType } from '../enumerations/blood-type.model';
import { Gender } from '../enumerations/gender.model';
import { MedicalSpecialty } from '../enumerations/medical-specialty.model';

/**
 * Flattened interface combining User and DoctorProfile fields.
 * Used for atomic creation and update of Doctor+User.
 * Matches backend DoctorProfileUserDTO.
 */
export interface IDoctorProfileUser {
  // Read-only fields (populated on read for edit)
  uid?: string | null;
  codeClinic?: string | null;

  // User fields
  login: string;
  email: string;
  firstName?: string | null;
  lastName?: string | null;
  langKey?: string | null;
  sendActivationEmail?: boolean;
  activatedOnCreate?: boolean;

  // DoctorProfile fields (uid and codeClinic are generated on backend)
  medicalLicenseNumber: string;
  birthDate: dayjs.Dayjs;
  gender?: keyof typeof Gender | null;
  bloodType?: keyof typeof BloodType | null;
  primarySpecialty: keyof typeof MedicalSpecialty;
  otherSpecialties?: string | null;
  university?: string | null;
  graduationYear?: number | null;
  startDateOfPractice: dayjs.Dayjs;
  consultationDurationMinutes: number;
  acceptingNewPatients: boolean;
  allowsTeleconsultation: boolean;
  consultationFee?: number | null;
  teleconsultationFee?: number | null;
  bio?: string | null;
  spokenLanguages?: string | null;
  websiteUrl?: string | null;
  officePhone?: string | null;
  officeAddress?: string | null;
  nif?: string | null;
  ninu?: string | null;
}
