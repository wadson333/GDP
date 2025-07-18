import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import HospitalizationResolve from './route/hospitalization-routing-resolve.service';

const hospitalizationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/hospitalization.component').then(m => m.HospitalizationComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/hospitalization-detail.component').then(m => m.HospitalizationDetailComponent),
    resolve: {
      hospitalization: HospitalizationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/hospitalization-update.component').then(m => m.HospitalizationUpdateComponent),
    resolve: {
      hospitalization: HospitalizationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/hospitalization-update.component').then(m => m.HospitalizationUpdateComponent),
    resolve: {
      hospitalization: HospitalizationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default hospitalizationRoute;
