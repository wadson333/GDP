import dayjs from 'dayjs/esm';
import { IPatient } from 'app/entities/patient/patient.model';

export interface IMedicalDocument {
  id: number;
  documentName?: string | null;
  documentDate?: dayjs.Dayjs | null;
  filePath?: string | null;
  fileType?: string | null;
  desc?: string | null;
  patient?: Pick<IPatient, 'id' | 'firstName'> | null;
}

export type NewMedicalDocument = Omit<IMedicalDocument, 'id'> & { id: null };
