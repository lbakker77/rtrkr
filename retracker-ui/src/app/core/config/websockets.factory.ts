import { rxStompConfig } from "./rx-stomp.config";
import { WebsocketService } from "./websockets.service";

export function rxStompServiceFactory() {
    const rxStomp = new WebsocketService();
    rxStomp.configure(rxStompConfig);
    return rxStomp;
  }