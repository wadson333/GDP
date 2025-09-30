import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILabTestCatalog, NewLabTestCatalog } from '../lab-test-catalog.model';
import { SearchCriteria } from '../search-criteria.model';

export type PartialUpdateLabTestCatalog = Partial<ILabTestCatalog> & Pick<ILabTestCatalog, 'id'>;

type RestOf<T extends ILabTestCatalog | NewLabTestCatalog> = Omit<T, 'validFrom' | 'validTo'> & {
  validFrom?: string | null;
  validTo?: string | null;
};

export type RestLabTestCatalog = RestOf<ILabTestCatalog>;

export type NewRestLabTestCatalog = RestOf<NewLabTestCatalog>;

export type PartialUpdateRestLabTestCatalog = RestOf<PartialUpdateLabTestCatalog>;

export type EntityResponseType = HttpResponse<ILabTestCatalog>;
export type EntityArrayResponseType = HttpResponse<ILabTestCatalog[]>;

@Injectable({ providedIn: 'root' })
export class LabTestCatalogService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lab-test-catalogs');

  create(labTestCatalog: NewLabTestCatalog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(labTestCatalog);
    return this.http
      .post<RestLabTestCatalog>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(labTestCatalog: ILabTestCatalog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(labTestCatalog);
    return this.http
      .put<RestLabTestCatalog>(`${this.resourceUrl}/${this.getLabTestCatalogIdentifier(labTestCatalog)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(labTestCatalog: PartialUpdateLabTestCatalog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(labTestCatalog);
    return this.http
      .patch<RestLabTestCatalog>(`${this.resourceUrl}/${this.getLabTestCatalogIdentifier(labTestCatalog)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestLabTestCatalog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestLabTestCatalog[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const params = createRequestOption(req);

    return this.http
      .get<RestLabTestCatalog[]>(`${this.resourceUrl}/search`, {
        params,
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getLatestVersions(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestLabTestCatalog[]>(`${this.resourceUrl}/latest`, {
        params: options,
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLabTestCatalogIdentifier(labTestCatalog: Pick<ILabTestCatalog, 'id'>): number {
    return labTestCatalog.id;
  }

  compareLabTestCatalog(o1: Pick<ILabTestCatalog, 'id'> | null, o2: Pick<ILabTestCatalog, 'id'> | null): boolean {
    return o1 && o2 ? this.getLabTestCatalogIdentifier(o1) === this.getLabTestCatalogIdentifier(o2) : o1 === o2;
  }

  addLabTestCatalogToCollectionIfMissing<Type extends Pick<ILabTestCatalog, 'id'>>(
    labTestCatalogCollection: Type[],
    ...labTestCatalogsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const labTestCatalogs: Type[] = labTestCatalogsToCheck.filter(isPresent);
    if (labTestCatalogs.length > 0) {
      const labTestCatalogCollectionIdentifiers = labTestCatalogCollection.map(labTestCatalogItem =>
        this.getLabTestCatalogIdentifier(labTestCatalogItem),
      );
      const labTestCatalogsToAdd = labTestCatalogs.filter(labTestCatalogItem => {
        const labTestCatalogIdentifier = this.getLabTestCatalogIdentifier(labTestCatalogItem);
        if (labTestCatalogCollectionIdentifiers.includes(labTestCatalogIdentifier)) {
          return false;
        }
        labTestCatalogCollectionIdentifiers.push(labTestCatalogIdentifier);
        return true;
      });
      return [...labTestCatalogsToAdd, ...labTestCatalogCollection];
    }
    return labTestCatalogCollection;
  }

  protected convertDateFromClient<T extends ILabTestCatalog | NewLabTestCatalog | PartialUpdateLabTestCatalog>(
    labTestCatalog: T,
  ): RestOf<T> {
    return {
      ...labTestCatalog,
      validFrom: labTestCatalog.validFrom?.toJSON() ?? null,
      validTo: labTestCatalog.validTo?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restLabTestCatalog: RestLabTestCatalog): ILabTestCatalog {
    return {
      ...restLabTestCatalog,
      validFrom: restLabTestCatalog.validFrom ? dayjs(restLabTestCatalog.validFrom) : undefined,
      validTo: restLabTestCatalog.validTo ? dayjs(restLabTestCatalog.validTo) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestLabTestCatalog>): HttpResponse<ILabTestCatalog> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestLabTestCatalog[]>): HttpResponse<ILabTestCatalog[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
