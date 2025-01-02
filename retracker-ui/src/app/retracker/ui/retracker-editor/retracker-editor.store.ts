import { computed, inject, Injectable } from "@angular/core";
import { MarkRetrackerEntryDoneRequest, PostponeRetrackerRequest, RetrackerDataChangeRequest, RetrackerEntry } from "../../data/retracker.model";
import { patchState, signalStore, withState } from "@ngrx/signals";
import { rxMethod } from "@ngrx/signals/rxjs-interop";
import { finalize, pipe, Subject, switchMap, takeUntil, tap } from "rxjs";
import { RetrackerService } from "../../data/retracker.service";
import { NotificationService } from "../../../shared/service/notification.service";


interface RetrackerDetailStoreContent {
    isLoading: boolean,
    selectedEntry: RetrackerEntry | undefined
    updating: boolean;
    isInEditMode: boolean;
    hasChanged: boolean;
    isDeleted: boolean;
}

const initialState: RetrackerDetailStoreContent = {
    isLoading: true,
    selectedEntry: undefined,
    updating: false,
    isInEditMode: false,
    hasChanged: false,
    isDeleted: false
  }

@Injectable()
export class RetrackerEditorStore extends signalStore({ protectedState: false },withState(initialState)) {
    private cancelPendigLoad$ = new Subject<void>();
    private retrackerService = inject(RetrackerService);
    private notificationService = inject(NotificationService);


    loadAndSelectById = rxMethod<string>(pipe(
      tap(() => {
        this.cancelPendigLoad$.next();
        patchState(this, { isLoading: true, isInEditMode: false, updating: false, hasChanged: false, isDeleted: false });
      }), 
      switchMap((id) => this.retrackerService.loadDetail(id).pipe(
        takeUntil(this.cancelPendigLoad$),
        tap(entry => patchState(this, { selectedEntry: entry})),
        finalize(() => patchState(this, { isLoading: false })))
    ) ));


    updateData = rxMethod<RetrackerDataChangeRequest>(pipe(
      tap(() => patchState(this, { updating: true })),
      switchMap((request) => this.retrackerService.updateEntry(request).pipe(
        tap(() => {
          patchState(this, { isInEditMode: false, hasChanged: true });
          this.notificationService.open($localize `Eintrag erfolgreich aktualisiert` );
          this.loadAndSelectById(request.id); 
        }),
        finalize(() => patchState(this, { updating: false })))
    ) ));

    hasSelectedEntry = computed(() => 
    {
      const hasValue = this.selectedEntry() !== undefined;
      return hasValue;
    });

    unselect() {
        patchState(this, { selectedEntry: undefined, isLoading: false, updating: false, isInEditMode: false  });
    }

    private markDone = rxMethod<MarkRetrackerEntryDoneRequest>(pipe(
      tap(() => patchState(this, { updating: true })),
      switchMap((request) => this.retrackerService.markDone(request).pipe(
        tap(() => {
          patchState(this, { isInEditMode: false, hasChanged: true });
          this.notificationService.open($localize `${this.selectedEntry()?.name} erledigt` );
          this.loadAndSelectById(request.id); 
        }),
        finalize(() => patchState(this, { updating: false })))
    ) ));

    markDoneToday() {
      const now = new Date();
      const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
      this.markDone({id: this.selectedEntry()!.id, doneAt: today  });
    }

    markDoneYesterDay() {
      const now = new Date();
      const yesterday = new Date(now.getFullYear(), now.getMonth(), now.getDate() -1);
      this.markDone({id: this.selectedEntry()!.id, doneAt: yesterday  });
    }

    markDoneAt(date: Date) {
      this.markDone({id: this.selectedEntry()!.id, doneAt: date  });
    }

    private postpone = rxMethod<PostponeRetrackerRequest>(pipe(
      tap(() => patchState(this, { updating: true })),
      switchMap((request) => this.retrackerService.postpone(request).pipe(
        tap(() => {
          patchState(this, { isInEditMode: false, hasChanged: true });
          this.notificationService.open($localize `${this.selectedEntry()?.name} erfolgreich verschoben` );
          this.loadAndSelectById(request.id); 
        }),
        finalize(() => patchState(this, { updating: false })))
    ) ));


    postponeOneDay() {
      const currentDueDate =  this.selectedEntry()?.dueDate;
      if (currentDueDate) {
        const oneDayLaster = new Date(currentDueDate.getFullYear(), currentDueDate.getMonth(), currentDueDate.getDate() + 1);
        this.postpone({id: this.selectedEntry()!.id, postponedDate: oneDayLaster  });
      }
    }

    postponeOneWeek() {
      const currentDueDate =  this.selectedEntry()?.dueDate;
      if (currentDueDate) {
        const oneWeekLaster = new Date(currentDueDate.getFullYear(), currentDueDate.getMonth(), currentDueDate.getDate() + 7);
        this.postpone({id: this.selectedEntry()!.id, postponedDate: oneWeekLaster  });
      }
    }

    postponeTil(date: Date) {
      this.postpone({id: this.selectedEntry()!.id, postponedDate: date  });
    }


    enableEditMode() {
      patchState(this, { isInEditMode: true});
    }

    abortEdit() {
      patchState(this, { isInEditMode: false});
    }

    delete = rxMethod<string>(pipe(
      tap(() => patchState(this, { updating: true })),
      switchMap((id) => this.retrackerService.delete(id).pipe(
        tap(() => {
          patchState(this, { isInEditMode: false, hasChanged: true, isDeleted: true });
          this.notificationService.open($localize `${this.selectedEntry()?.name} erfolgtreich gelöscht` );
        }),
        finalize(() => patchState(this, { updating: false })))
    ) ));

}

