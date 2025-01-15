import { ChangeDetectionStrategy, Component, computed, input } from '@angular/core';
import { RetrackerHistory } from '../../../data/retracker.model';

interface HistoryViewModel {
  dueDate?: Date;
  completionDate: Date;
  postponedDays?: number;

  showPlannedLine: boolean;
  showPostponedLine: boolean;
  showOverdueLine: boolean;

  plannedLinePercentage: number;
  postponedLinePercentage: number;
  overdueLinePercentage: number;
}
@Component({
  selector: 'app-history-view',
  imports: [],
  templateUrl: './history-view.component.html',
  styleUrl: './history-view.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class HistoryViewComponent {
  historyElements = input<RetrackerHistory[]>();


  
  
}
