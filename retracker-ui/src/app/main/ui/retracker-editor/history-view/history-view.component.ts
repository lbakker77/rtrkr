import { ChangeDetectionStrategy, Component, computed, inject, input } from '@angular/core';
import { TaskHistory } from '../../../data/task.model';
import { DatePipe } from '@angular/common';
import { MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { TaskEditorStore } from '../task-editor.store';

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

  private store = inject(TaskEditorStore);
  
  historyElements = input<TaskHistory[]>();


  deleteLastEntry() {
    this.store.undoLastCompletion()
    }
  
}
