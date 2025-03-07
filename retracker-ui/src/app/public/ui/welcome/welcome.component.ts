import { ChangeDetectionStrategy, Component, effect, inject } from '@angular/core';
import { AuthStore } from '../../../core/service/auth.store';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { ResponsiveClassDirective } from '../../../shared/component/responsive-class.directive';
import { SimplePageViewComponent } from '../../../shared/component/simple-page-view/simple-page-view.component';

@Component({
  selector: 'app-welcome',
  imports: [MatButtonModule,ResponsiveClassDirective, SimplePageViewComponent], 
  templateUrl: './welcome.component.html',
  styleUrl: './welcome.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class WelcomeComponent {
  private readonly router = inject(Router);
  private readonly authStore = inject(AuthStore);
  constructor(){
    effect(() => {
      if (this.authStore.isAuthenticated() && this.authStore.userId() && this.router.url === '/welcome'  || this.router.url === '/') {
        console.log('User is authenticated and is already on the welcome page');
        this.router.navigate(['/retracker']);
      }
    }); 
  }

  register() {
    this.authStore.register();
  }
 }
