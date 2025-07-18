import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IUserConfiguration } from '../user-configuration.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../user-configuration.test-samples';

import { UserConfigurationService } from './user-configuration.service';

const requireRestSample: IUserConfiguration = {
  ...sampleWithRequiredData,
};

describe('UserConfiguration Service', () => {
  let service: UserConfigurationService;
  let httpMock: HttpTestingController;
  let expectedResult: IUserConfiguration | IUserConfiguration[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(UserConfigurationService);
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

    it('should create a UserConfiguration', () => {
      const userConfiguration = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(userConfiguration).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserConfiguration', () => {
      const userConfiguration = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(userConfiguration).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UserConfiguration', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserConfiguration', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UserConfiguration', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUserConfigurationToCollectionIfMissing', () => {
      it('should add a UserConfiguration to an empty array', () => {
        const userConfiguration: IUserConfiguration = sampleWithRequiredData;
        expectedResult = service.addUserConfigurationToCollectionIfMissing([], userConfiguration);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userConfiguration);
      });

      it('should not add a UserConfiguration to an array that contains it', () => {
        const userConfiguration: IUserConfiguration = sampleWithRequiredData;
        const userConfigurationCollection: IUserConfiguration[] = [
          {
            ...userConfiguration,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUserConfigurationToCollectionIfMissing(userConfigurationCollection, userConfiguration);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserConfiguration to an array that doesn't contain it", () => {
        const userConfiguration: IUserConfiguration = sampleWithRequiredData;
        const userConfigurationCollection: IUserConfiguration[] = [sampleWithPartialData];
        expectedResult = service.addUserConfigurationToCollectionIfMissing(userConfigurationCollection, userConfiguration);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userConfiguration);
      });

      it('should add only unique UserConfiguration to an array', () => {
        const userConfigurationArray: IUserConfiguration[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const userConfigurationCollection: IUserConfiguration[] = [sampleWithRequiredData];
        expectedResult = service.addUserConfigurationToCollectionIfMissing(userConfigurationCollection, ...userConfigurationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userConfiguration: IUserConfiguration = sampleWithRequiredData;
        const userConfiguration2: IUserConfiguration = sampleWithPartialData;
        expectedResult = service.addUserConfigurationToCollectionIfMissing([], userConfiguration, userConfiguration2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userConfiguration);
        expect(expectedResult).toContain(userConfiguration2);
      });

      it('should accept null and undefined values', () => {
        const userConfiguration: IUserConfiguration = sampleWithRequiredData;
        expectedResult = service.addUserConfigurationToCollectionIfMissing([], null, userConfiguration, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userConfiguration);
      });

      it('should return initial array if no UserConfiguration is added', () => {
        const userConfigurationCollection: IUserConfiguration[] = [sampleWithRequiredData];
        expectedResult = service.addUserConfigurationToCollectionIfMissing(userConfigurationCollection, undefined, null);
        expect(expectedResult).toEqual(userConfigurationCollection);
      });
    });

    describe('compareUserConfiguration', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUserConfiguration(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUserConfiguration(entity1, entity2);
        const compareResult2 = service.compareUserConfiguration(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUserConfiguration(entity1, entity2);
        const compareResult2 = service.compareUserConfiguration(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUserConfiguration(entity1, entity2);
        const compareResult2 = service.compareUserConfiguration(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
