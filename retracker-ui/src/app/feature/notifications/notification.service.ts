import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { UserNotification } from './notification.model';
import { map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private readonly httpClient = inject(HttpClient);


  loadNotifications(): Observable<UserNotification[]>{ 
    return this.httpClient.get<UserNotification[]>(`/api/notification` ).pipe(map(notifications => 
       notifications.map(notification => {
        notification.sentAt ? new Date(notification.sentAt) : undefined;
        return notification;
       })));
  }

  markAsRead(notificationId: string): Observable<void> {
    return this.httpClient.post<void>(`/api/notification/${notificationId}/mark-as-read`, {});
  }


}
