export interface ILabTestCatalog {
  id: number;
  name?: string | null;
  unit?: string | null;
  referenceRangeLow?: number | null;
  referenceRangeHigh?: number | null;
}

export type NewLabTestCatalog = Omit<ILabTestCatalog, 'id'> & { id: null };
