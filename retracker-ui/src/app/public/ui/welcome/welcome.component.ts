import { ChangeDetectionStrategy, Component, effect, inject } from '@angular/core';
import { AuthStore } from '../../../core/service/auth.store';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-welcome',
  imports: [MatButtonModule],
  templateUrl: './welcome.component.html',
  styleUrl: './welcome.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class WelcomeComponent {
  private readonly router = inject(Router);
  private readonly authStore = inject(AuthStore);
  constructor(){
    effect(() => {
      if (this.authStore.isAuthenticated()) {
        this.router.navigate(['/retracker']);
      }
    }); 
  }
 }
