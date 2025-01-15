import { inject, Injectable } from '@angular/core';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { ResponsivnessService } from './responsivness.service';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private snackBar = inject(MatSnackBar);
  private responsivnessService = inject(ResponsivnessService);

  open(message: string, error: boolean = false, durationSecords: number = 3): void {
    let config : MatSnackBarConfig = { duration: durationSecords * 1000,  horizontalPosition: 'right' , verticalPosition: 'top', panelClass: error? ['error-snackbar'] : [] };
    if (this.responsivnessService.isPhoneOrTablet()) {
      config = { duration: durationSecords * 1000,  horizontalPosition: 'center' , verticalPosition: 'bottom' };
    }
    this.snackBar.open(message, undefined, config  );
  }
}
 