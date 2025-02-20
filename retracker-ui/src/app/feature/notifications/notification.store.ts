import { computed, inject, Injectable } from "@angular/core";
import { UserNotification } from "./notification.model"
import { patchState, signalStore, withState } from "@ngrx/signals";
import { rxMethod } from "@ngrx/signals/rxjs-interop";
import { pipe, switchMap, tap } from "rxjs";
import { NotificationService } from "./notification.service";

interface NotificationsModel {
    notifications: UserNotification[],
    loading: boolean
}

const initialState: NotificationsModel = {
    notifications: [],
    loading: false,
};


@Injectable({
    providedIn: 'root' 
  })
export class NotificationStore extends signalStore({ protectedState: false }, withState(initialState)) {
    private nofiticationService = inject(NotificationService);
    
    loadNotifications = rxMethod<void>(pipe(
        tap(() => patchState(this, { loading: true })),
        switchMap(() => this.nofiticationService.loadNotifications().pipe(
            tap(notifications => patchState(this, { notifications, loading: false })),
        )),
    ));

    markAsRead = rxMethod<string>(pipe(
        tap((notificationId) => patchState(this, { loading: true })),
        switchMap((notificationId) => this.nofiticationService.markAsRead(notificationId).pipe(
            tap(() => this.loadNotifications()),
        )),
    ));

    count = computed(() => this.notifications().length);
}