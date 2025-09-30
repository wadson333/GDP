import dayjs from 'dayjs/esm';
import { LabTestMethod } from 'app/entities/enumerations/lab-test-method.model';
import { SampleType } from 'app/entities/enumerations/sample-type.model';
import { LabTestType } from 'app/entities/enumerations/lab-test-type.model';

export interface ILabTestCatalog {
  id: number;
  name?: string | null;
  unit?: string | null;
  description?: string | null;
  version?: number | null;
  validFrom?: dayjs.Dayjs | null;
  validTo?: dayjs.Dayjs | null;
  method?: keyof typeof LabTestMethod | null;
  sampleType?: keyof typeof SampleType | null;
  referenceRangeLow?: number | null;
  referenceRangeHigh?: number | null;
  active?: boolean | null;
  type?: keyof typeof LabTestType | null;
  loincCode?: string | null;
  cost?: number | null;
  turnaroundTime?: number | null;
}

export type NewLabTestCatalog = Omit<ILabTestCatalog, 'id'> & { id: null };
