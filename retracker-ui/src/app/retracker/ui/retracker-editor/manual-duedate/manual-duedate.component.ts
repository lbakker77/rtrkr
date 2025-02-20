import { ChangeDetectionStrategy, Component, inject, model } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { provideNativeDateAdapter } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-manual-duedate',
  imports: [MatDatepickerModule, MatDialogModule, MatButtonModule],
  templateUrl: './manual-duedate.component.html',
  styleUrl: './manual-duedate.component.scss',
    providers: [provideNativeDateAdapter()],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ManualDuedateComponent {
  private readonly dialogRef = inject(MatDialogRef<ManualDuedateComponent>);
  selected = model<Date | null>(null); 
  minDate: Date =  new Date((new Date()).getFullYear(), (new Date()).getMonth(), (new Date()).getDate());

  datePicked() {
    this.dialogRef.close(this.selected());
  } 
 }
