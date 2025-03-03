import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { toISOStringDateOnly } from '../../core/utils/date.utils';
import { CreatedResponse } from '../../shared/data/response.model';
import { User } from '../../shared/data/user.model';
import { RetrackerList, ShareConfig, ShareListRequest } from './list.model';
import { OverviewTask, RecurrenceTimeUnit, CATEGORY_TO_COLOR, CategoryColor, Task, UserCategory, ChangeTaskDataRequest, MarkTaskDoneRequest, PostponeTaskRequest, CreateTaskRequest } from './task.model';

@Injectable({
  providedIn: 'root'
})
export class ListService {
  private httpClient = inject(HttpClient);

  loadLists(): Observable<RetrackerList[]> {
    const options = {
      params: new HttpParams().set('clientDate', toISOStringDateOnly(new Date()))
    };
    return this.httpClient.get<RetrackerList[]>("/api/retracker", options);
  }

  createList(name: string, icon: string): Observable<CreatedResponse> {
    return this.httpClient.post<CreatedResponse>("/api/retracker", { name, icon });
  }

  updateList(id: string, name: string, icon: string) {
    return this.httpClient.put<CreatedResponse>("/api/retracker", { name, icon, id });
  }

  deleteList(listId: string) {
    return this.httpClient.delete(`/api/retracker/${listId}`);
  }

  getDueCount(listId: string): Observable<number> {
    return this.httpClient.get<number>(`/api/retracker/${listId}/due-count`);
  }

  getShareInfos(listId: string): Observable<ShareConfig[]> {
    return this.httpClient.get<ShareConfig[]>(`/api/retracker/${listId}/share`);
  }

  shareWithUser(listId: string, email: string): Observable<void> {
    return this.httpClient.post<void>(`/api/retracker/share`, { listId, email } as ShareListRequest);
  }

  shareListDeleteAccess(listId: string, userId: string): Observable<void> {
    return this.httpClient.delete<void>(`/api/retracker/${listId}/share/${userId}`);
  }

  acceptInvitation(listId: string): Observable<void> {
    return this.httpClient.post<void>(`/api/retracker/${listId}/accept-invitation`, {});
  }

  getKnownUsersToShareWith(listId: string): Observable<User[]> {
    return this.httpClient.get<User[]>(`/api/retracker/${listId}/known-users-to-share-with`);
  }
}


