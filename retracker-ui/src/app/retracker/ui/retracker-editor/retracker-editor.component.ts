import { ChangeDetectionStrategy, Component, effect, inject, input, output, signal } from '@angular/core';
import { RetrackerListViewStore } from '../retracker-list-view/retracker-list-view.store';
import { RetrackerEditorStore } from './retracker-editor.store';
import { RetrackerOverviewEntry } from '../../data/retracker.model';
import { DetailHeaderBarComponent } from '../../../shared/component/responsive-master-detail/detail-header-bar/detail-header-bar.component';
import { DisplayValueComponent } from '../../../shared/component/display-value/display-value.component';
import { MatButton, MatIconButton } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatIcon } from '@angular/material/icon';
import { RetrackerEditorBasedataComponent } from "./retracker-editor-basedata/retracker-editor-basedata.component";
import { DisplayLabelDirective } from "../../../shared/component/display-value/display-label.directive";
import { DisplayContentDirective } from "../../../shared/component/display-value/display-content.directive";
import { DueDateViewComponent } from '../shared/due-date-view/due-date-view.component';
import { RecurranceConfigViewComponent } from "../shared/recurrance-config-view/recurrance-config-view.component";
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ManualCompletionDateComponent } from './manual-completion-date/manual-completion-date.component';
import { DatePipe } from '@angular/common';
import { HistoryViewComponent } from "./history-view/history-view.component";

@Component({
  selector: 'app-retracker-editor',
  imports: [DatePipe, DetailHeaderBarComponent, DisplayValueComponent, MatButton, MatMenuModule, MatIcon, MatIconButton, RetrackerEditorBasedataComponent, DisplayLabelDirective, DisplayContentDirective, DueDateViewComponent, RecurranceConfigViewComponent, MatDialogModule, HistoryViewComponent, HistoryViewComponent],
  templateUrl: './retracker-editor.component.html',
  styleUrl: './retracker-editor.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [RetrackerEditorStore]
})
export class RetrackerEditorComponent {
  readonly dialog = inject(MatDialog);
  store = inject(RetrackerEditorStore);
  overviewStore = inject(RetrackerListViewStore);

  id = input.required<string | undefined>();

  entryChange = output<RetrackerOverviewEntry>();
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
      if (this.store.selectedEntry()) {
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

  doneChooseDate() {
    const dialogRef = this.dialog.open(ManualCompletionDateComponent);
    dialogRef.afterClosed().subscribe((result: Date | null) => {
      if (result) {
        this.store.markDoneAt(result);
      }
    });
  }

  doneYesterday() {
    this.store.markDoneYesterDay();
  }
  doneToday() {
    this.store.markDoneToday();
  }

  postponeChooseDate() {
    const dialogRef = this.dialog.open(ManualCompletionDateComponent);
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


}
