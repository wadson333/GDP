/* eslint-disable @typescript-eslint/no-useless-constructor */
import { Component, Input, OnInit, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import SharedModule from 'app/shared/shared.module';
import { AccountService } from 'app/core/auth/account.service';
import { AvatarModule } from 'primeng/avatar';
import { BadgeModule } from 'primeng/badge';
import { RippleModule } from 'primeng/ripple';
import { MenuItem } from 'primeng/api';
import { PanelMenuModule } from 'primeng/panelmenu';
import { MenuModule } from 'primeng/menu';
import { LoginService } from 'app/login/login.service';
import { ButtonModule } from 'primeng/button';

@Component({
  standalone: true,
  selector: 'jhi-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss',
  imports: [RouterModule, SharedModule, AvatarModule, BadgeModule, RippleModule, PanelMenuModule, MenuModule, ButtonModule],
})
export default class SidebarComponent implements OnInit {
  @Input() collapsed = false; // Reçoit l'état depuis le parent
  account = inject(AccountService).trackCurrentAccount();
  isConfigCollapsed = false;
  isAdmCollapsed = false;
  items: MenuItem[] | undefined;
  protected accountService = inject(AccountService);
  private router = inject(Router);
  private loginService = inject(LoginService);

  // eslint-disable-next-line @typescript-eslint/no-empty-function
  constructor() {}

  ngOnInit(): void {
    this.items = [
      // {
      //   label: 'TABLEAU DE BORD',
      //   items: [
      {
        label: 'Tableau de bord',
        icon: 'pi pi-home',
        routerLink: ['d/dashboard'],
        visible: this.accountService.hasAnyAuthority(['ROLE_USER', 'ROLE_ADMIN']),
        routerLinkActiveOptions: { exact: true },
        title: 'Tableau de bord',
      },
      {
        label: 'Rendez-vous',
        icon: 'pi pi-calendar',
        routerLink: ['/appointment'],
        routerLinkActiveOptions: { exact: true },
        title: 'Rendez-vous',
      },
      //   ],
      // },
      {
        label: 'GESTION CLINIQUE',
        items: [
          {
            label: 'Patients',
            icon: 'pi pi-id-card',
            routerLink: ['/patient'],
          },
          {
            label: 'Médecins',
            icon: 'pi pi-graduation-cap',
            routerLink: ['/doctor-profile'],
          },
          {
            label: 'Consultations',
            icon: 'pi pi-comments',
            routerLink: ['/consultation'],
          },
          {
            label: 'Hospitalisations',
            icon: 'pi pi-briefcase',
            routerLink: ['/hospitalization'],
          },
        ],
      },
      {
        label: 'SUIVI MÉDICAL',
        items: [
          {
            label: 'Résultats des examens',
            icon: 'pi pi-file-check',
            routerLink: ['/lab-test-result'],
          },
          {
            label: 'Documents médicaux',
            icon: 'pi pi-folder-open',
            routerLink: ['/medical-document'],
            routerLinkActiveOptions: { exact: true },
            title: 'Documents médicaux',
          },
          {
            label: 'Prescriptions',
            icon: 'pi pi-file-edit',
            routerLink: ['/prescription'],
          },
        ],
      },
      {
        label: 'CATALOGUE DES SERVICES',
        items: [
          {
            label: 'Médicaments',
            icon: 'pi pi-heart',
            routerLink: ['/medication'],
          },
          {
            label: 'Examens médicaux',
            icon: 'pi pi-search',
            routerLink: ['/lab-test-catalog'],
          },
        ],
      },
      {
        label: 'ADMINISTRATION',
        items: [
          {
            label: 'Utilisateurs',
            icon: 'pi pi-users',
            routerLink: ['/admin/user-management'],
          },
          {
            label: 'Rôles et permissions',
            icon: 'pi pi-lock',
            routerLink: ['/authority'],
          },
        ],
      },
    ];
  }

  logout(): void {
    this.loginService.logout();
    this.router.navigate(['']);
  }
}
