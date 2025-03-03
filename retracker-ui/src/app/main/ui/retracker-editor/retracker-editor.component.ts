import { ChangeDetectionStrategy, Component, computed, effect, inject, input, output, signal } from '@angular/core';
import { TaskStore } from '../../data/task.store';
import { TaskEditorStore } from './task-editor.store';
import { OverviewTask } from '../../data/task.model';
import { DetailHeaderBarComponent } from '../../../shared/component/responsive-master-detail/detail-header-bar/detail-header-bar.component';
import { DisplayValueComponent } from '../../../shared/component/display-value/display-value.component';
import { MatButton, MatIconButton } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatIcon } from '@angular/material/icon';
import { RetrackerEditorBasedataComponent } from "./retracker-editor-basedata/retracker-editor-basedata.component";
import { DisplayLabelDirective } from "../../../shared/component/display-value/display-label.directive";
import { DisplayContentDirective } from "../../../shared/component/display-value/display-content.directive";
import { RecurranceConfigViewComponent } from "../shared/recurrance-config-view/recurrance-config-view.component";
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ManualCompletionDateComponent, ManualCompletionDateDialogData } from './manual-completion-date/manual-completion-date.component';
import { DatePipe } from '@angular/common';
import { HistoryViewComponent } from "./history-view/history-view.component";
import { ManualPostponeDateComponent } from './manual-postpone-date/manual-postpone-date.component';
import { addDays, todayAsDate } from '../../../core/utils/date.utils';
import { ResponsiveDialogService } from '../../../shared/component/responsive-dialog.service';
import { ManualDuedateComponent } from './manual-duedate/manual-duedate.component';
import { MatProgressBarModule } from '@angular/material/progress-bar';

@Component({
  selector: 'app-retracker-editor',
  imports: [DatePipe, DetailHeaderBarComponent, DisplayValueComponent, MatButton, MatMenuModule, MatIcon, MatIconButton, RetrackerEditorBasedataComponent, DisplayLabelDirective, DisplayContentDirective, RecurranceConfigViewComponent, MatDialogModule, HistoryViewComponent, HistoryViewComponent, MatProgressBarModule],
  templateUrl: './retracker-editor.component.html',
  styleUrl: './retracker-editor.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush, 
  providers: [TaskEditorStore] 
})
export class RetrackerEditorComponent {


  readonly dialog = inject(MatDialog);
  readonly responsiveDialogService = inject(ResponsiveDialogService);
  store = inject(TaskEditorStore);
  overviewStore = inject(TaskStore);
  id = input.required<string | undefined>();
  entryChange = output<OverviewTask>();
  deleted = output<string>();



  constructor() {
    effect(() => {
      const id = this.id();
      if (typeof id === 'string') {
        this.store.loadAndSelectById(id);
      } else {
        this.store.unselect();
      }
    });

    effect(() => {
      if (this.store.lastChange()) {
        this.entryChange.emit(this.store.selectedEntry()!);
      }
    });

    effect(() => {
      if (this.store.isDeleted()) {
        this.deleted.emit(this.id()!);
      }
    });
  }

  delete() {
    const idToDelete = this.store.selectedEntry()!.id;
    this.store.delete(idToDelete);
  }

  edit() {
    this.store.enableEditMode();
  }

  completedChooseDate() {
    const lastEntryDate = this.store.selectedEntry()!.lastEntryDate;
    const minDate = lastEntryDate  ? addDays(lastEntryDate,1): undefined;
    const dialogRef = this.responsiveDialogService.open(ManualCompletionDateComponent, { data:  { minDate } as ManualCompletionDateDialogData  });
    dialogRef.afterClosed().subscribe((result: Date | null) => { 
      if (result) {
        this.store.markDoneAt(result);
      }
    });
  }

  completedYesterday() {
    this.store.markDoneYesterDay();
  }
  completedToday() {
    this.store.markDoneToday();
  }

  postponeChooseDate() {
    const minDate = this.store.selectedEntry()!.dueDate  ? addDays(this.store.selectedEntry()!.dueDate!, 1): undefined;  // Start from the next day after due date.  // TODO: Consider adding a day delay based on the recurrence config.  // TODO: Add an option to choose a different postponement date.  // TODO: Add an option to choose a different postponement duration.  // TODO: Add
    const dialogRef = this.responsiveDialogService.open(ManualPostponeDateComponent, { data:  { minDate } as ManualCompletionDateDialogData  });
    dialogRef.afterClosed().subscribe((result: Date | null) => {
      if (result) {
        this.store.postponeTil(result);
      }
    });
  }
  postponeOneWeek() {
    this.store.postponeOneWeek();
  }
  postponeOneDay() {
    this.store.postponeOneDay();
  }

  setManualDueDate() {
    const dialogRef = this.responsiveDialogService.open(ManualDuedateComponent);
    dialogRef.afterClosed().subscribe((result: Date | null) => {
      if (result) {
        this.store.setManualDueDate(result);
      }
    });
  }
}
