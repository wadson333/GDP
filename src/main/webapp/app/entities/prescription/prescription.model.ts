import dayjs from 'dayjs/esm';

export interface IPrescription {
  id: number;
  prescriptionDate?: dayjs.Dayjs | null;
}

export type NewPrescription = Omit<IPrescription, 'id'> & { id: null };
