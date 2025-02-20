import { ChangeDetectionStrategy, Component, effect, inject } from '@angular/core';
import { MatBadgeModule } from '@angular/material/badge';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { NotificationStore } from '../notification.store';
import { Dialog,  DialogModule } from '@angular/cdk/dialog';
import { UserNotificationsListComponent } from './user-notifications-list/user-notifications-list.component';
import { AuthStore } from '../../../core/service/auth.store';

@Component({
  selector: 'app-user-notifications',
  imports: [MatButtonModule, MatIconModule, MatBadgeModule, DialogModule],
  templateUrl: './user-notifications.component.html',
  styleUrl: './user-notifications.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
  export class UserNotificationsComponent {
    readonly store = inject(NotificationStore);
    readonly authStore = inject(AuthStore);
    readonly dialog = inject(Dialog)

    constructor() { 
      effect(() => {
        if (this.authStore.isAuthenticated()) {
          this.store.loadNotifications();
        }
      });
    }
    viewNotifications() {
      const dialogRef = this.dialog.open(UserNotificationsListComponent, {
        panelClass: 'side-dialog',
      });

      dialogRef.closed.subscribe(() => {
        // Handle dialog close if needed
      });
    }
 

}
