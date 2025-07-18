import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDoctorProfile } from '../doctor-profile.model';
import { DoctorProfileService } from '../service/doctor-profile.service';

const doctorProfileResolve = (route: ActivatedRouteSnapshot): Observable<null | IDoctorProfile> => {
  const id = route.params.id;
  if (id) {
    return inject(DoctorProfileService)
      .find(id)
      .pipe(
        mergeMap((doctorProfile: HttpResponse<IDoctorProfile>) => {
          if (doctorProfile.body) {
            return of(doctorProfile.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default doctorProfileResolve;
