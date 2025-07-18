import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PrescriptionItemResolve from './route/prescription-item-routing-resolve.service';

const prescriptionItemRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/prescription-item.component').then(m => m.PrescriptionItemComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/prescription-item-detail.component').then(m => m.PrescriptionItemDetailComponent),
    resolve: {
      prescriptionItem: PrescriptionItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/prescription-item-update.component').then(m => m.PrescriptionItemUpdateComponent),
    resolve: {
      prescriptionItem: PrescriptionItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/prescription-item-update.component').then(m => m.PrescriptionItemUpdateComponent),
    resolve: {
      prescriptionItem: PrescriptionItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default prescriptionItemRoute;
