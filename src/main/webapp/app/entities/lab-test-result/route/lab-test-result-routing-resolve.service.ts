import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILabTestResult } from '../lab-test-result.model';
import { LabTestResultService } from '../service/lab-test-result.service';

const labTestResultResolve = (route: ActivatedRouteSnapshot): Observable<null | ILabTestResult> => {
  const id = route.params.id;
  if (id) {
    return inject(LabTestResultService)
      .find(id)
      .pipe(
        mergeMap((labTestResult: HttpResponse<ILabTestResult>) => {
          if (labTestResult.body) {
            return of(labTestResult.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default labTestResultResolve;
