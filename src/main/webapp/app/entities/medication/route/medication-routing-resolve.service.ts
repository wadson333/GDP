import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMedication } from '../medication.model';
import { MedicationService } from '../service/medication.service';

const medicationResolve = (route: ActivatedRouteSnapshot): Observable<null | IMedication> => {
  const id = route.params.id;
  if (id) {
    return inject(MedicationService)
      .find(id)
      .pipe(
        mergeMap((medication: HttpResponse<IMedication>) => {
          if (medication.body) {
            return of(medication.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default medicationResolve;
