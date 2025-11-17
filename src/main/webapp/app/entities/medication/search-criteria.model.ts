/* eslint-disable prettier/prettier */
import { PrescriptionStatus } from '../enumerations/prescription-status.model';
import { RiskLevel } from '../enumerations/risk-level.model';
import { RouteAdmin } from '../enumerations/route-admin.model';

// Add this interface above the component
export interface SearchCriteria {
  name?: string;
  codeAtc?: string;
  formulation?: string;
  strength?: string;
  manufacturer?: string;
  unitPriceMin?: number;
  unitPriceMax?: number;
  active?: boolean;
  routeOfAdministration?: RouteAdmin;
  prescriptionStatus?: PrescriptionStatus;
  riskLevel?: RiskLevel;
}
