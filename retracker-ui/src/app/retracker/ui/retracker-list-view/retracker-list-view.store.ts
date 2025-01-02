import { signalStore, withState, withMethods, patchState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';

import { CreateRetrackerEntryRequest, RetrackerEntry, RetrackerOverviewEntry } from '../../data/retracker.model';
import { RetrackerService } from '../../data/retracker.service';
import { computed, inject, Injectable } from '@angular/core';
import { finalize, pipe, switchMap, tap } from 'rxjs';


interface RetrackerStoreContent {
    id: string,
    retrackerEntries: RetrackerOverviewEntry[],
    isLoading: boolean,
    search?: string,
    createNewModeEnabled: boolean,
    selected: RetrackerOverviewEntry | undefined
}

const initialState: RetrackerStoreContent = {
    id: "",
    retrackerEntries: [],
    isLoading: true,
    search: undefined,
    createNewModeEnabled: false,
    selected: undefined
  }

@Injectable()
export class RetrackerListViewStore extends signalStore(
  { protectedState: false },
  withState(initialState)) {
    retrackerService = inject(RetrackerService);

    loadByListname = rxMethod<string>(pipe(
        tap((listId) => patchState(this, { isLoading: true, id: listId})), 
        switchMap((listId) => this.retrackerService.loadList(listId).pipe(
            tap(entries =>   patchState(this, {  retrackerEntries: entries.sort((e1, e2) => (e1.dueDate ?? new Date(2200,1,1)) > (e2.dueDate ?? new Date(2200,1,1)) ? 1 : -1) })),
            finalize(() => patchState(this, { isLoading: false })))
        )
    ));

    dueEntries = computed(() =>  {
      const due =this.retrackerEntries().filter(e => e.dueDate != null && e.dueDate.getTime() < (new Date()).getTime())
      return due;
    });
    
    dueNextSevenDays = computed(() => {
      const now = new Date();
      const inSevenDays = new Date(now.getFullYear(), now.getMonth(), now.getDate() + 7);
      const dueSoon = this.retrackerEntries().filter(e => e.dueDate && e.dueDate.getTime() >= now.getTime() && e.dueDate.getTime() < inSevenDays.getTime())
      return dueSoon;
    });

    otherEntries = computed(() => {
      const now = new Date();
      const inSevenDays = new Date(now.getFullYear(), now.getMonth(), now.getDate() + 7);
      return this.retrackerEntries().filter(e => (e.dueDate && e.dueDate.getTime() >= inSevenDays.getTime()) || e.dueDate == null);
    });

    selectedId = computed(() => this.selected()?.id);

    removeSelected(id: string) {
      const entriesWithoutSelected = this.retrackerEntries().filter(e => e.id != id );
      patchState(this, {retrackerEntries: entriesWithoutSelected, selected: undefined});
    }

    updateEntry(entry: RetrackerOverviewEntry) {
      const entriesWithUpdatedEntry = this.retrackerEntries().map(e => e.id === entry.id? entry : e);
      patchState(this, {retrackerEntries: entriesWithUpdatedEntry});
    }

    addEntry(entry: RetrackerOverviewEntry) {
      const entriesWithUpdatedEntry = [...this.retrackerEntries(), entry];
      patchState(this, {retrackerEntries: entriesWithUpdatedEntry, selected: entry});
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

