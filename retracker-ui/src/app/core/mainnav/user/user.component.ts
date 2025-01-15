import { ChangeDetectionStrategy, Component, computed, effect, inject, signal } from '@angular/core';
import { MatButton, MatIconButton } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import {  MatTooltipModule } from '@angular/material/tooltip';
import { AuthStore } from '../../service/auth.store';

@Component({
  selector: 'app-user',
  imports: [MatIconButton, MatButton, MatIconModule, MatMenuModule, MatTooltipModule],
  templateUrl: './user.component.html',
  styleUrl: './user.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UserComponent {

  authStore = inject(AuthStore);

  loginInfo = computed(() => this.authStore.firstName() +' ' + this.authStore.lastName());

  login() 
  {
    this.authStore.login();
  }

  logout() {
    this.authStore.logout();
  }

  register() {
    this.authStore.register();
  }

  openProfile() {
    throw new Error('Method not implemented.');
  }

 }
