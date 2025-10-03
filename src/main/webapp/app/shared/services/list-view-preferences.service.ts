/* eslint-disable prettier/prettier */
import { Injectable } from '@angular/core';
import { ListViewPreferences } from '../model/list-view-preferences.model';

@Injectable({ providedIn: 'root' })
export class UserPreferencesService {
  getViewPreferences(key: string): ListViewPreferences {
    const stored = localStorage.getItem(key);
    if (stored) {
      return JSON.parse(stored) as ListViewPreferences;
    }
    return {
      viewMode: 'table',
      itemsPerPage: 10,
      gridColumns: 4,
    };
  }

  saveViewPreferences(key: string, preferences: Partial<ListViewPreferences>): void {
    const current = this.getViewPreferences(key);
    const updated = { ...current, ...preferences };
    localStorage.setItem(key, JSON.stringify(updated));
  }
}
