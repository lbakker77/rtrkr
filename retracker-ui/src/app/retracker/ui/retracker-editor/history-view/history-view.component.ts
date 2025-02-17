import { ChangeDetectionStrategy, Component, computed, inject, input } from '@angular/core';
import { RetrackerHistory } from '../../../data/retracker.model';
import { DatePipe } from '@angular/common';
import { MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { RetrackerEditorStore } from '../retracker-editor.store';

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
  imports: [DatePipe, MatIconButton, MatIcon ],
  templateUrl: './history-view.component.html',
  styleUrl: './history-view.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class HistoryViewComponent {

  private store = inject(RetrackerEditorStore);
  
  historyElements = input<RetrackerHistory[]>();


  deleteLastEntry() {
    this.store.undoLastCompletion()
    }
  
}
