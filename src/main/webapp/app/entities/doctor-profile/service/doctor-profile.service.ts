/* eslint-disable @typescript-eslint/no-unnecessary-condition */
import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDoctorProfile, NewDoctorProfile } from '../doctor-profile.model';
import { IDoctorPublic } from '../doctor-profile-public.model';
import { IDoctorProfileUser } from '../doctor-profile-user.model';
import { IDoctorVerification } from '../doctor-profile-verification.model';

export type PartialUpdateDoctorProfile = Partial<IDoctorProfile> & Pick<IDoctorProfile, 'id'>;

type RestOf<T extends IDoctorProfile | NewDoctorProfile> = Omit<T, 'birthDate' | 'startDateOfPractice' | 'verifiedAt'> & {
  birthDate?: string | null;
  startDateOfPractice?: string | null;
  verifiedAt?: string | null;
};

type RestOfDoctorProfileUser = Omit<IDoctorProfileUser, 'birthDate' | 'startDateOfPractice'> & {
  birthDate?: string | null;
  startDateOfPractice?: string | null;
};

export type RestDoctorProfile = RestOf<IDoctorProfile>;

export type NewRestDoctorProfile = RestOf<NewDoctorProfile>;

export type PartialUpdateRestDoctorProfile = RestOf<PartialUpdateDoctorProfile>;

export type RestDoctorProfileUser = RestOfDoctorProfileUser;

export type EntityResponseType = HttpResponse<IDoctorProfile>;
export type EntityArrayResponseType = HttpResponse<IDoctorProfile[]>;
export type PublicEntityArrayResponseType = HttpResponse<IDoctorPublic[]>;

@Injectable({ providedIn: 'root' })
export class DoctorProfileService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/doctor-profiles');
  protected publicResourceUrl = this.applicationConfigService.getEndpointFor('api/public/doctors');

  create(doctorProfile: NewDoctorProfile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(doctorProfile);
    return this.http
      .post<RestDoctorProfile>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(doctorProfile: IDoctorProfile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(doctorProfile);
    return this.http
      .put<RestDoctorProfile>(`${this.resourceUrl}/${this.getDoctorProfileIdentifier(doctorProfile)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(doctorProfile: PartialUpdateDoctorProfile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(doctorProfile);
    return this.http
      .patch<RestDoctorProfile>(`${this.resourceUrl}/${this.getDoctorProfileIdentifier(doctorProfile)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDoctorProfile>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDoctorProfile[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  /**
   * Create a new doctor profile with user account atomically.
   * Endpoint: POST /api/doctor-profiles/doctor-with-user
   *
   * @param doctorUser the combined user and doctor profile data
   * @returns the created doctor profile
   */
  createWithUser(doctorUser: IDoctorProfileUser): Observable<EntityResponseType> {
    const copy = this.convertDoctorProfileUserDateFromClient(doctorUser);
    return this.http
      .post<RestDoctorProfile>(`${this.resourceUrl}/doctor-with-user`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  /**
   * Get combined DoctorProfile and User data by UID.
   * Endpoint: GET /api/doctor-profiles/doctor-with-user/{uid}
   *
   * @param uid the UID of the doctor profile
   * @returns the combined doctor profile and user data
   */
  getWithUser(uid: string): Observable<HttpResponse<IDoctorProfileUser>> {
    return this.http
      .get<RestDoctorProfileUser>(`${this.resourceUrl}/doctor-with-user/${uid}`, { observe: 'response' })
      .pipe(map(res => this.convertDoctorProfileUserResponseFromServer(res)));
  }

  /**
   * Update an existing doctor profile with user account atomically.
   * Endpoint: PUT /api/doctor-profiles/doctor-with-user
   *
   * @param doctorUser the combined user and doctor profile data
   * @returns the updated doctor profile
   */
  updateWithUser(doctorUser: IDoctorProfileUser): Observable<EntityResponseType> {
    const copy = this.convertDoctorProfileUserDateFromClient(doctorUser);
    return this.http
      .put<RestDoctorProfile>(`${this.resourceUrl}/doctor-with-user`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  /**
   * Verify (approve or reject) a doctor profile.
   * Endpoint: POST /api/doctor-profiles/{uid}/verify
   *
   * @param uid the UID of the doctor profile
   * @param approved whether to approve or reject
   * @param comment optional comment (reason for rejection)
   * @returns the updated doctor profile
   */
  verify(uid: string, approved: boolean, comment?: string): Observable<EntityResponseType> {
    const verification: IDoctorVerification = {
      approved,
      comment: comment ?? null,
    };
    return this.http
      .post<RestDoctorProfile>(`${this.resourceUrl}/${uid}/verify`, verification, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  /**
   * Query public doctors (active only, no authentication required).
   * Endpoint: GET /api/public/doctors
   *
   * @param req optional query parameters
   * @returns list of public doctor profiles
   */
  queryPublic(req?: any): Observable<PublicEntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDoctorPublic[]>(this.publicResourceUrl, { params: options, observe: 'response' });
  }

  /**
   * Get a specific public doctor by UID (active only, no authentication required).
   * Endpoint: GET /api/public/doctors/{uid}
   *
   * @param uid the UID of the doctor
   * @returns the public doctor profile
   */
  findPublic(uid: string): Observable<HttpResponse<IDoctorPublic>> {
    return this.http.get<IDoctorPublic>(`${this.publicResourceUrl}/${uid}`, { observe: 'response' });
  }

  /**
   * Count active public doctors matching criteria.
   * Endpoint: GET /api/public/doctors/count
   *
   * @param req optional query parameters
   * @returns count of matching doctors
   */
  countPublic(req?: any): Observable<HttpResponse<number>> {
    const options = createRequestOption(req);
    return this.http.get<number>(`${this.publicResourceUrl}/count`, { params: options, observe: 'response' });
  }

  getDoctorProfileIdentifier(doctorProfile: Pick<IDoctorProfile, 'id'>): number {
    return doctorProfile.id;
  }

  compareDoctorProfile(o1: Pick<IDoctorProfile, 'id'> | null, o2: Pick<IDoctorProfile, 'id'> | null): boolean {
    return o1 && o2 ? this.getDoctorProfileIdentifier(o1) === this.getDoctorProfileIdentifier(o2) : o1 === o2;
  }

  addDoctorProfileToCollectionIfMissing<Type extends Pick<IDoctorProfile, 'id'>>(
    doctorProfileCollection: Type[],
    ...doctorProfilesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const doctorProfiles: Type[] = doctorProfilesToCheck.filter(isPresent);
    if (doctorProfiles.length > 0) {
      const doctorProfileCollectionIdentifiers = doctorProfileCollection.map(doctorProfileItem =>
        this.getDoctorProfileIdentifier(doctorProfileItem),
      );
      const doctorProfilesToAdd = doctorProfiles.filter(doctorProfileItem => {
        const doctorProfileIdentifier = this.getDoctorProfileIdentifier(doctorProfileItem);
        if (doctorProfileCollectionIdentifiers.includes(doctorProfileIdentifier)) {
          return false;
        }
        doctorProfileCollectionIdentifiers.push(doctorProfileIdentifier);
        return true;
      });
      return [...doctorProfilesToAdd, ...doctorProfileCollection];
    }
    return doctorProfileCollection;
  }

  protected convertDateFromClient<T extends IDoctorProfile | NewDoctorProfile | PartialUpdateDoctorProfile>(doctorProfile: T): RestOf<T> {
    return {
      ...doctorProfile,
      birthDate: doctorProfile.birthDate?.format(DATE_FORMAT) ?? null,
      startDateOfPractice: doctorProfile.startDateOfPractice?.format(DATE_FORMAT) ?? null,
      verifiedAt: doctorProfile.verifiedAt?.toJSON() ?? null,
    };
  }

  protected convertDoctorProfileUserDateFromClient(doctorUser: IDoctorProfileUser): RestDoctorProfileUser {
    return {
      ...doctorUser,
      birthDate: doctorUser.birthDate.format(DATE_FORMAT) ?? null,
      startDateOfPractice: doctorUser.startDateOfPractice.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restDoctorProfile: RestDoctorProfile): IDoctorProfile {
    return {
      ...restDoctorProfile,
      birthDate: restDoctorProfile.birthDate ? dayjs(restDoctorProfile.birthDate) : undefined,
      startDateOfPractice: restDoctorProfile.startDateOfPractice ? dayjs(restDoctorProfile.startDateOfPractice) : undefined,
      verifiedAt: restDoctorProfile.verifiedAt ? dayjs(restDoctorProfile.verifiedAt) : undefined,
    };
  }

  protected convertDoctorProfileUserDateFromServer(restDoctorUser: RestDoctorProfileUser): IDoctorProfileUser {
    return {
      ...restDoctorUser,
      birthDate: restDoctorUser.birthDate ? dayjs(restDoctorUser.birthDate) : dayjs(),
      startDateOfPractice: restDoctorUser.startDateOfPractice ? dayjs(restDoctorUser.startDateOfPractice) : dayjs(),
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDoctorProfile>): HttpResponse<IDoctorProfile> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertDoctorProfileUserResponseFromServer(res: HttpResponse<RestDoctorProfileUser>): HttpResponse<IDoctorProfileUser> {
    return res.clone({
      body: res.body ? this.convertDoctorProfileUserDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDoctorProfile[]>): HttpResponse<IDoctorProfile[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
