import { Injectable, Renderer2, RendererFactory2 } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ThemeService {
  private renderer: Renderer2;
  private themeLink: HTMLLinkElement | undefined;
  private readonly THEME_KEY = 'selected-theme';

  constructor(private rendererFactory: RendererFactory2) {
    this.renderer = this.rendererFactory.createRenderer(null, null);
  }

  /**
   * Initialise le service, charge le thème sauvegardé ou le thème par défaut.
   * Doit être appelé dans le constructeur de app.component.ts.
   */
  initTheme(): void {
    const savedTheme = localStorage.getItem(this.THEME_KEY);
    if (savedTheme) {
      this.switchTheme(savedTheme);
    } else {
      // Thème par défaut si rien n'est sauvegardé
      this.switchTheme('saga-blue');
    }
  }

  /**
   * Change le thème de l'application.
   * @param themeName Le nom du bundle du thème (ex: 'saga-blue', 'arya-blue')
   */
  switchTheme(themeName: string): void {
    // Sauvegarde le choix de l'utilisateur
    localStorage.setItem(this.THEME_KEY, themeName);

    const newThemeHref = `${themeName}.css`;

    // Si le lien du thème n'existe pas encore, on le crée
    if (!this.themeLink) {
      this.themeLink = this.renderer.createElement('link');
      this.renderer.setAttribute(this.themeLink, 'rel', 'stylesheet');
      this.renderer.appendChild(document.head, this.themeLink);
    }

    // On met à jour le href pour charger le bon fichier CSS
    this.renderer.setAttribute(this.themeLink, 'href', newThemeHref);
  }
}
