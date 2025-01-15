import { ApplicationConfig, LOCALE_ID, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { timezoneInterceptor } from './core/interceptor/timezone.interceptor';

import { provideKeycloak, includeBearerTokenInterceptor, createInterceptorCondition, IncludeBearerTokenCondition, INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG } from 'keycloak-angular';
import { accessDeniedInterceptor } from './core/interceptor/access-denied.interceptor';

const urlCondition = createInterceptorCondition<IncludeBearerTokenCondition>({
  urlPattern: /^http:\/\/localhost:4200\/.*/i,
  bearerPrefix: 'Bearer'
});
const urlCondition2 = createInterceptorCondition<IncludeBearerTokenCondition>({
  urlPattern: /^.*/i,
  bearerPrefix: 'Bearer'
});

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }), 
    provideRouter(routes), 
    provideAnimationsAsync(), 
    provideHttpClient(withInterceptors([timezoneInterceptor, includeBearerTokenInterceptor, accessDeniedInterceptor])),  
    {provide: LOCALE_ID, useValue: 'de-DE'},
    provideKeycloak({
      config: {
        url: 'http://localhost:8081',
        realm: 'retracker',
        clientId: 'retracker-web-ui'
      },
      initOptions: {
        onLoad: 'check-sso',
        silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
        redirectUri: window.location.origin + '/'
      }
    }),
    {
      provide: INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG,
      useValue: [urlCondition, urlCondition2]
    }
  ]
};
