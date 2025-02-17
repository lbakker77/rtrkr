import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { CategoryColor, RetrackerDataChangeRequest, RetrackerEntry, RetrackerList, RetrackerOverviewEntry, RecurrenceTimeUnit, MarkRetrackerEntryDoneRequest, PostponeRetrackerRequest, CreateRetrackerEntryRequest, CreatedResponse, ShareConfig, User, ShareListRequest } from './retracker.model';
import { map, Observable } from 'rxjs';
import { toISOStringDateOnly } from '../../core/utils/date.utils';

@Injectable({
  providedIn: 'root'
})
export class RetrackerService {


  private httpClient = inject(HttpClient);

  loadList(listname: string): Observable<RetrackerOverviewEntry[]>{ 
    return this.httpClient.get<RetrackerOverviewEntry[]>(`/api/retracker/entry/overview/${listname}` ).pipe(map(entries => 
       entries.map(entry => {
        return this.mapDates(entry);
       })));
  }

  loadAll(): Observable<RetrackerOverviewEntry[]>{ 
    return this.httpClient.get<RetrackerOverviewEntry[]>(`/api/retracker/entry/overview/all` ).pipe(map(entries => 
       entries.map(entry => {
        return this.mapDates(entry);
       })));
  }

  private mapDates(entry: RetrackerOverviewEntry) {
    entry.dueDate = entry.dueDate ? new Date(entry.dueDate) : undefined;
    entry.lastEntryDate = entry.lastEntryDate ? new Date(entry.lastEntryDate) : undefined;
    if (entry.recurrenceConfig && entry.recurrenceConfig.recurrenceTimeUnit) {
      entry.recurrenceConfig.recurrenceTimeUnit = RecurrenceTimeUnit[entry.recurrenceConfig.recurrenceTimeUnit];
    }
    entry.userCategory.categoryColor = entry.userCategory.categoryColor;
    return entry;
  }

  loadDetail(id: string): Observable<RetrackerEntry> {
    return this.httpClient.get<RetrackerEntry>(`/api/retracker/entry/${id}` ).pipe(map(entry => {
       entry.dueDate = entry.dueDate? new Date(entry.dueDate) : undefined;
       entry.lastEntryDate = entry.lastEntryDate? new Date(entry.lastEntryDate) : undefined;
       if(entry.recurrenceConfig && entry.recurrenceConfig.recurrenceTimeUnit) {
         entry.recurrenceConfig.recurrenceTimeUnit = RecurrenceTimeUnit[entry.recurrenceConfig.recurrenceTimeUnit];
       }
       if(entry.history) {
         entry.history = entry.history.map(historyEntry => {
           if (historyEntry.lastDueDate != null) {
            historyEntry.lastDueDate = new Date(historyEntry.lastDueDate);
           }
           if (historyEntry.completionDate!= null) {
            historyEntry.completionDate = new Date(historyEntry.completionDate);
           }
           return historyEntry;
         });
       }
       entry.userCategory.categoryColor;
       return entry;
      }));
  }


  loadLists(): Observable<RetrackerList[]> {
    const options = { 
      params: new HttpParams().set('clientDate', toISOStringDateOnly(new Date())) 
    };
    return this.httpClient.get<RetrackerList[]>("/api/retracker", options);
  }

  updateEntry(request: RetrackerDataChangeRequest): Observable<void> {
    return this.httpClient.put<void>("/api/retracker/entry", request);
  }

  markDone(request: MarkRetrackerEntryDoneRequest): Observable<void> {
    return this.httpClient.post<void>("/api/retracker/entry/done", request);
  }

  postpone(request: PostponeRetrackerRequest): Observable<void> {
    return this.httpClient.post<void>("/api/retracker/entry/postpone", request);
  }

  delete(id: string): Observable<void> {
    return this.httpClient.delete<void>(`/api/retracker/entry/${id}`);
  }

  create(request: CreateRetrackerEntryRequest): Observable<CreatedResponse> {
    return this.httpClient.post<CreatedResponse>("/api/retracker/entry", request);
  }

  undoLastCompletion(id: string): Observable<void> {
    return this.httpClient.post<void>(`/api/retracker/entry/${id}/undoLastCompletion`, {});
  }

  getShareInfos(listId: string): Observable<ShareConfig[]> {
    return this.httpClient.get<ShareConfig[]>(`/api/retracker/${listId}/share-config`);
  }

  getKnownUsersToShareWith(listId: string): Observable<User[]> {
    return this.httpClient.get<User[]>(`/api/retracker/${listId}/known-users-to-share-with`);
  }

  shareWithUser(listId: string, email: string): Observable<void> {
    return this.httpClient.post<void>(`/api/retracker/share`, { listId, email } as ShareListRequest);
  }

  shareListDeleteAccess(listId: string, userId: string): Observable<void> {
    return this.httpClient.delete<void>(`/api/retracker/${listId}/share/${userId}`);
  }

  acceptInvitation(listId: string): Observable<void> {
    return this.httpClient.post<void>(`/api/retracker/${listId}/accept-invitation`, { });
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
}
