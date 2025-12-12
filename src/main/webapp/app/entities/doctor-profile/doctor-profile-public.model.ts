/* eslint-disable prettier/prettier */
import { MedicalSpecialty } from '../enumerations/medical-specialty.model';

/**
 * Public DTO for doctor profiles (safe, non-sensitive information).
 */
export interface IDoctorPublic {
  uid?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  primarySpecialty?: keyof typeof MedicalSpecialty | null;
  otherSpecialties?: string | null;
  bio?: string | null;
  spokenLanguages?: string | null;
  university?: string | null;
  consultationFee?: number | null;
  teleconsultationFee?: number | null;
  officeAddress?: string | null;
  officePhone?: string | null;
  averageRating?: number | null;
  reviewCount?: number | null;
  photoUrl?: string | null;
}
