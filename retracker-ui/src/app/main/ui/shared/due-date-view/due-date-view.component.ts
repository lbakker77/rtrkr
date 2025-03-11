import { DatePipe } from '@angular/common';
import { ChangeDetectionStrategy, Component, computed, input } from '@angular/core';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-due-date-view',
  imports: [MatIcon, DatePipe],
  templateUrl: './due-date-view.component.html',
  styleUrl: './due-date-view.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DueDateViewComponent { 
  dueDate = input<Date|null>();
  showIcon = input(true);

  dueIn = computed(() => {
    if (this.dueDate()) {
      const now = new Date();
      const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
      const msDiff = this.dueDate()!.getTime() - today.getTime();
      const dayFloat = msDiff / 1000 / 3600 / 24;
      const dueDays =  Math.floor(dayFloat);
      return dueDays;
    }  
    return null; 
  });

  overDueDays = computed(() => this.dueIn() ? Math.abs(this.dueIn()!) : null);
}
