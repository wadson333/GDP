/* eslint-disable prettier/prettier */
/**
 * DTO for doctor verification (approval/rejection).
 */
export interface IDoctorVerification {
  approved: boolean;
  comment?: string | null;
}
