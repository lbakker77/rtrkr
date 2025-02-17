import { patchState, signalStore, withState } from "@ngrx/signals";
import { ShareConfig, User } from "../../data/retracker.model"
import { computed, inject, Injectable } from "@angular/core";
import { RetrackerService } from "../../data/retracker.service";
import { rxMethod } from "@ngrx/signals/rxjs-interop";
import { finalize, pipe, switchMap, tap } from "rxjs";
import { AuthStore } from "../../../core/service/auth.store";
import { NotificationService } from "../../../shared/service/notification.service";

interface ShareStoreModel {
    id: string,
    shareConfigsLoaded: boolean,
    shareConfigs: ShareConfig[],
    knownUsers: User[],
    knownUsersLoaded: boolean,
    loading: boolean
}

const initialState: ShareStoreModel = {
    id: "",
    shareConfigsLoaded: false,
    shareConfigs: [],
    knownUsers: [],
    knownUsersLoaded: false,
    loading: false
}


@Injectable()
export class ShareStore extends signalStore(
  { protectedState: false },
  withState(initialState)) {
    private retrackerService = inject(RetrackerService);
    private notificationService = inject(NotificationService);
    authStore = inject(AuthStore);
    

    loadShareConfig = rxMethod<string>(pipe(
            tap((listId) => patchState(this, { loading: true, shareConfigsLoaded: false, id: listId})), 
            switchMap((listId) => this.retrackerService.getShareInfos(listId).pipe(
                tap(shareConfigs =>   patchState(this, { shareConfigs })),
                finalize(() => patchState(this, { shareConfigsLoaded: true, loading: false })))
            )
    ));


    loadKnowUsers = rxMethod<string>(pipe(
        tap((listId) => patchState(this, { loading: true, knownUsersLoaded: false, id: listId})), 
        switchMap((listId) => this.retrackerService.getKnownUsersToShareWith(listId).pipe(
            tap(knownUsers =>   patchState(this, { knownUsers })),
            finalize(() => patchState(this, { knownUsersLoaded: true, loading: false })))
        )
    ));

    shareWithUser = rxMethod<string>(pipe(        
        tap((email) => patchState(this, { loading: true })), 
        switchMap((email) => this.retrackerService.shareWithUser(this.id(), email).pipe(
            tap(() => {
                this.notificationService.open($localize `List geteilt mit ${email}` );
                this.loadShareConfig(this.id());
            }),
            finalize(() => patchState(this, { loading: false })))
        )
    ));

    deleteAccess = rxMethod<string>(pipe(        
        tap((userId) => patchState(this, { loading: true })), 
        switchMap((userId) => this.retrackerService.shareListDeleteAccess(this.id(), userId).pipe(
            tap(() => {
                this.loadShareConfig(this.id());
                this.notificationService.open($localize `Zugriff auf Liste entfernt` );
            }),
            finalize(() => patchState(this, { loading: false })))
        )
    ));

    loaded = computed(() => this.shareConfigsLoaded() && this.knownUsersLoaded());

    isAdmin = computed(() => this.authStore.email() === this.shareConfigs().find(c => c.isOwner)?.email);

  }