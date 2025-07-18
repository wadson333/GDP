import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHospitalization, NewHospitalization } from '../hospitalization.model';

export type PartialUpdateHospitalization = Partial<IHospitalization> & Pick<IHospitalization, 'id'>;

type RestOf<T extends IHospitalization | NewHospitalization> = Omit<T, 'admissionDate' | 'dischargeDate'> & {
  admissionDate?: string | null;
  dischargeDate?: string | null;
};

export type RestHospitalization = RestOf<IHospitalization>;

export type NewRestHospitalization = RestOf<NewHospitalization>;

export type PartialUpdateRestHospitalization = RestOf<PartialUpdateHospitalization>;

export type EntityResponseType = HttpResponse<IHospitalization>;
export type EntityArrayResponseType = HttpResponse<IHospitalization[]>;

@Injectable({ providedIn: 'root' })
export class HospitalizationService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/hospitalizations');

  create(hospitalization: NewHospitalization): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hospitalization);
    return this.http
      .post<RestHospitalization>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(hospitalization: IHospitalization): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hospitalization);
    return this.http
      .put<RestHospitalization>(`${this.resourceUrl}/${this.getHospitalizationIdentifier(hospitalization)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(hospitalization: PartialUpdateHospitalization): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(hospitalization);
    return this.http
      .patch<RestHospitalization>(`${this.resourceUrl}/${this.getHospitalizationIdentifier(hospitalization)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestHospitalization>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestHospitalization[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getHospitalizationIdentifier(hospitalization: Pick<IHospitalization, 'id'>): number {
    return hospitalization.id;
  }

  compareHospitalization(o1: Pick<IHospitalization, 'id'> | null, o2: Pick<IHospitalization, 'id'> | null): boolean {
    return o1 && o2 ? this.getHospitalizationIdentifier(o1) === this.getHospitalizationIdentifier(o2) : o1 === o2;
  }

  addHospitalizationToCollectionIfMissing<Type extends Pick<IHospitalization, 'id'>>(
    hospitalizationCollection: Type[],
    ...hospitalizationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const hospitalizations: Type[] = hospitalizationsToCheck.filter(isPresent);
    if (hospitalizations.length > 0) {
      const hospitalizationCollectionIdentifiers = hospitalizationCollection.map(hospitalizationItem =>
        this.getHospitalizationIdentifier(hospitalizationItem),
      );
      const hospitalizationsToAdd = hospitalizations.filter(hospitalizationItem => {
        const hospitalizationIdentifier = this.getHospitalizationIdentifier(hospitalizationItem);
        if (hospitalizationCollectionIdentifiers.includes(hospitalizationIdentifier)) {
          return false;
        }
        hospitalizationCollectionIdentifiers.push(hospitalizationIdentifier);
        return true;
      });
      return [...hospitalizationsToAdd, ...hospitalizationCollection];
    }
    return hospitalizationCollection;
  }

  protected convertDateFromClient<T extends IHospitalization | NewHospitalization | PartialUpdateHospitalization>(
    hospitalization: T,
  ): RestOf<T> {
    return {
      ...hospitalization,
      admissionDate: hospitalization.admissionDate?.toJSON() ?? null,
      dischargeDate: hospitalization.dischargeDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restHospitalization: RestHospitalization): IHospitalization {
    return {
      ...restHospitalization,
      admissionDate: restHospitalization.admissionDate ? dayjs(restHospitalization.admissionDate) : undefined,
      dischargeDate: restHospitalization.dischargeDate ? dayjs(restHospitalization.dischargeDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestHospitalization>): HttpResponse<IHospitalization> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestHospitalization[]>): HttpResponse<IHospitalization[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
