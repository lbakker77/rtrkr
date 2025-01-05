import { patchState, signalStore, withState } from "@ngrx/signals";
import { RetrackerList } from "../../data/retracker.model";
import { inject, Injectable } from "@angular/core";
import { RetrackerService } from "../../data/retracker.service";
import { finalize, pipe, switchMap, tap } from "rxjs";
import { rxMethod } from "@ngrx/signals/rxjs-interop";

interface NavStoreContent {
    lists: RetrackerList[],
    isLoading: boolean,
    dueCountMap: Map<string, number>
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
                const dueCountMap = new Map<string, number>();
                this.lists().forEach(list => dueCountMap.set(list.id, list.dueCount));
                patchState(this, { lists,  dueCountMap });
        }),
            finalize(() => patchState(this, { isLoading: false })))
        )
    ));

    updateDueCount(listId: string, dueCount: number) {
        const newMap =  this.dueCountMap().set(listId, dueCount);
        patchState(this, {  dueCountMap: newMap });   
    }
  }
