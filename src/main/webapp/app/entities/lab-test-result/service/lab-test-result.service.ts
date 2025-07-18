import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILabTestResult, NewLabTestResult } from '../lab-test-result.model';

export type PartialUpdateLabTestResult = Partial<ILabTestResult> & Pick<ILabTestResult, 'id'>;

type RestOf<T extends ILabTestResult | NewLabTestResult> = Omit<T, 'resultDate'> & {
  resultDate?: string | null;
};

export type RestLabTestResult = RestOf<ILabTestResult>;

export type NewRestLabTestResult = RestOf<NewLabTestResult>;

export type PartialUpdateRestLabTestResult = RestOf<PartialUpdateLabTestResult>;

export type EntityResponseType = HttpResponse<ILabTestResult>;
export type EntityArrayResponseType = HttpResponse<ILabTestResult[]>;

@Injectable({ providedIn: 'root' })
export class LabTestResultService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lab-test-results');

  create(labTestResult: NewLabTestResult): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(labTestResult);
    return this.http
      .post<RestLabTestResult>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(labTestResult: ILabTestResult): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(labTestResult);
    return this.http
      .put<RestLabTestResult>(`${this.resourceUrl}/${this.getLabTestResultIdentifier(labTestResult)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(labTestResult: PartialUpdateLabTestResult): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(labTestResult);
    return this.http
      .patch<RestLabTestResult>(`${this.resourceUrl}/${this.getLabTestResultIdentifier(labTestResult)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestLabTestResult>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestLabTestResult[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLabTestResultIdentifier(labTestResult: Pick<ILabTestResult, 'id'>): number {
    return labTestResult.id;
  }

  compareLabTestResult(o1: Pick<ILabTestResult, 'id'> | null, o2: Pick<ILabTestResult, 'id'> | null): boolean {
    return o1 && o2 ? this.getLabTestResultIdentifier(o1) === this.getLabTestResultIdentifier(o2) : o1 === o2;
  }

  addLabTestResultToCollectionIfMissing<Type extends Pick<ILabTestResult, 'id'>>(
    labTestResultCollection: Type[],
    ...labTestResultsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const labTestResults: Type[] = labTestResultsToCheck.filter(isPresent);
    if (labTestResults.length > 0) {
      const labTestResultCollectionIdentifiers = labTestResultCollection.map(labTestResultItem =>
        this.getLabTestResultIdentifier(labTestResultItem),
      );
      const labTestResultsToAdd = labTestResults.filter(labTestResultItem => {
        const labTestResultIdentifier = this.getLabTestResultIdentifier(labTestResultItem);
        if (labTestResultCollectionIdentifiers.includes(labTestResultIdentifier)) {
          return false;
        }
        labTestResultCollectionIdentifiers.push(labTestResultIdentifier);
        return true;
      });
      return [...labTestResultsToAdd, ...labTestResultCollection];
    }
    return labTestResultCollection;
  }

  protected convertDateFromClient<T extends ILabTestResult | NewLabTestResult | PartialUpdateLabTestResult>(labTestResult: T): RestOf<T> {
    return {
      ...labTestResult,
      resultDate: labTestResult.resultDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restLabTestResult: RestLabTestResult): ILabTestResult {
    return {
      ...restLabTestResult,
      resultDate: restLabTestResult.resultDate ? dayjs(restLabTestResult.resultDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestLabTestResult>): HttpResponse<ILabTestResult> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestLabTestResult[]>): HttpResponse<ILabTestResult[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
