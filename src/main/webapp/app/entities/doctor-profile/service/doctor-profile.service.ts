import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDoctorProfile, NewDoctorProfile } from '../doctor-profile.model';

export type PartialUpdateDoctorProfile = Partial<IDoctorProfile> & Pick<IDoctorProfile, 'id'>;

type RestOf<T extends IDoctorProfile | NewDoctorProfile> = Omit<T, 'startDateOfPractice'> & {
  startDateOfPractice?: string | null;
};

export type RestDoctorProfile = RestOf<IDoctorProfile>;

export type NewRestDoctorProfile = RestOf<NewDoctorProfile>;

export type PartialUpdateRestDoctorProfile = RestOf<PartialUpdateDoctorProfile>;

export type EntityResponseType = HttpResponse<IDoctorProfile>;
export type EntityArrayResponseType = HttpResponse<IDoctorProfile[]>;

@Injectable({ providedIn: 'root' })
export class DoctorProfileService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/doctor-profiles');

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
      startDateOfPractice: doctorProfile.startDateOfPractice?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restDoctorProfile: RestDoctorProfile): IDoctorProfile {
    return {
      ...restDoctorProfile,
      startDateOfPractice: restDoctorProfile.startDateOfPractice ? dayjs(restDoctorProfile.startDateOfPractice) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDoctorProfile>): HttpResponse<IDoctorProfile> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDoctorProfile[]>): HttpResponse<IDoctorProfile[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
