import { patchState, signalStore, withState } from "@ngrx/signals";
import { RetrackerList } from "./list.model";
import { computed, effect, inject, Injectable, signal, Signal, untracked, WritableSignal } from "@angular/core";
import { ListService } from './list.service';
import { finalize, pipe, switchMap, tap } from "rxjs";
import { rxMethod } from "@ngrx/signals/rxjs-interop";
import { Router } from "@angular/router";
import { NotificationService } from "../../shared/service/notification.service";
import { RetrackerUpdateService } from "./retracker-update.service";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";

interface ListStoreContent {
    lists: RetrackerList[],
    isLoading: boolean,
    isInitialized: boolean
}

const initialState: ListStoreContent = {
    lists: [],
    isLoading: true,
    isInitialized: false
}
@Injectable()
export class ListStore extends signalStore(
  { protectedState: false },
  withState(initialState)) {
    listService = inject(ListService);
    nofificationService = inject(NotificationService);
    retrackerUpdateService = inject(RetrackerUpdateService);

    constructor() { 
        super();
        this.retrackerUpdateService.dueCountChanges.pipe(takeUntilDestroyed()).subscribe(update => {
            this.updateDueCount(update.listId, update.dueCount);
        });
    }

    router = inject(Router);

    loadLists = rxMethod<void>(pipe(
        tap(() => patchState(this, { isLoading: true})), 
        switchMap(() => this.listService.loadLists().pipe(
            tap(lists =>   {
                const sortedLists = this.sortLists(lists);
                patchState(this, { lists });
                this.retrackerUpdateService.init(lists);
        }),
            finalize(() => patchState(this, { isLoading: false, isInitialized: true })))
        )
    ));

    reloadDueCount = rxMethod<string>(pipe(
        switchMap((listId) => this.listService.getDueCount(listId).pipe(
            tap((response) => {
                this.updateDueCount(listId, response);
            })
        )
    )));

    deleteList = rxMethod<string>(pipe(
        switchMap((listId) => this.listService.deleteList(listId).pipe(
            tap(() => {         
                this.loadLists();
                this.nofificationService.open('Liste erfolgreich gelöscht');  
                this.router.navigate(['/']);
            })
        ))
    ));

    acceptInvitation = rxMethod<string>(pipe(
        tap((listId) => patchState(this, { isLoading: true })),
        switchMap((listId) => this.listService.acceptInvitation(listId).pipe(
            tap(() => {
                this.loadLists();
                this.router.navigate(['retracker', listId]);
            }),
            finalize(() => patchState(this, { isLoading: false })))
        )
    ));

    overallDueCount = computed(() => {
        return this.lists().map(list => list.dueCount).reduce((a, b) => a + b, 0);
    });

    private sortLists(lists: RetrackerList[]) {
        return lists.sort((a, b) => {
            if (a.defaultList) return -1;
            return a.name.localeCompare(b.name);
        });
    }

    updateDueCount(listId: string, dueCount: number) {
        const changedEntry = this.lists().find(l => l.id === listId);
        const clone = {...changedEntry, dueCount: dueCount} as RetrackerList;
        const entriesWithoutChanged = this.lists().filter(l => l.id!== listId);
        
        patchState(this, {  lists: this.sortLists([...entriesWithoutChanged!, clone]) });   
    }
  }
