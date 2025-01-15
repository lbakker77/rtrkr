import { HttpEvent, HttpEventType, HttpHandlerFn, HttpRequest, HttpStatusCode } from "@angular/common/http";
import { inject } from "@angular/core";
import { Router } from "@angular/router";
import { Observable, tap } from "rxjs";
import { NotificationService } from "../../shared/service/notification.service";

export function accessDeniedInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> {
    return next(req).pipe(tap(event => {

      if (event.type === HttpEventType.ResponseHeader && event.status === HttpStatusCode.Unauthorized ) {

        const router = inject(Router);
        const notificationService = inject(NotificationService);
        notificationService.open('Access Denied', true, 10  );
        router.navigate(['/']);
        console.log(req.url, 'returned a response with status', event.status);
      }
    }));
  }