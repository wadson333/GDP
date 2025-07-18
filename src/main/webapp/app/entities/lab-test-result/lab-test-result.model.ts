import dayjs from 'dayjs/esm';
import { IPatient } from 'app/entities/patient/patient.model';
import { ILabTestCatalog } from 'app/entities/lab-test-catalog/lab-test-catalog.model';
import { IConsultation } from 'app/entities/consultation/consultation.model';

export interface ILabTestResult {
  id: number;
  resultValue?: number | null;
  resultDate?: dayjs.Dayjs | null;
  isAbnormal?: boolean | null;
  patient?: Pick<IPatient, 'id' | 'firstName'> | null;
  labTest?: Pick<ILabTestCatalog, 'id' | 'name'> | null;
  consultation?: Pick<IConsultation, 'id'> | null;
}

export type NewLabTestResult = Omit<ILabTestResult, 'id'> & { id: null };
