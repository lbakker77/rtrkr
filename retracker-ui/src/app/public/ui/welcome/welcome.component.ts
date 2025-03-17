import { ChangeDetectionStrategy, Component, effect, ElementRef, inject, OnInit, Signal, viewChild } from '@angular/core';
import { AuthStore } from '../../../core/service/auth.store';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { ResponsiveClassDirective } from '../../../shared/component/responsive-class.directive';
import { SimplePageViewComponent } from '../../../shared/component/simple-page-view/simple-page-view.component';
import { ColorSchemeClassDirective } from '../../../shared/directives/color-scheme-class.directive';

@Component({
  selector: 'app-welcome',
  imports: [MatButtonModule,ResponsiveClassDirective, ColorSchemeClassDirective], 
  templateUrl: './welcome.component.html',
  styleUrl: './welcome.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class WelcomeComponent {
  private readonly router = inject(Router);
  readonly authStore = inject(AuthStore);
  private readonly video: Signal<ElementRef | undefined> = viewChild("video");

  constructor(){
    effect(() => {
      if (this.authStore.isAuthenticated() && this.authStore.userId() && (this.router.url === '/welcome'  || this.router.url === '/')) {
        this.router.navigate(['/retracker/all']);
      }
    }); 
    effect(() => {
      if (this.video() && this.authStore.ready() && !this.authStore.isAuthenticated()) {
          this.video()!.nativeElement.muted = true;
          this.video()!.nativeElement.play();
      }
      });;
  }

  register() {
    this.authStore.register();
  }
 }
