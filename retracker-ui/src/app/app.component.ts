import { Component, inject, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { RetrackerListsNavComponent } from "./main/ui/retracker-lists-nav/retracker-lists-nav.component";
import { MainnavComponent } from './layout/mainnav/mainnav.component';
import { ResponsivnessService } from './shared/service/responsivness.service';
import { ColorSchemeClassDirective } from './shared/directives/color-scheme-class.directive';
import { GlobalErrorNotifierService } from './core/service/global-error-notifier.service';

@Component({
  selector: 'app-root',
  imports: [MainnavComponent,RouterOutlet, ColorSchemeClassDirective],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  responsivnesService = inject(ResponsivnessService);
  private readonly globalErrorNotifierService = inject(GlobalErrorNotifierService);

  constructor() {
    this.globalErrorNotifierService.enable();
  }

  title = 'retracker';
}
