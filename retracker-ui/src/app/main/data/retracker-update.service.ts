import { inject, Injectable } from '@angular/core';
import { Subject, Subscription, take } from 'rxjs';
import { RetrackerList, RetrackerTask, TaskChangeEvent } from './retracker.model';
import { WebsocketService } from '../../core/config/websockets.service';
import { RetrackerService } from './retracker.service';
import { AuthStore } from '../../core/service/auth.store';

@Injectable({
  providedIn: 'root'
})
export class RetrackerUpdateService {
  private websocketService = inject(WebsocketService);
  private retrackerService = inject(RetrackerService);
  private authStore = inject(AuthStore);
  private subscriptions = new Map<string, Subscription>();

  private listSelected = false;
  private selectedListId: string | null = null;

  dueCountChanges = new Subject<{listId: string, dueCount: number}>();
  taskChanges = new Subject<TaskChangeEvent>();

  init(lists: RetrackerList[]) {
    lists.filter(l => l.shared && !this.subscriptions.has(l.id)).forEach(l => {
      const subscription = this.websocketService.watch(`/topic/retracker/${l.id}`).subscribe(data => {
        const taskChangeEvent: TaskChangeEvent = JSON.parse(data.body);
        if (taskChangeEvent.userId !== this.authStore.userId()) {
          if (this.listSelected && (this.selectedListId === l.id || this.selectedListId === null)) {
            this.taskChanges.next(taskChangeEvent);
          }else if (taskChangeEvent.dueCountChanged) {
            this.retrackerService.getDueCount(l.id).subscribe(dueCount => {
              this.dueCountChanges.next({ listId: l.id, dueCount: dueCount });
            });
          }
        }
        this.subscriptions.set(l.id, subscription);
      });
    });
    for(let listId of this.subscriptions.keys()){
      if (!lists.find(l => l.id === listId && l.shared)) {
        this.subscriptions.get(listId)?.unsubscribe();
        this.subscriptions.delete(listId);
      }
    }
  }

  setSelectedList(listId: string | null) {
    this.selectedListId = listId;
    this.listSelected = true;
  };

}
