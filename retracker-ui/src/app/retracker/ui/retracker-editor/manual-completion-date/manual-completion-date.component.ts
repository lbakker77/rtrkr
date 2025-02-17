import { ChangeDetectionStrategy, Component, inject, model } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { provideNativeDateAdapter } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';

export interface ManualCompletionDateDialogData {
  minDate: Date;
}

@Component({
  selector: 'app-manual-completion-date',
  imports: [MatDatepickerModule, MatDialogModule, MatButtonModule],
  templateUrl: './manual-completion-date.component.html',
  styleUrl: './manual-completion-date.component.scss',
  providers: [provideNativeDateAdapter()],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ManualCompletionDateComponent {
  dialogRef = inject(MatDialogRef<ManualCompletionDateComponent>);
  private readonly data = inject<ManualCompletionDateDialogData>(MAT_DIALOG_DATA);
  selected = model<Date | null>(null); 
  maxDate: Date =  new Date((new Date()).getFullYear(), (new Date()).getMonth(), (new Date()).getDate());
  minDate = this.data.minDate;
  
  datePicked() {
    this.dialogRef.close(this.selected());
  } 
}
