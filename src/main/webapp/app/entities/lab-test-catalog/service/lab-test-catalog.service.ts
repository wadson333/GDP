import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILabTestCatalog, NewLabTestCatalog } from '../lab-test-catalog.model';

export type PartialUpdateLabTestCatalog = Partial<ILabTestCatalog> & Pick<ILabTestCatalog, 'id'>;

export type EntityResponseType = HttpResponse<ILabTestCatalog>;
export type EntityArrayResponseType = HttpResponse<ILabTestCatalog[]>;

@Injectable({ providedIn: 'root' })
export class LabTestCatalogService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lab-test-catalogs');

  create(labTestCatalog: NewLabTestCatalog): Observable<EntityResponseType> {
    return this.http.post<ILabTestCatalog>(this.resourceUrl, labTestCatalog, { observe: 'response' });
  }

  update(labTestCatalog: ILabTestCatalog): Observable<EntityResponseType> {
    return this.http.put<ILabTestCatalog>(`${this.resourceUrl}/${this.getLabTestCatalogIdentifier(labTestCatalog)}`, labTestCatalog, {
      observe: 'response',
    });
  }

  partialUpdate(labTestCatalog: PartialUpdateLabTestCatalog): Observable<EntityResponseType> {
    return this.http.patch<ILabTestCatalog>(`${this.resourceUrl}/${this.getLabTestCatalogIdentifier(labTestCatalog)}`, labTestCatalog, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILabTestCatalog>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILabTestCatalog[]>(this.resourceUrl, { params: options, observe: 'response' });
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
}
