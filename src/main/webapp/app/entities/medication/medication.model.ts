import dayjs from 'dayjs/esm';
import { RouteAdmin } from 'app/entities/enumerations/route-admin.model';
import { PrescriptionStatus } from 'app/entities/enumerations/prescription-status.model';
import { RiskLevel } from 'app/entities/enumerations/risk-level.model';

export interface IMedication {
  id: number;
  name?: string | null;
  internationalName?: string | null;
  codeAtc?: string | null;
  formulation?: string | null;
  strength?: string | null;
  routeOfAdministration?: keyof typeof RouteAdmin | null;
  manufacturer?: string | null;
  marketingAuthorizationNumber?: string | null;
  marketingAuthorizationDate?: dayjs.Dayjs | null;
  packaging?: string | null;
  prescriptionStatus?: keyof typeof PrescriptionStatus | null;
  description?: string | null;
  expiryDate?: dayjs.Dayjs | null;
  barcode?: string | null;
  storageCondition?: string | null;
  unitPrice?: number | null;
  image?: string | null;
  composition?: string | null;
  contraindications?: string | null;
  sideEffects?: string | null;
  active?: boolean | null;
  isGeneric?: boolean | null;
  riskLevel?: keyof typeof RiskLevel | null;
  createdBy?: string;
  createdDate?: Date;
  lastModifiedBy?: string;
  lastModifiedDate?: Date;
}

export type NewMedication = Omit<IMedication, 'id'> & { id: null };
