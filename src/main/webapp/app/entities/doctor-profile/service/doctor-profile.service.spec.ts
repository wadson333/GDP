import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IDoctorProfile } from '../doctor-profile.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../doctor-profile.test-samples';

import { DoctorProfileService, RestDoctorProfile } from './doctor-profile.service';

const requireRestSample: RestDoctorProfile = {
  ...sampleWithRequiredData,
  startDateOfPractice: sampleWithRequiredData.startDateOfPractice?.format(DATE_FORMAT),
};

describe('DoctorProfile Service', () => {
  let service: DoctorProfileService;
  let httpMock: HttpTestingController;
  let expectedResult: IDoctorProfile | IDoctorProfile[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DoctorProfileService);
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

    it('should create a DoctorProfile', () => {
      const doctorProfile = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(doctorProfile).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DoctorProfile', () => {
      const doctorProfile = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(doctorProfile).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DoctorProfile', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DoctorProfile', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DoctorProfile', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDoctorProfileToCollectionIfMissing', () => {
      it('should add a DoctorProfile to an empty array', () => {
        const doctorProfile: IDoctorProfile = sampleWithRequiredData;
        expectedResult = service.addDoctorProfileToCollectionIfMissing([], doctorProfile);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(doctorProfile);
      });

      it('should not add a DoctorProfile to an array that contains it', () => {
        const doctorProfile: IDoctorProfile = sampleWithRequiredData;
        const doctorProfileCollection: IDoctorProfile[] = [
          {
            ...doctorProfile,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDoctorProfileToCollectionIfMissing(doctorProfileCollection, doctorProfile);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DoctorProfile to an array that doesn't contain it", () => {
        const doctorProfile: IDoctorProfile = sampleWithRequiredData;
        const doctorProfileCollection: IDoctorProfile[] = [sampleWithPartialData];
        expectedResult = service.addDoctorProfileToCollectionIfMissing(doctorProfileCollection, doctorProfile);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(doctorProfile);
      });

      it('should add only unique DoctorProfile to an array', () => {
        const doctorProfileArray: IDoctorProfile[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const doctorProfileCollection: IDoctorProfile[] = [sampleWithRequiredData];
        expectedResult = service.addDoctorProfileToCollectionIfMissing(doctorProfileCollection, ...doctorProfileArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const doctorProfile: IDoctorProfile = sampleWithRequiredData;
        const doctorProfile2: IDoctorProfile = sampleWithPartialData;
        expectedResult = service.addDoctorProfileToCollectionIfMissing([], doctorProfile, doctorProfile2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(doctorProfile);
        expect(expectedResult).toContain(doctorProfile2);
      });

      it('should accept null and undefined values', () => {
        const doctorProfile: IDoctorProfile = sampleWithRequiredData;
        expectedResult = service.addDoctorProfileToCollectionIfMissing([], null, doctorProfile, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(doctorProfile);
      });

      it('should return initial array if no DoctorProfile is added', () => {
        const doctorProfileCollection: IDoctorProfile[] = [sampleWithRequiredData];
        expectedResult = service.addDoctorProfileToCollectionIfMissing(doctorProfileCollection, undefined, null);
        expect(expectedResult).toEqual(doctorProfileCollection);
      });
    });

    describe('compareDoctorProfile', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDoctorProfile(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDoctorProfile(entity1, entity2);
        const compareResult2 = service.compareDoctorProfile(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDoctorProfile(entity1, entity2);
        const compareResult2 = service.compareDoctorProfile(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDoctorProfile(entity1, entity2);
        const compareResult2 = service.compareDoctorProfile(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
