import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MedicationResolve from './route/medication-routing-resolve.service';

const medicationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/medication.component').then(m => m.MedicationComponent),
    data: {
      defaultSort: `id,${ASC}`,
      breadcrumb: 'gdpApp.medication.breadcrumb',
    },
    canActivate: [UserRouteAccessService],
  },
  // {
  //   path: ':id/view',
  //   loadComponent: () => import('./detail/medication-detail.component').then(m => m.MedicationDetailComponent),
  //   resolve: {
  //     medication: MedicationResolve,
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  // {
  //   path: 'new',
  //   loadComponent: () => import('./update/medication-update.component').then(m => m.MedicationUpdateComponent),
  //   resolve: {
  //     medication: MedicationResolve,
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  // {
  //   path: ':id/edit',
  //   loadComponent: () => import('./update/medication-update.component').then(m => m.MedicationUpdateComponent),
  //   resolve: {
  //     medication: MedicationResolve,
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
];

export default medicationRoute;
