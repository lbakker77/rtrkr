import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { CreatedResponse } from '../../shared/data/response.model';
import { OverviewTask, RecurrenceTimeUnit, CATEGORY_TO_COLOR, CategoryColor, Task, UserCategory, ChangeTaskDataRequest, MarkTaskDoneRequest, PostponeTaskRequest, CreateTaskRequest } from './task.model';


@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private httpClient = inject(HttpClient);

  loadList(listname: string): Observable<OverviewTask[]> {
    return this.httpClient.get<OverviewTask[]>(`/api/retracker/task/overview/${listname}`).pipe(map(entries => entries.map(entry => {
      return this.mapDatesAndColors(entry);
    })));
  }

  loadAll(): Observable<OverviewTask[]> {
    return this.httpClient.get<OverviewTask[]>(`/api/retracker/task/overview/all`).pipe(map(entries => entries.map(entry => {
      return this.mapDatesAndColors(entry);
    })));
  }

  private mapDatesAndColors(entry: OverviewTask) {
    entry.dueDate = entry.dueDate ? new Date(entry.dueDate) : undefined;
    entry.lastEntryDate = entry.lastEntryDate ? new Date(entry.lastEntryDate) : undefined;
    if (entry.recurrenceConfig && entry.recurrenceConfig.recurrenceTimeUnit) {
      entry.recurrenceConfig.recurrenceTimeUnit = RecurrenceTimeUnit[entry.recurrenceConfig.recurrenceTimeUnit];
    }
    entry.category.categoryColor = CATEGORY_TO_COLOR.find(category => category.category === entry.category.category)?.categoryColor || CategoryColor.SILVER;
    return entry;
  }

  loadDetail(id: string): Observable<Task> {
    return this.httpClient.get<Task>(`/api/retracker/task/${id}`).pipe(map(entry => {
      entry.dueDate = entry.dueDate ? new Date(entry.dueDate) : undefined;
      entry.lastEntryDate = entry.lastEntryDate ? new Date(entry.lastEntryDate) : undefined;
      if (entry.recurrenceConfig && entry.recurrenceConfig.recurrenceTimeUnit) {
        entry.recurrenceConfig.recurrenceTimeUnit = RecurrenceTimeUnit[entry.recurrenceConfig.recurrenceTimeUnit];
      }
      if (entry.history) {
        entry.history = entry.history.map(historyEntry => {
          if (historyEntry.lastDueDate != null) {
            historyEntry.lastDueDate = new Date(historyEntry.lastDueDate);
          }
          if (historyEntry.completionDate != null) {
            historyEntry.completionDate = new Date(historyEntry.completionDate);
          }
          return historyEntry;
        });
      }
      this.assignColor(entry.category);

      return entry;
    }));
  }

  private assignColor(userCategory: UserCategory): UserCategory {
    userCategory.categoryColor = CATEGORY_TO_COLOR.find(category => category.category === userCategory.category)?.categoryColor || CategoryColor.SILVER;
    return userCategory;
  }

  updateEntry(request: ChangeTaskDataRequest): Observable<void> {
    return this.httpClient.put<void>("/api/retracker/task", request);
  }

  markDone(request: MarkTaskDoneRequest): Observable<void> {
    return this.httpClient.post<void>("/api/retracker/task/done", request);
  }

  postpone(request: PostponeTaskRequest): Observable<void> {
    return this.httpClient.post<void>("/api/retracker/task/postpone", request);
  }

  delete(id: string): Observable<void> {
    return this.httpClient.delete<void>(`/api/retracker/task/${id}`);
  }

  create(request: CreateTaskRequest): Observable<CreatedResponse> {
    return this.httpClient.post<CreatedResponse>("/api/retracker/task", request);
  }

  undoLastCompletion(id: string): Observable<void> {
    return this.httpClient.post<void>(`/api/retracker/task/${id}/undoLastCompletion`, {});
  }

  setManualDueDate(id: string, dueDate: Date) {
    return this.httpClient.post<void>(`/api/retracker/task/${id}/set-manual-due-date`, dueDate);
  }

  getUserCategories(): Observable<UserCategory[]> {
    return this.httpClient.get<UserCategory[]>(`/api/retracker/task/categories`).pipe(map(categories => categories.map(category => this.assignColor(category))));
  }
}