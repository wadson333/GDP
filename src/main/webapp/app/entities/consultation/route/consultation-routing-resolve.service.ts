import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConsultation } from '../consultation.model';
import { ConsultationService } from '../service/consultation.service';

const consultationResolve = (route: ActivatedRouteSnapshot): Observable<null | IConsultation> => {
  const id = route.params.id;
  if (id) {
    return inject(ConsultationService)
      .find(id)
      .pipe(
        mergeMap((consultation: HttpResponse<IConsultation>) => {
          if (consultation.body) {
            return of(consultation.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default consultationResolve;
