import { HttpErrorResponse, HttpEvent, HttpEventType, HttpHandlerFn, HttpRequest } from "@angular/common/http";
import { inject, ProviderToken } from "@angular/core";
import { catchError, Observable, tap } from "rxjs";
import { ErrorDispatcherService } from "../service/error-dispatcher.service";

export interface ErrorResponse {
    violations: Violation[];
}
export interface Violation {
    fieldName: string;
    message: string;
}

export abstract class ErrorHandler {
    abstract handleError(error: ErrorResponse, status: number|undefined) : void;
}

export function errorHandlerInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> {
    const errorHandler = inject(ErrorDispatcherService);
    return next(req).pipe(
    tap(event => {
      if (event.type === HttpEventType.Response && event.status >= 400 && event.status < 500) {
        console.log('Error occurred:', event.body);
      }
    }),
    catchError(error => {
      if (errorHandler) {
        if (error instanceof HttpErrorResponse) {
          errorHandler.handleError(error.error, error.status);
        } else {
          errorHandler.handleError(error.error, undefined);
        }
      }


      // Rethrow the error to propagate it further
      throw error;
    })
  );
}

