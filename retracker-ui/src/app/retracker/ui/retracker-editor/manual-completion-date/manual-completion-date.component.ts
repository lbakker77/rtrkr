import { ChangeDetectionStrategy, Component, inject, model } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { provideNativeDateAdapter } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-manual-completion-date',
  imports: [MatCardModule, MatDatepickerModule],
  templateUrl: './manual-completion-date.component.html',
  styleUrl: './manual-completion-date.component.scss',
  providers: [provideNativeDateAdapter()],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ManualCompletionDateComponent {
  dialogRef = inject(MatDialogRef<ManualCompletionDateComponent>);
  selected = model<Date | null>(null);
  maxDate: Date =  new Date((new Date()).getFullYear(), (new Date()).getMonth(), (new Date()).getDate());

  datePicked() {
    this.dialogRef.close(this.selected());
  } 
}
