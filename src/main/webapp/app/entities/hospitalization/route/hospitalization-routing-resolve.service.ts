import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHospitalization } from '../hospitalization.model';
import { HospitalizationService } from '../service/hospitalization.service';

const hospitalizationResolve = (route: ActivatedRouteSnapshot): Observable<null | IHospitalization> => {
  const id = route.params.id;
  if (id) {
    return inject(HospitalizationService)
      .find(id)
      .pipe(
        mergeMap((hospitalization: HttpResponse<IHospitalization>) => {
          if (hospitalization.body) {
            return of(hospitalization.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default hospitalizationResolve;
