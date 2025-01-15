import type { CanActivateFn } from '@angular/router';
import { AuthStore } from '../service/auth.store';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {
  const store = inject(AuthStore); 
  return store.isAuthenticated();
};

