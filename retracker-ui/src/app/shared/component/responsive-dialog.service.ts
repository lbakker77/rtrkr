import { Injectable, inject } from '@angular/core';
import { MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import { ComponentType } from '@angular/cdk/portal';
import { ResponsivnessService } from '../service/responsivness.service';
import { ConfirmationDialogComponent, ConfirmationDialogData } from './confirmation-dialog/confirmation-dialog.component';

@Injectable({
  providedIn: 'root'
})
export class ResponsiveDialogService {
  private dialog = inject(MatDialog);
  private responsivnessService = inject(ResponsivnessService);

  open<T, D = any, R = any>(component: ComponentType<T>, config?: MatDialogConfig<D>): MatDialogRef<T, R> {
    const isSmallScreen = this.responsivnessService.isPhoneOrTablet();

    const dialogConfig: MatDialogConfig = {
      ...config,
      width: isSmallScreen ? '100%' : (config?.width || ''),
      height: isSmallScreen ? '100%' : (config?.height || ''),
      maxWidth: isSmallScreen ? '100vw' : (config?.maxWidth || ''),
      maxHeight: isSmallScreen ? '100vh' : (config?.maxHeight || ''),
      panelClass: isSmallScreen ? ['full-screen-dialog', ...(config?.panelClass || [])] : config?.panelClass,
    };

    return this.dialog.open(component, dialogConfig);
  }

  showConfirmation< D = any, R = any>(title: string, message: string): MatDialogRef<ConfirmationDialogComponent, R> {
    return this.open(ConfirmationDialogComponent, { data: {title, message} as ConfirmationDialogData} );
  }
}