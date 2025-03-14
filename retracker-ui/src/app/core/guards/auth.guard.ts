import type { ActivatedRouteSnapshot, CanActivateFn, RouterStateSnapshot, UrlTree } from '@angular/router';
import { AuthStore } from '../service/auth.store';
import { inject } from '@angular/core';
import { AuthGuardData, createAuthGuard, KeycloakService } from 'keycloak-angular';

const authGuardFn =  async (
  route: ActivatedRouteSnapshot,
  _: RouterStateSnapshot,
  authData: AuthGuardData
): Promise<boolean | UrlTree> => {
  const { authenticated } = authData;
  if (!authenticated) {
    const autoStore = inject(AuthStore);
    autoStore.loginAndRedirectBack();
  }
  return authenticated;
};


export const authGuard = createAuthGuard<CanActivateFn>(authGuardFn);
