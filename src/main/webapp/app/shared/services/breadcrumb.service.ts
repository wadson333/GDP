// Fichier : breadcrumb.service.ts (Version simplifiée et finale)

import { Injectable, OnDestroy } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router, Data } from '@angular/router';
import { filter, takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { MenuItem } from 'primeng/api';

@Injectable({ providedIn: 'root' })
export class BreadcrumbService implements OnDestroy {
  // On revient à un simple tableau public
  items: MenuItem[] = [];

  private destroy$ = new Subject<void>();

  constructor(private router: Router) {
    this.router.events
      .pipe(
        filter(event => event instanceof NavigationEnd),
        takeUntil(this.destroy$),
      )
      .subscribe(() => {
        // La seule responsabilité du service est de construire le tableau d'items
        this.items = this.createBreadcrumbs(this.router.routerState.root);
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private createBreadcrumbs(route: ActivatedRoute, url = '', breadcrumbs: MenuItem[] = []): MenuItem[] {
    const children: ActivatedRoute[] = route.children;

    if (children.length === 0) {
      return breadcrumbs;
    }

    for (const child of children) {
      const routeURL: string = child.snapshot.url.map(segment => segment.path).join('/');
      if (routeURL !== '') {
        url += `/${routeURL}`;
      }
      const breadcrumbKey = child.snapshot.data['breadcrumb'] as string | undefined;

      if (breadcrumbKey) {
        // ON NE TRADUIT PAS ICI ! On stocke la clé directement.
        const item: MenuItem = {
          label: breadcrumbKey, // Le label est la clé de traduction
          routerLink: url,
        };
        breadcrumbs.push(item);
      }

      return this.createBreadcrumbs(child, url, breadcrumbs);
    }
    return breadcrumbs;
  }
}
