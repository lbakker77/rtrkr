import { Injectable } from '@angular/core';
import { ErrorHandler, ErrorResponse } from '../interceptor/error-handler.interceptor';

export interface ErrorConsumer {
  consume(error: ErrorResponse, status: number|undefined): void;
}

@Injectable({
  providedIn: 'root'
})
export class ErrorDispatcherService implements ErrorHandler {

  private consumers: ErrorConsumer[] = [];

  registerConsumer(consumer: ErrorConsumer): void {
    this.consumers.push(consumer);
  }

  removeConsumer(consumer: ErrorConsumer): void {
    const index = this.consumers.indexOf(consumer);
    if (index > -1) {
      this.consumers.splice(index, 1);
    }
  }

  handleError(error: ErrorResponse, status: number|undefined): void {
    if (this.consumers.length > 0) {
      this.consumers[this.consumers.length - 1].consume(error, status);
    } 
  }

}
