import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DoctorProfileResolve from './route/doctor-profile-routing-resolve.service';

const doctorProfileRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/doctor-profile.component').then(m => m.DoctorProfileComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/doctor-profile-detail.component').then(m => m.DoctorProfileDetailComponent),
    resolve: {
      doctorProfile: DoctorProfileResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/doctor-profile-update.component').then(m => m.DoctorProfileUpdateComponent),
    resolve: {
      doctorProfile: DoctorProfileResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/doctor-profile-update.component').then(m => m.DoctorProfileUpdateComponent),
    resolve: {
      doctorProfile: DoctorProfileResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default doctorProfileRoute;
