export interface IMedication {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewMedication = Omit<IMedication, 'id'> & { id: null };
