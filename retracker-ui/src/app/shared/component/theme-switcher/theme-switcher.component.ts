import { Component, inject } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { ColorScheme, ColorSchemePreferenceService } from '../../directives/color-scheme-preference.service';

@Component({
  selector: 'app-theme-switcher',
  standalone: true,
  imports: [MatIconModule, MatButtonModule, MatMenuModule],
  template: `
    <button mat-icon-button [matMenuTriggerFor]="menu" aria-label="Theme switcher" title="configure dark of light mode">
      <mat-icon>palette</mat-icon>
    </button>
    <mat-menu #menu="matMenu">
      <button mat-menu-item (click)="setTheme('light')">
        <mat-icon>light_mode</mat-icon>
        <span>Light</span>
      </button>
      <button mat-menu-item (click)="setTheme('dark')">
        <mat-icon>dark_mode</mat-icon>
        <span>Dark</span>
      </button>
      <button mat-menu-item (click)="setTheme('system')">
        <mat-icon>devices</mat-icon>
        <span>System</span>
      </button>
    </mat-menu>
  `,
  styles: [`
    /* Add any component-specific styles here */
  `]
})
export class ThemeSwitcherComponent {
  private colorSchemePreferenceService = inject(ColorSchemePreferenceService);

  setTheme(theme: ColorScheme) {
    this.colorSchemePreferenceService.setPreference(theme);
  }
}