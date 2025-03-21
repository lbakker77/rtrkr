import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { NotificationStore } from '../../notification.store';
import { DatePipe } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';
import { DialogRef } from '@angular/cdk/dialog';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-user-notifications-list',
  imports: [DatePipe, MatButtonModule, MatButtonModule, MatIcon],
  templateUrl: './user-notifications-list.component.html',
  styleUrl: './user-notifications-list.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UserNotificationsListComponent {
  private readonly router = inject(Router);
  readonly store = inject(NotificationStore);  
  private readonly dialogRef = inject(DialogRef<any>);

  markAsRead(id: string)  {
    this.store.markAsRead(id);
  } 

  performAction(action: string) {
    var command = action.split(':')[0];
    switch (command) {
      case 'show_invite':
        this.router.navigate(['retracker', "invitation", action.split(':')[1]]);
        this.dialogRef.close();
    }
  }

  
  closeDialog() {
    this.dialogRef.close();
  }
}
 