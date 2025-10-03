/* eslint-disable prettier/prettier */
import { LabTestMethod } from '../enumerations/lab-test-method.model';
import { LabTestType } from '../enumerations/lab-test-type.model';

export interface SearchCriteria {
  name?: string;
  type?: LabTestType;
  method?: LabTestMethod;
  active?: boolean;
  isLatestOnly?: boolean;
}
