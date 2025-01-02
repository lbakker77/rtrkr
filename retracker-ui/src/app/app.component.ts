import { Component, inject, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { RetrackerListsNavComponent } from "./retracker/ui/retracker-lists-nav/retracker-lists-nav.component";
import { MainnavComponent } from './core/mainnav/mainnav.component';
import { ResponsivnessService } from './shared/service/responsivness.service';

@Component({
  selector: 'app-root',
  imports: [MainnavComponent,RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  responsivnesService = inject(ResponsivnessService);

  title = 'retracker';
}
