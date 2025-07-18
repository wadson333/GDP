import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import LabTestResultResolve from './route/lab-test-result-routing-resolve.service';

const labTestResultRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/lab-test-result.component').then(m => m.LabTestResultComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/lab-test-result-detail.component').then(m => m.LabTestResultDetailComponent),
    resolve: {
      labTestResult: LabTestResultResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/lab-test-result-update.component').then(m => m.LabTestResultUpdateComponent),
    resolve: {
      labTestResult: LabTestResultResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/lab-test-result-update.component').then(m => m.LabTestResultUpdateComponent),
    resolve: {
      labTestResult: LabTestResultResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default labTestResultRoute;
