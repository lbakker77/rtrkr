import { ChangeDetectionStrategy, Component, computed, inject, input, signal } from '@angular/core';
import { ErrorHandler, ErrorResponse, Violation } from '../../../core/interceptor/error-handler.interceptor';
import { ErrorConsumer, ErrorDispatcherService } from '../../../core/service/error-dispatcher.service';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-error-display',
  imports: [MatButtonModule, MatIconModule],
  templateUrl: './error-display.component.html',
  styleUrl: './error-display.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ErrorDisplayComponent implements ErrorConsumer{
  showError = signal(false)
  violations = signal<Violation[]>([])
  statusCode = signal<number|undefined>(undefined);
  globalErrorsOnly = input(true);
  showGlobalErrorsOnly = computed(() => this.globalErrorsOnly() || this.violations().length == 0);

  errorDispatcherService = inject(ErrorDispatcherService);  

  constructor() {
    this.errorDispatcherService.registerConsumer(this);
  }

  ngOnDestroy(): void {
    this.errorDispatcherService.removeConsumer(this);
  }

  consume(error: ErrorResponse, status: number|undefined): void {
    this.showError.set(true);
    this.violations.set(error?.violations || []);
    this.statusCode.set(status);  
    console.log('An error occurred:', error); 
   }
}
