import { HttpHandlerFn, HttpRequest } from "@angular/common/http";

export function timezoneInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn) {
    // Inject the current `AuthService` and use it to get an authentication token:
    // Clone the request to add the authentication header.
    const newReq = req.clone({
      headers: req.headers.append('X-User-Timezone', Intl.DateTimeFormat().resolvedOptions().timeZone),
    });
    return next(newReq);
  }