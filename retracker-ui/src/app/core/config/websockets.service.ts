import { inject, Injectable } from '@angular/core';
import { RxStomp } from '@stomp/rx-stomp';
import Keycloak from 'keycloak-js';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService extends RxStomp {
  private readonly keycloak = inject(Keycloak);


  constructor() { 
    super();
  }
  
  activateWithToken() {
    this.configure({ connectHeaders: { Authorization: `Bearer ${this.keycloak.token}` } });
    this.activate();
  }
}
