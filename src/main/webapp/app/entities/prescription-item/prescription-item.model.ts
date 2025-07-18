import { IMedication } from 'app/entities/medication/medication.model';
import { IPrescription } from 'app/entities/prescription/prescription.model';

export interface IPrescriptionItem {
  id: number;
  dosage?: string | null;
  frequency?: string | null;
  duration?: string | null;
  medication?: Pick<IMedication, 'id' | 'name'> | null;
  prescription?: Pick<IPrescription, 'id'> | null;
}

export type NewPrescriptionItem = Omit<IPrescriptionItem, 'id'> & { id: null };
