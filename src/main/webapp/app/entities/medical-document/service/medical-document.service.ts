import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMedicalDocument, NewMedicalDocument } from '../medical-document.model';

export type PartialUpdateMedicalDocument = Partial<IMedicalDocument> & Pick<IMedicalDocument, 'id'>;

type RestOf<T extends IMedicalDocument | NewMedicalDocument> = Omit<T, 'documentDate'> & {
  documentDate?: string | null;
};

export type RestMedicalDocument = RestOf<IMedicalDocument>;

export type NewRestMedicalDocument = RestOf<NewMedicalDocument>;

export type PartialUpdateRestMedicalDocument = RestOf<PartialUpdateMedicalDocument>;

export type EntityResponseType = HttpResponse<IMedicalDocument>;
export type EntityArrayResponseType = HttpResponse<IMedicalDocument[]>;

@Injectable({ providedIn: 'root' })
export class MedicalDocumentService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/medical-documents');

  create(medicalDocument: NewMedicalDocument): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(medicalDocument);
    return this.http
      .post<RestMedicalDocument>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(medicalDocument: IMedicalDocument): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(medicalDocument);
    return this.http
      .put<RestMedicalDocument>(`${this.resourceUrl}/${this.getMedicalDocumentIdentifier(medicalDocument)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(medicalDocument: PartialUpdateMedicalDocument): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(medicalDocument);
    return this.http
      .patch<RestMedicalDocument>(`${this.resourceUrl}/${this.getMedicalDocumentIdentifier(medicalDocument)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMedicalDocument>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMedicalDocument[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMedicalDocumentIdentifier(medicalDocument: Pick<IMedicalDocument, 'id'>): number {
    return medicalDocument.id;
  }

  compareMedicalDocument(o1: Pick<IMedicalDocument, 'id'> | null, o2: Pick<IMedicalDocument, 'id'> | null): boolean {
    return o1 && o2 ? this.getMedicalDocumentIdentifier(o1) === this.getMedicalDocumentIdentifier(o2) : o1 === o2;
  }

  addMedicalDocumentToCollectionIfMissing<Type extends Pick<IMedicalDocument, 'id'>>(
    medicalDocumentCollection: Type[],
    ...medicalDocumentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const medicalDocuments: Type[] = medicalDocumentsToCheck.filter(isPresent);
    if (medicalDocuments.length > 0) {
      const medicalDocumentCollectionIdentifiers = medicalDocumentCollection.map(medicalDocumentItem =>
        this.getMedicalDocumentIdentifier(medicalDocumentItem),
      );
      const medicalDocumentsToAdd = medicalDocuments.filter(medicalDocumentItem => {
        const medicalDocumentIdentifier = this.getMedicalDocumentIdentifier(medicalDocumentItem);
        if (medicalDocumentCollectionIdentifiers.includes(medicalDocumentIdentifier)) {
          return false;
        }
        medicalDocumentCollectionIdentifiers.push(medicalDocumentIdentifier);
        return true;
      });
      return [...medicalDocumentsToAdd, ...medicalDocumentCollection];
    }
    return medicalDocumentCollection;
  }

  protected convertDateFromClient<T extends IMedicalDocument | NewMedicalDocument | PartialUpdateMedicalDocument>(
    medicalDocument: T,
  ): RestOf<T> {
    return {
      ...medicalDocument,
      documentDate: medicalDocument.documentDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restMedicalDocument: RestMedicalDocument): IMedicalDocument {
    return {
      ...restMedicalDocument,
      documentDate: restMedicalDocument.documentDate ? dayjs(restMedicalDocument.documentDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMedicalDocument>): HttpResponse<IMedicalDocument> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMedicalDocument[]>): HttpResponse<IMedicalDocument[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
