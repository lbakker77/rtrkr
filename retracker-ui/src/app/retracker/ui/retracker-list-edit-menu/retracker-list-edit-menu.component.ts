import { ChangeDetectionStrategy, Component, computed, inject, input } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { RetrackerListViewStore } from '../retracker-list-view/retracker-list-view.store';
import { RetrackerListsNavStore } from '../retracker-lists-nav/retracker-lists-nav.store';
import { MatIconButton } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { ShareListComponent, ShareListDialogData } from '../share-list/share-list.component';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ResponsiveDialogService } from '../../../shared/component/responsive-dialog.service';
import { RetrackerListCreateEditComponent, RetrackerListCreateResult } from '../retracker-list-create-edit/retracker-list-create-edit.component';

@Component({
  selector: 'app-retracker-list-edit-menu',
  imports: [MatIconButton, MatIcon, MatMenuModule ],
  templateUrl: './retracker-list-edit-menu.component.html',
  styleUrl: './retracker-list-edit-menu.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class RetrackerListEditMenuComponent {
  private readonly store = inject(RetrackerListsNavStore);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly responsiveDialogService = inject(ResponsiveDialogService);

  listId = input.required<string>();
  list = computed(() => this.store.lists().filter((list) => list.id === this.listId())[0]);
  canEdit = computed(() => !this.list().defaultList);


  editList() { 
       const dialogRef =  this.responsiveDialogService.open(RetrackerListCreateEditComponent, { data: this.list()});
       dialogRef.afterClosed().subscribe((result: RetrackerListCreateResult | null) => { 
        this.store.loadLists();
      });
  }
  deleteList() { 
    this.responsiveDialogService.showConfirmation("Liste löschen", "Möchten sie diese Liste wirklich löschen?").afterClosed().subscribe((result) => {
      if (result) {
        this.store.deleteList(this.listId());
      }
    });
  }
  openShareConfigDialog() {
    this.router.navigate([ '../share', this.listId()  ], { relativeTo: this.route }  );
  }
}
