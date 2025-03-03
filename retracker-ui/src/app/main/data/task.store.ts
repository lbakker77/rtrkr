import { signalStore, withState, withMethods, patchState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';

import { CreateTaskRequest, Task, OverviewTask, ChangeType } from './task.model';
import { TaskService } from './task.service';
import { computed, inject, Injectable, OnDestroy } from '@angular/core';
import { finalize, map, pipe, switchMap, tap } from 'rxjs';
import { GlobalSearchService } from '../../layout/global-search/global-search.service';
import { RetrackerUpdateService } from './retracker-update.service';
import { NotificationService } from '../../shared/service/notification.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';


interface TaskStoreContent {
    id: string,
    retrackerEntries: OverviewTask[],
    isLoading: boolean,
    search: string,
    createNewModeEnabled: boolean,
    selected: OverviewTask | undefined
}

const initialState: TaskStoreContent = {
    id: "",
    retrackerEntries: [],
    isLoading: true,
    search: "",
    createNewModeEnabled: false,
    selected: undefined
  }

@Injectable()
export class TaskStore  extends signalStore ({ protectedState: false },  withState(initialState)) {
    private retrackerService = inject(TaskService);
    private globalSearchService = inject(GlobalSearchService);
    private retrackerUpdateService = inject(RetrackerUpdateService);
    private notificationService = inject(NotificationService);
    constructor() { 
      super();
      this.globalSearchService.search.pipe(takeUntilDestroyed()).subscribe(search => patchState(this, { search }));
      this.retrackerUpdateService.taskChanges.pipe(takeUntilDestroyed()).subscribe(changes => {
        if (changes.changeType === ChangeType.DELETED) {
          this.removeSelected(changes.taskId);
        } else if (changes.changeType === ChangeType.CHANGED) {
          this.retrackerService.loadDetail(changes.taskId).subscribe(entry => {
            this.updateEntry(entry);
            if (entry.id === this.selected()?.id) {
              this.notificationService.open($localize `Der aktuelle Eintrag wurde von einem anderen Benutzer geändert` );
              patchState(this, {  selected: entry });
            }
          });
        } else if (changes.changeType === ChangeType.CREATED) {
          this.retrackerService.loadDetail(changes.taskId).subscribe(entry => this.addEntry(entry));
        }
      });
    }

    loadByListname = rxMethod<string>(pipe(
        tap((listId) => patchState(this, { isLoading: true, id: listId})), 
        switchMap((listId) => this.retrackerService.loadList(listId).pipe(
            tap(entries =>  {  
              patchState(this, {  retrackerEntries: entries.sort(this.sort)});
              this.retrackerUpdateService.setSelectedList(listId);
            }),
            finalize(() => patchState(this, { isLoading: false })))
        )
    ));

    loadAll = rxMethod<void>(pipe(
      tap(() => patchState(this, { isLoading: true, id: undefined})), 
      switchMap(() => this.retrackerService.loadAll().pipe(
          tap(entries => {
            patchState(this, { retrackerEntries: entries.sort(this.sort)});
            this.retrackerUpdateService.setSelectedList(null);
          }),
          finalize(() => patchState(this, { isLoading: false })))
      )
    ));

    private sort(e1: OverviewTask, e2: OverviewTask): number {
      if (e1.dueDate?.getTime() === e2.dueDate?.getTime()) {
        return e1.name.localeCompare(e2.name);
      }
      return (e1.dueDate?.getTime() || 0 > (e2.dueDate?.getTime() || 0)) ? 1 : -1;
    }

    entriesWithFilter = computed(() => {
      return this.retrackerEntries().filter(e => e.name.toLocaleLowerCase().indexOf(this.search().toLocaleLowerCase()) >= 0);
    });

    dueEntries = computed(() =>  {
      const due =this.entriesWithFilter().filter(e => e.dueDate != null && e.dueDate.getTime() < (new Date()).getTime())
      return due;
    });

    filterActive = computed(() => this.search().length > 0);
    overallCount = computed(() => this.retrackerEntries().length);
    filteredCount = computed(() => this.entriesWithFilter().length);

    dueEntriesCount = computed(() => this.dueEntries().length);
    
    dueNextSevenDays = computed(() => {
      const now = new Date();
      const inSevenDays = new Date(now.getFullYear(), now.getMonth(), now.getDate() + 7);
      const dueSoon = this.entriesWithFilter().filter(e => e.dueDate && e.dueDate.getTime() >= now.getTime() && e.dueDate.getTime() < inSevenDays.getTime())
      return dueSoon;
    });

    otherEntries = computed(() => {
      const now = new Date();
      const inSevenDays = new Date(now.getFullYear(), now.getMonth(), now.getDate() + 7);
      return this.entriesWithFilter().filter(e => ((e.dueDate && e.dueDate.getTime() >= inSevenDays.getTime()) || e.dueDate == null) );
    });

    selectedId = computed(() => this.selected()?.id);

    getDueCountByListId(listId: string): number {
      return this.retrackerEntries().filter(e => e.listId === listId).filter(e => e.dueDate!= null && e.dueDate.getTime() < (new Date()).getTime()).length;
    }


    removeSelected(id: string) {
      const entriesWithoutSelected = this.retrackerEntries().filter(e => e.id != id );
      patchState(this, {retrackerEntries: entriesWithoutSelected, selected: undefined});
    }

    updateEntry(entry: OverviewTask) {
      const entriesWithUpdatedEntry = this.retrackerEntries().map(e => e.id === entry.id? entry : e).sort( this.sort) ;
      patchState(this, {retrackerEntries: entriesWithUpdatedEntry.sort(this.sort)});
    }

    addEntryAndSelect(entry: OverviewTask) {
      const entriesWithUpdatedEntry = [...this.retrackerEntries(), entry];
      patchState(this, {retrackerEntries: entriesWithUpdatedEntry.sort(this.sort), selected: entry});
    }

    private addEntry(entry: OverviewTask) {
      this.retrackerService.loadDetail(entry.id).subscribe(detail => {
        const entriesWithUpdatedEntry = [...this.retrackerEntries(), detail];
        patchState(this, {retrackerEntries: entriesWithUpdatedEntry.sort(this.sort)});
      });
    }

    selectEntry(id: string) {
      const selectedEntry = this.retrackerEntries().find(e => e.id === id);
      patchState(this, {selected: selectedEntry});
    }

    unselectEntry() {
      patchState(this, {selected: undefined});
    }

    openNewEntryDialog() {
      patchState(this, {createNewModeEnabled: true});
    }

    closeNewEntryDialog() {
      patchState(this, {createNewModeEnabled: false});
    }
}

