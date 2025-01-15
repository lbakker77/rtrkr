import { patchState, signalStore, withState } from "@ngrx/signals";
import { RetrackerList } from "../../data/retracker.model";
import { inject, Injectable, signal, Signal, WritableSignal } from "@angular/core";
import { RetrackerService } from "../../data/retracker.service";
import { finalize, pipe, switchMap, tap } from "rxjs";
import { rxMethod } from "@ngrx/signals/rxjs-interop";

interface NavStoreContent {
    lists: RetrackerList[],
    isLoading: boolean,
    dueCountMap: Map<string, WritableSignal<number>>
}

const initialState: NavStoreContent = {
    lists: [],
    isLoading: true,
    dueCountMap: new Map(),
}

@Injectable()
export class RetrackerListsNavStore extends signalStore(
  { protectedState: false },
  withState(initialState)) {
    retrackerService = inject(RetrackerService);

    loadLists = rxMethod<void>(pipe(
        tap(() => patchState(this, { isLoading: true})), 
        switchMap(() => this.retrackerService.loadLists().pipe(
            tap(lists =>   {
                const dueCountMap = new Map<string, WritableSignal<number>>();
                this.lists().forEach(list => dueCountMap.set(list.id, signal(list.dueCount)));
                patchState(this, { lists,  dueCountMap });
        }),
            finalize(() => patchState(this, { isLoading: false })))
        )
    ));

    updateDueCount(listId: string, dueCount: number) {
        const changedEntry = this.lists().find(l => l.id === listId);
        const clone = {...changedEntry, dueCount: dueCount} as RetrackerList;
        const entriesWithoutChanged = this.lists().filter(l => l.id!== listId);
        patchState(this, {  lists: [...entriesWithoutChanged!, clone] });   
    }
  }
