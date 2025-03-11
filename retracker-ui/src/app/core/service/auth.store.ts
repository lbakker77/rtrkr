import { effect, inject, Injectable } from "@angular/core";
import { patchState, signalStore, withState } from "@ngrx/signals";
import { KEYCLOAK_EVENT_SIGNAL, KeycloakEventType, ReadyArgs, typeEventArgs } from "keycloak-angular";
import Keycloak from 'keycloak-js';
import { UserService } from "./user.service";
import { pipe, switchMap, tap } from "rxjs";
import { rxMethod } from "@ngrx/signals/rxjs-interop";

interface AuthStoreModel {
    isAuthenticated: boolean,
    firstName: string | undefined,
    lastName: string | undefined,
    email: string | undefined,
    userId: string | undefined,
    ready: boolean
}

const initialState: AuthStoreModel = 
{
    isAuthenticated: false,
    firstName: undefined,
    lastName: undefined,
    email: undefined,
    userId: undefined,
    ready: false
  }

@Injectable({providedIn: "root"} )
export class AuthStore extends signalStore({ protectedState: false },withState(initialState)) {
  private readonly keycloak = inject(Keycloak);
  private readonly userService = inject(UserService);
  private readonly keycloakSignal = inject(KEYCLOAK_EVENT_SIGNAL);

  constructor() {
    super();
    effect(() => {
        const keycloakEvent = this.keycloakSignal();
        if (keycloakEvent.type === KeycloakEventType.AuthSuccess) {
            
            const keycloakUser = this.keycloak.idTokenParsed;
            patchState(this, {isAuthenticated: true, firstName: keycloakUser?.["given_name"], lastName: keycloakUser?.["family_name"], email: keycloakUser?.["email"]});
        }

        if (keycloakEvent.type === KeycloakEventType.Ready) {
            const authenticated = typeEventArgs<ReadyArgs>(keycloakEvent.args);

            if(authenticated){
                const keycloakUser = this.keycloak.idTokenParsed;
                
                patchState(this, {firstName: keycloakUser?.["given_name"], lastName: keycloakUser?.["family_name"], email: keycloakUser?.["email"], isAuthenticated: false});
                this.getUserId();
            }
            else {
                patchState(this, {isAuthenticated: false, firstName: undefined, lastName: undefined, email: undefined, ready: true});
            }

        }
        if (keycloakEvent.type === KeycloakEventType.AuthLogout) {
            patchState(this, {isAuthenticated: false, firstName: undefined, lastName: undefined, email: undefined});   
        }
      });
      effect(() => {
        if (this.userId()) {
          patchState(this, { isAuthenticated: true });
        }});
    }


    getUserId = rxMethod<void>(pipe(
      switchMap(() => this.userService.getOrCreateUserId().pipe(
        tap(userId => patchState(this, { userId, ready: true })))
      )));

    login() 
    {
      this.keycloak.login();
    }
  
    logout() {
      this.keycloak.logout();
    }
  
    register() {
      this.keycloak.register();
    }
}
