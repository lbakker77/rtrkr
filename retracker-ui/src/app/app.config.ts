import { ApplicationConfig, ErrorHandler, LOCALE_ID, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { timezoneInterceptor } from './core/interceptor/timezone.interceptor';

import { includeBearerTokenInterceptor } from 'keycloak-angular';
import { accessDeniedInterceptor } from './core/interceptor/access-denied.interceptor';
import { ErrorStateMatcher } from '@angular/material/core';
import { RetrackerErrorStateMatcher } from './core/utils/retracker-error-state-matcher';
import { errorHandlerInterceptor } from './core/interceptor/error-handler.interceptor';
import { rxStompServiceFactory } from './core/config/websockets.factory';
import { WebsocketService } from './core/config/websockets.service';
import { provideHttpBearerInterceptor, provideKeycloakConfig } from './core/config/keycloak.config';



export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }), 
    provideRouter(routes), 
    provideAnimationsAsync(), 
    provideHttpClient(withInterceptors([timezoneInterceptor, includeBearerTokenInterceptor, accessDeniedInterceptor, errorHandlerInterceptor])),  
    {provide: LOCALE_ID, useValue: 'de-DE'},
    provideKeycloakConfig(),
    provideHttpBearerInterceptor(), 
    {
      provide: WebsocketService,
      useFactory: rxStompServiceFactory,
    },
    {provide: ErrorStateMatcher, useClass: RetrackerErrorStateMatcher},
    ErrorHandler 
  ]
};
