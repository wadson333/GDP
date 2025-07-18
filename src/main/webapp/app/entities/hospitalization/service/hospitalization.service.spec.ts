import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IHospitalization } from '../hospitalization.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../hospitalization.test-samples';

import { HospitalizationService, RestHospitalization } from './hospitalization.service';

const requireRestSample: RestHospitalization = {
  ...sampleWithRequiredData,
  admissionDate: sampleWithRequiredData.admissionDate?.toJSON(),
  dischargeDate: sampleWithRequiredData.dischargeDate?.toJSON(),
};

describe('Hospitalization Service', () => {
  let service: HospitalizationService;
  let httpMock: HttpTestingController;
  let expectedResult: IHospitalization | IHospitalization[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(HospitalizationService);
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

    it('should create a Hospitalization', () => {
      const hospitalization = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(hospitalization).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Hospitalization', () => {
      const hospitalization = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(hospitalization).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Hospitalization', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Hospitalization', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Hospitalization', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addHospitalizationToCollectionIfMissing', () => {
      it('should add a Hospitalization to an empty array', () => {
        const hospitalization: IHospitalization = sampleWithRequiredData;
        expectedResult = service.addHospitalizationToCollectionIfMissing([], hospitalization);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hospitalization);
      });

      it('should not add a Hospitalization to an array that contains it', () => {
        const hospitalization: IHospitalization = sampleWithRequiredData;
        const hospitalizationCollection: IHospitalization[] = [
          {
            ...hospitalization,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHospitalizationToCollectionIfMissing(hospitalizationCollection, hospitalization);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Hospitalization to an array that doesn't contain it", () => {
        const hospitalization: IHospitalization = sampleWithRequiredData;
        const hospitalizationCollection: IHospitalization[] = [sampleWithPartialData];
        expectedResult = service.addHospitalizationToCollectionIfMissing(hospitalizationCollection, hospitalization);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hospitalization);
      });

      it('should add only unique Hospitalization to an array', () => {
        const hospitalizationArray: IHospitalization[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const hospitalizationCollection: IHospitalization[] = [sampleWithRequiredData];
        expectedResult = service.addHospitalizationToCollectionIfMissing(hospitalizationCollection, ...hospitalizationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const hospitalization: IHospitalization = sampleWithRequiredData;
        const hospitalization2: IHospitalization = sampleWithPartialData;
        expectedResult = service.addHospitalizationToCollectionIfMissing([], hospitalization, hospitalization2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hospitalization);
        expect(expectedResult).toContain(hospitalization2);
      });

      it('should accept null and undefined values', () => {
        const hospitalization: IHospitalization = sampleWithRequiredData;
        expectedResult = service.addHospitalizationToCollectionIfMissing([], null, hospitalization, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hospitalization);
      });

      it('should return initial array if no Hospitalization is added', () => {
        const hospitalizationCollection: IHospitalization[] = [sampleWithRequiredData];
        expectedResult = service.addHospitalizationToCollectionIfMissing(hospitalizationCollection, undefined, null);
        expect(expectedResult).toEqual(hospitalizationCollection);
      });
    });

    describe('compareHospitalization', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHospitalization(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareHospitalization(entity1, entity2);
        const compareResult2 = service.compareHospitalization(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareHospitalization(entity1, entity2);
        const compareResult2 = service.compareHospitalization(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareHospitalization(entity1, entity2);
        const compareResult2 = service.compareHospitalization(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
