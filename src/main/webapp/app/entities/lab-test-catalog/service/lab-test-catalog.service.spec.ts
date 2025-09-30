import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ILabTestCatalog } from '../lab-test-catalog.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../lab-test-catalog.test-samples';

import { LabTestCatalogService, RestLabTestCatalog } from './lab-test-catalog.service';

const requireRestSample: RestLabTestCatalog = {
  ...sampleWithRequiredData,
  validFrom: sampleWithRequiredData.validFrom?.toJSON(),
  validTo: sampleWithRequiredData.validTo?.toJSON(),
};

describe('LabTestCatalog Service', () => {
  let service: LabTestCatalogService;
  let httpMock: HttpTestingController;
  let expectedResult: ILabTestCatalog | ILabTestCatalog[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(LabTestCatalogService);
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

    it('should create a LabTestCatalog', () => {
      const labTestCatalog = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(labTestCatalog).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LabTestCatalog', () => {
      const labTestCatalog = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(labTestCatalog).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LabTestCatalog', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LabTestCatalog', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a LabTestCatalog', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLabTestCatalogToCollectionIfMissing', () => {
      it('should add a LabTestCatalog to an empty array', () => {
        const labTestCatalog: ILabTestCatalog = sampleWithRequiredData;
        expectedResult = service.addLabTestCatalogToCollectionIfMissing([], labTestCatalog);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(labTestCatalog);
      });

      it('should not add a LabTestCatalog to an array that contains it', () => {
        const labTestCatalog: ILabTestCatalog = sampleWithRequiredData;
        const labTestCatalogCollection: ILabTestCatalog[] = [
          {
            ...labTestCatalog,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLabTestCatalogToCollectionIfMissing(labTestCatalogCollection, labTestCatalog);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LabTestCatalog to an array that doesn't contain it", () => {
        const labTestCatalog: ILabTestCatalog = sampleWithRequiredData;
        const labTestCatalogCollection: ILabTestCatalog[] = [sampleWithPartialData];
        expectedResult = service.addLabTestCatalogToCollectionIfMissing(labTestCatalogCollection, labTestCatalog);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(labTestCatalog);
      });

      it('should add only unique LabTestCatalog to an array', () => {
        const labTestCatalogArray: ILabTestCatalog[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const labTestCatalogCollection: ILabTestCatalog[] = [sampleWithRequiredData];
        expectedResult = service.addLabTestCatalogToCollectionIfMissing(labTestCatalogCollection, ...labTestCatalogArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const labTestCatalog: ILabTestCatalog = sampleWithRequiredData;
        const labTestCatalog2: ILabTestCatalog = sampleWithPartialData;
        expectedResult = service.addLabTestCatalogToCollectionIfMissing([], labTestCatalog, labTestCatalog2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(labTestCatalog);
        expect(expectedResult).toContain(labTestCatalog2);
      });

      it('should accept null and undefined values', () => {
        const labTestCatalog: ILabTestCatalog = sampleWithRequiredData;
        expectedResult = service.addLabTestCatalogToCollectionIfMissing([], null, labTestCatalog, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(labTestCatalog);
      });

      it('should return initial array if no LabTestCatalog is added', () => {
        const labTestCatalogCollection: ILabTestCatalog[] = [sampleWithRequiredData];
        expectedResult = service.addLabTestCatalogToCollectionIfMissing(labTestCatalogCollection, undefined, null);
        expect(expectedResult).toEqual(labTestCatalogCollection);
      });
    });

    describe('compareLabTestCatalog', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLabTestCatalog(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareLabTestCatalog(entity1, entity2);
        const compareResult2 = service.compareLabTestCatalog(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareLabTestCatalog(entity1, entity2);
        const compareResult2 = service.compareLabTestCatalog(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareLabTestCatalog(entity1, entity2);
        const compareResult2 = service.compareLabTestCatalog(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
