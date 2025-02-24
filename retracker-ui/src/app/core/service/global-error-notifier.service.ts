import { inject, Injectable } from '@angular/core';
import { ErrorResponse } from '../interceptor/error-handler.interceptor';
import { ErrorDispatcherService } from './error-dispatcher.service';
import { NotificationService } from '../../shared/service/notification.service';

@Injectable({
  providedIn: 'root'
})
export class GlobalErrorNotifierService {
  errorDispatcherService = inject(ErrorDispatcherService);
  readonly notificationService = inject(NotificationService); 
  private isEnabled = false;
  enable() {
    if (!this.isEnabled) {
      this.isEnabled = true;
      this.errorDispatcherService.registerConsumer(this);
    }
  }

  consume(error: ErrorResponse, status: number | undefined): void {
    if (error.violations?.length > 0) {
      this.notificationService.open("Es ist ein Fehler aufgetreten: " + error.violations.map(v => v.message).join(', '), true, 5);
    } else {
      this.notificationService.open("Es ist ein Fehler aufgetreten.", true, 5);
    }

  }
}
