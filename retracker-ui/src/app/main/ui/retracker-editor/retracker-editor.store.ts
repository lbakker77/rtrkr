import { computed, inject, Injectable } from "@angular/core";
import { MarkRetrackerEntryDoneRequest, PostponeRetrackerRequest, RetrackerDataChangeRequest, RetrackerTask } from "../../data/retracker.model";
import { patchState, signalStore, withState } from "@ngrx/signals";
import { rxMethod } from "@ngrx/signals/rxjs-interop";
import { catchError, finalize, of, pipe, Subject, switchMap, takeUntil, tap } from "rxjs";
import { RetrackerService } from "../../data/retracker.service";
import { NotificationService } from "../../../shared/service/notification.service";
import { addDays, todayAsDate } from "../../../core/utils/date.utils";


interface RetrackerDetailStoreContent {
    isLoading: boolean,
    selectedEntry: RetrackerTask | undefined
    updating: boolean;
    isInEditMode: boolean;
    lastChange: Date | undefined;
    isDeleted: boolean;
}

const initialState: RetrackerDetailStoreContent = {
    isLoading: false,
    selectedEntry: undefined,
    updating: false,
    isInEditMode: false,
    lastChange: undefined,
    isDeleted: false
  }

@Injectable()
export class RetrackerEditorStore extends signalStore({ protectedState: false }, withState(initialState)) {
    private cancelPendigLoad$ = new Subject<void>();
    private retrackerService = inject(RetrackerService);
    private notificationService = inject(NotificationService);


    loadAndSelectById = rxMethod<string>(pipe(
      tap(() => {
        this.cancelPendigLoad$.next();
        patchState(this, { isLoading: true, isInEditMode: false, updating: false, lastChange: undefined, isDeleted: false });
      }), 
      switchMap((id) => this.retrackerService.loadDetail(id).pipe(
        takeUntil(this.cancelPendigLoad$),
        tap(entry => {
          if (entry.id === this.selectedEntry()?.id) {
            patchState(this, { selectedEntry: entry, lastChange: new Date() });

          } else {
            patchState(this, { selectedEntry: entry})
          }    
        }),
        catchError((e) => { patchState(this, { selectedEntry: undefined }); return of(undefined) }),
        finalize(() => patchState(this, { isLoading: false, updating: false })))
    ) ));


    updateData = rxMethod<RetrackerDataChangeRequest>(pipe(
      tap(() => patchState(this, { updating: true })),
      switchMap((request) => this.retrackerService.updateEntry(request).pipe(
        tap(() => {
          patchState(this, { isInEditMode: false });
          this.notificationService.open($localize `Eintrag erfolgreich aktualisiert` );
          this.loadAndSelectById(request.id); 
        })
      ) )));

    setManualDueDate = rxMethod<Date>(pipe(
      tap(() => patchState(this, { updating: true })),
      switchMap((dueDate) => this.retrackerService.setManualDueDate(this.selectedEntry()!.id, dueDate).pipe(
        tap(() => {
          this.notificationService.open($localize `Manuelles Fälligkeitsdatum erfolgreich gesetzt` );
          this.loadAndSelectById(this.selectedEntry()!.id); 
        })
    ) )));

    private markDone = rxMethod<MarkRetrackerEntryDoneRequest>(pipe(
      tap(() => patchState(this, { updating: true })),
      switchMap((request) => this.retrackerService.markDone(request).pipe(
        tap(() => {
          this.notificationService.open($localize `${this.selectedEntry()?.name} erledigt` );
          this.loadAndSelectById(request.id); 
        })
    ) )));

    private postpone = rxMethod<PostponeRetrackerRequest>(pipe(
      tap(() => patchState(this, { updating: true })),
      switchMap((request) => this.retrackerService.postpone(request).pipe(
        tap(() => {
          this.notificationService.open($localize `${this.selectedEntry()?.name} erfolgreich verschoben` );
          this.loadAndSelectById(request.id); 
        })
    ) )));

    delete = rxMethod<string>(pipe(
      tap(() => patchState(this, { updating: true })),
      switchMap((id) => this.retrackerService.delete(id).pipe(
        tap(() => {
          patchState(this, { isDeleted: true });
          this.notificationService.open($localize `${this.selectedEntry()?.name} erfolgtreich gelöscht` );
        })
    ) )));


    undoLastCompletion = rxMethod<void>(pipe(
      tap(() => patchState(this, { updating: true })),
      switchMap(() => this.retrackerService.undoLastCompletion(this.selectedEntry()!.id).pipe(
        tap(() => {
          this.notificationService.open($localize `Erledigung von ${this.selectedEntry()?.name} rückgängig gemacht` );
          this.loadAndSelectById(this.selectedEntry()!.id); 
        })
    ) )));

    hasSelectedEntry = computed(() => 
    {
      const hasValue = this.selectedEntry() !== undefined;
      return hasValue;
    });


    canCompleteToday = computed(() => {
      if (this.selectedEntry()?.lastEntryDate) {
        const today = todayAsDate();
        return this.selectedEntry()!.lastEntryDate!.getTime() < today.getTime();
      } else {
        return true;
      }
    });
  
    canCompleteYesterday = computed(() => {
      if (this.selectedEntry()?.lastEntryDate) {
        const yesterday = addDays(todayAsDate(), -1);
        return this.selectedEntry()!.lastEntryDate!.getTime() < yesterday.getTime();
      } else {
        return true;
      }
    });
  
    canPostpone = computed(() => {
      return !!this.selectedEntry()?.dueDate;
    });

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

    unselect() {
      patchState(this, initialState);
  }
}

