import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrescriptionItem } from '../prescription-item.model';
import { PrescriptionItemService } from '../service/prescription-item.service';

const prescriptionItemResolve = (route: ActivatedRouteSnapshot): Observable<null | IPrescriptionItem> => {
  const id = route.params.id;
  if (id) {
    return inject(PrescriptionItemService)
      .find(id)
      .pipe(
        mergeMap((prescriptionItem: HttpResponse<IPrescriptionItem>) => {
          if (prescriptionItem.body) {
            return of(prescriptionItem.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default prescriptionItemResolve;
