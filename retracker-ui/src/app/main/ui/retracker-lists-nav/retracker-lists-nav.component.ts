import { ChangeDetectionStrategy, Component, effect, inject, resource, untracked } from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import {MatListModule} from '@angular/material/list';
import { IconbuttonComponent } from '../../../shared/component/iconbutton/iconbutton.component';
import { ActivatedRoute, Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { SecodaryNavService } from '../../../shared/service/secodaryNav.service';
import { firstValueFrom } from 'rxjs';
import { ListStore } from '../../data/list.store';
import { MatDialog } from '@angular/material/dialog';
import { ResponsiveDialogService } from '../../../shared/component/responsive-dialog.service';
import { RetrackerListCreateEditComponent, RetrackerListCreateResult } from '../retracker-list-create-edit/retracker-list-create-edit.component';

@Component({
  selector: 'app-retracker-lists-nav',
  imports: [MatSidenavModule, MatListModule, IconbuttonComponent, RouterLink, RouterOutlet],
  templateUrl: './retracker-lists-nav.component.html',
  styleUrl: './retracker-lists-nav.component.scss', 
  providers: [ListStore],
  
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class RetrackerListsNavComponent {

  secodaryNavService = inject(SecodaryNavService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private currentListId: string = '';
  store = inject(ListStore);
  private readonly dialogService = inject(ResponsiveDialogService);
  

  constructor() {
    this.store.loadLists();

    effect(() => {
      if (this.store.isInitialized()) {
        untracked(() => {
          if (this.store.lists().length > 1) {
            this.router.navigate(['all'],  { relativeTo: this.route });
          }else {
            const firstEntry = this.store.lists().at(0) ;
            this.router.navigate([firstEntry!.id],  { relativeTo: this.route });
          }
        });
      }
    });
  }


  openchanged(isOpen: boolean) {
    if (!isOpen) {
      // from backdrop click..
      this.secodaryNavService.sideNavClosed();
    }
  }

  addNewList() {
    this.secodaryNavService.closeSidenav();
    const dialogRef =  this.dialogService.open(RetrackerListCreateEditComponent);
    dialogRef.afterClosed().subscribe((result: RetrackerListCreateResult | null) => { 
      if (result) {
        this.store.loadLists();
        if (result.configureShare) {
          this.router.navigate(['share', result.listId],  { relativeTo: this.route });
        } else {
          this.router.navigate([result.listId],  { relativeTo: this.route });
        }
      }
    });
  }

}
