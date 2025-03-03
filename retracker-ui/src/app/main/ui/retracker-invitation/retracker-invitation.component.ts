import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { ListStore } from '../../data/list.store';
import { SimplePageViewComponent } from '../../../shared/component/simple-page-view/simple-page-view.component';

@Component({
  selector: 'app-retracker-invitation',
  standalone: true,
  imports: [CommonModule, MatButtonModule, SimplePageViewComponent],
  templateUrl: './retracker-invitation.component.html',
  styleUrls: ['./retracker-invitation.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class RetrackerInvitationComponent {
  private route = inject(ActivatedRoute);
  private store = inject(ListStore);
  private router = inject(Router);

  listId = this.route.snapshot.paramMap.get('listId');
  list = computed(() => this.store.lists().find(list => list.id === this.listId));

  acceptInvitation() {
    this.store.acceptInvitation(this.listId!);
  }

  refuseInvitation() {
    this.router.navigate(['/']);
  }
}