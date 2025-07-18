import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MedicalDocumentResolve from './route/medical-document-routing-resolve.service';

const medicalDocumentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/medical-document.component').then(m => m.MedicalDocumentComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/medical-document-detail.component').then(m => m.MedicalDocumentDetailComponent),
    resolve: {
      medicalDocument: MedicalDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/medical-document-update.component').then(m => m.MedicalDocumentUpdateComponent),
    resolve: {
      medicalDocument: MedicalDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/medical-document-update.component').then(m => m.MedicalDocumentUpdateComponent),
    resolve: {
      medicalDocument: MedicalDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default medicalDocumentRoute;
