import { RxStomp, RxStompConfig, StompHeaders } from '@stomp/rx-stomp';
import { environment } from '../../../environments/environment';
import cli from '@angular/cli';


export const rxStompConfig: RxStompConfig = {
    // Which server?
    brokerURL: environment.websocketBaseUrl,
  


    // How often to heartbeat?
    // Interval in milliseconds, set to 0 to disable
    heartbeatIncoming: 0, // Typical value 0 - disabled
    heartbeatOutgoing: 20000, // Typical value 20000 - every 20 seconds
  
    // Wait in milliseconds before attempting auto reconnect
    // Set to 0 to disable
    // Typical value 500 (500 milli seconds)
    reconnectDelay: 5000,
  
    // Will log diagnostics on console
    // It can be quite verbose, not recommended in production
    // Skip this key to stop logging to console
    // debug: (msg: string): void => {
    //   console.log(new Date(), msg);
    // },
  };