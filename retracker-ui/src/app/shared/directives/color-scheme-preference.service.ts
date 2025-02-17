import { Injectable, signal, computed } from '@angular/core';

export type ColorScheme = 'light' | 'dark' | 'system';

@Injectable({
  providedIn: 'root'
})
export class ColorSchemePreferenceService {
  private readonly STORAGE_KEY = 'colorSchemePreference';
  private preferenceSignal = signal<ColorScheme>(this.getStoredPreference());

  preference = this.preferenceSignal.asReadonly();

  constructor() {}

  private getStoredPreference(): ColorScheme {
    return (localStorage.getItem(this.STORAGE_KEY) as ColorScheme) || 'system';
  }

  setPreference(preference: ColorScheme): void {
    localStorage.setItem(this.STORAGE_KEY, preference);
    this.preferenceSignal.set(preference);
  }
}