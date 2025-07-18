import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPrescriptionItem } from '../prescription-item.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../prescription-item.test-samples';

import { PrescriptionItemService } from './prescription-item.service';

const requireRestSample: IPrescriptionItem = {
  ...sampleWithRequiredData,
};

describe('PrescriptionItem Service', () => {
  let service: PrescriptionItemService;
  let httpMock: HttpTestingController;
  let expectedResult: IPrescriptionItem | IPrescriptionItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PrescriptionItemService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a PrescriptionItem', () => {
      const prescriptionItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(prescriptionItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PrescriptionItem', () => {
      const prescriptionItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(prescriptionItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PrescriptionItem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PrescriptionItem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PrescriptionItem', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPrescriptionItemToCollectionIfMissing', () => {
      it('should add a PrescriptionItem to an empty array', () => {
        const prescriptionItem: IPrescriptionItem = sampleWithRequiredData;
        expectedResult = service.addPrescriptionItemToCollectionIfMissing([], prescriptionItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prescriptionItem);
      });

      it('should not add a PrescriptionItem to an array that contains it', () => {
        const prescriptionItem: IPrescriptionItem = sampleWithRequiredData;
        const prescriptionItemCollection: IPrescriptionItem[] = [
          {
            ...prescriptionItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPrescriptionItemToCollectionIfMissing(prescriptionItemCollection, prescriptionItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PrescriptionItem to an array that doesn't contain it", () => {
        const prescriptionItem: IPrescriptionItem = sampleWithRequiredData;
        const prescriptionItemCollection: IPrescriptionItem[] = [sampleWithPartialData];
        expectedResult = service.addPrescriptionItemToCollectionIfMissing(prescriptionItemCollection, prescriptionItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prescriptionItem);
      });

      it('should add only unique PrescriptionItem to an array', () => {
        const prescriptionItemArray: IPrescriptionItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const prescriptionItemCollection: IPrescriptionItem[] = [sampleWithRequiredData];
        expectedResult = service.addPrescriptionItemToCollectionIfMissing(prescriptionItemCollection, ...prescriptionItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const prescriptionItem: IPrescriptionItem = sampleWithRequiredData;
        const prescriptionItem2: IPrescriptionItem = sampleWithPartialData;
        expectedResult = service.addPrescriptionItemToCollectionIfMissing([], prescriptionItem, prescriptionItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(prescriptionItem);
        expect(expectedResult).toContain(prescriptionItem2);
      });

      it('should accept null and undefined values', () => {
        const prescriptionItem: IPrescriptionItem = sampleWithRequiredData;
        expectedResult = service.addPrescriptionItemToCollectionIfMissing([], null, prescriptionItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(prescriptionItem);
      });

      it('should return initial array if no PrescriptionItem is added', () => {
        const prescriptionItemCollection: IPrescriptionItem[] = [sampleWithRequiredData];
        expectedResult = service.addPrescriptionItemToCollectionIfMissing(prescriptionItemCollection, undefined, null);
        expect(expectedResult).toEqual(prescriptionItemCollection);
      });
    });

    describe('comparePrescriptionItem', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePrescriptionItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePrescriptionItem(entity1, entity2);
        const compareResult2 = service.comparePrescriptionItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePrescriptionItem(entity1, entity2);
        const compareResult2 = service.comparePrescriptionItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePrescriptionItem(entity1, entity2);
        const compareResult2 = service.comparePrescriptionItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
