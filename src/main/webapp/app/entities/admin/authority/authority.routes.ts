import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import AuthorityResolve from './route/authority-routing-resolve.service';

const authorityRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/authority.component').then(m => m.AuthorityComponent),
    data: {
      authorities: ['ROLE_ADMIN'],
      breadcrumb: 'gdpApp.adminAuthority.breadcrumb',
      defaultSort: 'name,asc',
    },
    title: 'Les roles d’utilisateurs',
    canActivate: [UserRouteAccessService],
  },
];

export default authorityRoute;
