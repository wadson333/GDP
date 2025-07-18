import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILabTestCatalog } from '../lab-test-catalog.model';
import { LabTestCatalogService } from '../service/lab-test-catalog.service';

const labTestCatalogResolve = (route: ActivatedRouteSnapshot): Observable<null | ILabTestCatalog> => {
  const id = route.params.id;
  if (id) {
    return inject(LabTestCatalogService)
      .find(id)
      .pipe(
        mergeMap((labTestCatalog: HttpResponse<ILabTestCatalog>) => {
          if (labTestCatalog.body) {
            return of(labTestCatalog.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default labTestCatalogResolve;
