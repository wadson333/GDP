import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ConsultationResolve from './route/consultation-routing-resolve.service';

const consultationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/consultation.component').then(m => m.ConsultationComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/consultation-detail.component').then(m => m.ConsultationDetailComponent),
    resolve: {
      consultation: ConsultationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/consultation-update.component').then(m => m.ConsultationUpdateComponent),
    resolve: {
      consultation: ConsultationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/consultation-update.component').then(m => m.ConsultationUpdateComponent),
    resolve: {
      consultation: ConsultationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default consultationRoute;
