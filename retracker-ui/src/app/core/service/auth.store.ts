import { effect, inject, Injectable } from "@angular/core";
import { patchState, signalStore, withState } from "@ngrx/signals";
import { KEYCLOAK_EVENT_SIGNAL, KeycloakEventType, ReadyArgs, typeEventArgs } from "keycloak-angular";
import Keycloak from 'keycloak-js';

interface AuthStoreModel {
    isAuthenticated: boolean,
    firstName: string | undefined,
    lastName: string | undefined,
    email: string | undefined,
}

const initialState: AuthStoreModel = 
{
    isAuthenticated: false,
    firstName: undefined,
    lastName: undefined,
    email: undefined,
  }

@Injectable({providedIn: "root"} )
export class AuthStore extends signalStore({ protectedState: false },withState(initialState)) {
private readonly keycloak = inject(Keycloak);

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
                patchState(this, {isAuthenticated: true, firstName: keycloakUser?.["given_name"], lastName: keycloakUser?.["family_name"], email: keycloakUser?.["email"]});
            }
            else {
                patchState(this, {isAuthenticated: false, firstName: undefined, lastName: undefined, email: undefined});
            }

        }
        if (keycloakEvent.type === KeycloakEventType.AuthLogout) {
            patchState(this, {isAuthenticated: false, firstName: undefined, lastName: undefined, email: undefined});   
        }
      });
    }

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
