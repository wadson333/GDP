import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import UserConfigurationResolve from './route/user-configuration-routing-resolve.service';

const userConfigurationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/user-configuration.component').then(m => m.UserConfigurationComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/user-configuration-detail.component').then(m => m.UserConfigurationDetailComponent),
    resolve: {
      userConfiguration: UserConfigurationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/user-configuration-update.component').then(m => m.UserConfigurationUpdateComponent),
    resolve: {
      userConfiguration: UserConfigurationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/user-configuration-update.component').then(m => m.UserConfigurationUpdateComponent),
    resolve: {
      userConfiguration: UserConfigurationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default userConfigurationRoute;
