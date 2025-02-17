import { ChangeDetectionStrategy, Component, inject, model, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { provideNativeDateAdapter } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';

export interface ManualPostponeDateDialogData {
  minDate: Date;
}


@Component({
  selector: 'app-manual-postpone-date',
  imports: [MatDialogModule, MatDatepickerModule, MatButtonModule],
  templateUrl: './manual-postpone-date.component.html',
  styleUrl: './manual-postpone-date.component.scss',
   providers: [provideNativeDateAdapter()],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ManualPostponeDateComponent { 
  private readonly dialogRef = inject(MatDialogRef<ManualPostponeDateComponent>);
  private readonly data = inject<ManualPostponeDateDialogData>(MAT_DIALOG_DATA);
  selected = model<Date | null>(null);
  readonly minDate = this.data.minDate;

  datePicked() {
    this.dialogRef.close(this.selected());
  } 
}

