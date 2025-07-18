import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPrescriptionItem, NewPrescriptionItem } from '../prescription-item.model';

export type PartialUpdatePrescriptionItem = Partial<IPrescriptionItem> & Pick<IPrescriptionItem, 'id'>;

export type EntityResponseType = HttpResponse<IPrescriptionItem>;
export type EntityArrayResponseType = HttpResponse<IPrescriptionItem[]>;

@Injectable({ providedIn: 'root' })
export class PrescriptionItemService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/prescription-items');

  create(prescriptionItem: NewPrescriptionItem): Observable<EntityResponseType> {
    return this.http.post<IPrescriptionItem>(this.resourceUrl, prescriptionItem, { observe: 'response' });
  }

  update(prescriptionItem: IPrescriptionItem): Observable<EntityResponseType> {
    return this.http.put<IPrescriptionItem>(
      `${this.resourceUrl}/${this.getPrescriptionItemIdentifier(prescriptionItem)}`,
      prescriptionItem,
      { observe: 'response' },
    );
  }

  partialUpdate(prescriptionItem: PartialUpdatePrescriptionItem): Observable<EntityResponseType> {
    return this.http.patch<IPrescriptionItem>(
      `${this.resourceUrl}/${this.getPrescriptionItemIdentifier(prescriptionItem)}`,
      prescriptionItem,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPrescriptionItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPrescriptionItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPrescriptionItemIdentifier(prescriptionItem: Pick<IPrescriptionItem, 'id'>): number {
    return prescriptionItem.id;
  }

  comparePrescriptionItem(o1: Pick<IPrescriptionItem, 'id'> | null, o2: Pick<IPrescriptionItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getPrescriptionItemIdentifier(o1) === this.getPrescriptionItemIdentifier(o2) : o1 === o2;
  }

  addPrescriptionItemToCollectionIfMissing<Type extends Pick<IPrescriptionItem, 'id'>>(
    prescriptionItemCollection: Type[],
    ...prescriptionItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const prescriptionItems: Type[] = prescriptionItemsToCheck.filter(isPresent);
    if (prescriptionItems.length > 0) {
      const prescriptionItemCollectionIdentifiers = prescriptionItemCollection.map(prescriptionItemItem =>
        this.getPrescriptionItemIdentifier(prescriptionItemItem),
      );
      const prescriptionItemsToAdd = prescriptionItems.filter(prescriptionItemItem => {
        const prescriptionItemIdentifier = this.getPrescriptionItemIdentifier(prescriptionItemItem);
        if (prescriptionItemCollectionIdentifiers.includes(prescriptionItemIdentifier)) {
          return false;
        }
        prescriptionItemCollectionIdentifiers.push(prescriptionItemIdentifier);
        return true;
      });
      return [...prescriptionItemsToAdd, ...prescriptionItemCollection];
    }
    return prescriptionItemCollection;
  }
}
