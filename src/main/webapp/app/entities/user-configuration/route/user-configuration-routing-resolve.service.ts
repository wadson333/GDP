import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserConfiguration } from '../user-configuration.model';
import { UserConfigurationService } from '../service/user-configuration.service';

const userConfigurationResolve = (route: ActivatedRouteSnapshot): Observable<null | IUserConfiguration> => {
  const id = route.params.id;
  if (id) {
    return inject(UserConfigurationService)
      .find(id)
      .pipe(
        mergeMap((userConfiguration: HttpResponse<IUserConfiguration>) => {
          if (userConfiguration.body) {
            return of(userConfiguration.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default userConfigurationResolve;
