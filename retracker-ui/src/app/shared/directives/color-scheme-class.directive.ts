import { Directive, inject, computed } from '@angular/core';
import { ColorSchemePreferenceService } from './color-scheme-preference.service';

@Directive({
  selector: '[colorSchemeClass]',
  standalone: true,
  host: {
    '[class.light-theme]': 'isLightTheme()',
    '[class.dark-theme]': 'isDarkTheme()',
  },
})
export class ColorSchemeClassDirective {
  private colorSchemePreferenceService = inject(ColorSchemePreferenceService);

  private systemPreference = computed(() => 
    window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'
  );

  private effectiveScheme = computed(() => {
    const preference = this.colorSchemePreferenceService.preference();
    return preference === 'system' ? this.systemPreference() : preference;
  });

  isLightTheme = computed(() => this.effectiveScheme() === 'light');
  isDarkTheme = computed(() => this.effectiveScheme() === 'dark');
}