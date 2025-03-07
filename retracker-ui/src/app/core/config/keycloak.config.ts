import { AutoRefreshTokenService, createInterceptorCondition, INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG, IncludeBearerTokenCondition, provideKeycloak, UserActivityService, withAutoRefreshToken } from "keycloak-angular";
import { environment } from "../../../environments/environment";

const urlCondition = createInterceptorCondition<IncludeBearerTokenCondition>({
  urlPattern: /^http:\/\/localhost:4200\/.*/i,
  bearerPrefix: 'Bearer'
});
const urlCondition2 = createInterceptorCondition<IncludeBearerTokenCondition>({
  urlPattern: /^.*/i,
  bearerPrefix: 'Bearer'
});

export const provideKeycloakConfig = () => provideKeycloak({
    config: {
      url: environment.keyCloakUrl,
      realm: 'retracker',
      clientId: 'retracker-web-ui'
    },
    initOptions: {
      onLoad: 'check-sso',
      silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
      redirectUri: window.location.origin + '/'
    },
    features: [
      withAutoRefreshToken({
        onInactivityTimeout: 'logout',
        sessionTimeout: 60000
      })
    ],
    providers: [AutoRefreshTokenService, UserActivityService]

  });

export const provideHttpBearerInterceptor = () => { 
    return {
    provide: INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG,
    useValue: [urlCondition, urlCondition2]
    }
};