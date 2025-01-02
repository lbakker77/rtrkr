import { ChangeDetectionStrategy, Component, computed, input } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { RecurrenceConfig } from '../../../data/retracker.model';

@Component({
  selector: 'app-recurrance-config-view',
  imports: [MatIcon],
  templateUrl: './recurrance-config-view.component.html',
  styleUrl: './recurrance-config-view.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class RecurranceConfigViewComponent { 
  recurrance = input<RecurrenceConfig | null>();
  showIcon = input(true);
}
 