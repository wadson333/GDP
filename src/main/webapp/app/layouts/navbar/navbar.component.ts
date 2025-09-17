import { Component, EventEmitter, OnInit, Output, inject, signal } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

import { StateStorageService } from 'app/core/auth/state-storage.service';
import SharedModule from 'app/shared/shared.module';
import HasAnyAuthorityDirective from 'app/shared/auth/has-any-authority.directive';
import { VERSION } from 'app/app.constants';
import { LANGUAGES } from 'app/config/language.constants';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { EntityNavbarItems } from 'app/entities/entity-navbar-items';
import ActiveMenuDirective from './active-menu.directive';
import NavbarItem from './navbar-item.model';
import { ThemeService } from 'app/core/theme/theme.service';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { TieredMenuModule } from 'primeng/tieredmenu';
import { AvatarModule } from 'primeng/avatar';
import { BreadcrumbModule } from 'primeng/breadcrumb';
import { BreadcrumbService } from 'app/shared/services/breadcrumb.service';
import { DividerModule } from 'primeng/divider';

@Component({
  standalone: true,
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
  imports: [
    RouterModule,
    SharedModule,
    HasAnyAuthorityDirective,
    ActiveMenuDirective,
    FormsModule,
    ButtonModule,
    TieredMenuModule,
    AvatarModule,
    BreadcrumbModule,
    DividerModule,
  ],
})
export default class NavbarComponent implements OnInit {
  @Output() sidebarToggle = new EventEmitter<void>();
  inProduction?: boolean;
  isNavbarCollapsed = signal(true);
  languages = LANGUAGES.map(lang => ({
    label: lang,
    icon: 'pi pi-flag',
    command: () => this.changeLanguage(lang),
  }));
  openAPIEnabled?: boolean;
  version = '';
  account = inject(AccountService).trackCurrentAccount();
  entitiesNavbarItems: NavbarItem[] = [];

  selectedTheme = 'lara-light-blue';
  themes = [
    { label: 'Lara Light Blue', icon: 'pi pi-sun', command: () => this.switchTheme('lara-light-blue') },
    { label: 'MD Light Indigo', icon: 'pi pi-sun', command: () => this.switchTheme('md-light-indigo') },
    { label: 'MD Light Purple', icon: 'pi pi-sun', command: () => this.switchTheme('md-light-deeppurple') },
    { label: 'MD Dark Indigo', icon: 'pi pi-moon', command: () => this.switchTheme('md-dark-indigo') },
    { label: 'MD Dark Purple', icon: 'pi pi-moon', command: () => this.switchTheme('md-dark-deeppurple') },
    { label: 'Viva Light', icon: 'pi pi-sun', command: () => this.switchTheme('viva-light') },
    { label: 'Viva Dark', icon: 'pi pi-moon', command: () => this.switchTheme('viva-dark') },
  ];
  accountMenuItems = [
    { label: 'Profil', icon: 'pi pi-user', command: () => this.router.navigate(['/account/settings']) },
    { label: 'setting', icon: 'pi pi-cog', command: () => this.router.navigate(['/account/password']) },
  ];

  private loginService = inject(LoginService);
  private translateService = inject(TranslateService);
  private stateStorageService = inject(StateStorageService);
  private profileService = inject(ProfileService);
  private router = inject(Router);
  private themeService = inject(ThemeService);

  constructor(public breadcrumbService: BreadcrumbService) {
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`;
    }
  }

  ngOnInit(): void {
    this.entitiesNavbarItems = EntityNavbarItems;
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });
  }

  changeLanguage(languageKey: string): void {
    this.stateStorageService.storeLocale(languageKey);
    this.translateService.use(languageKey);
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed.set(true);
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  logout(): void {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed.update(isNavbarCollapsed => !isNavbarCollapsed);
  }
  switchTheme(theme: any): void {
    this.themeService.switchTheme(theme);
    // this.selectedTheme = theme;
  }

  toggle(): void {
    this.sidebarToggle.emit();
  }
}
