import { ChangeDetectionStrategy, Component, effect, inject, resource } from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import {MatListModule} from '@angular/material/list';
import { IconbuttonComponent } from '../../../shared/component/iconbutton/iconbutton.component';
import { ActivatedRoute, Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { SecodaryNavService } from '../../../shared/service/secodaryNav.service';
import { RetrackerService } from '../../data/retracker.service';
import { firstValueFrom } from 'rxjs';
import { RetrackerListsNavStore } from './retracker-lists-nav.store';

@Component({
  selector: 'app-retracker-lists-nav',
  imports: [MatSidenavModule, MatListModule, IconbuttonComponent, RouterLink, RouterOutlet],
  templateUrl: './retracker-lists-nav.component.html',
  styleUrl: './retracker-lists-nav.component.scss', 
  providers: [RetrackerListsNavStore],
  
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class RetrackerListsNavComponent {
  secodaryNavService = inject(SecodaryNavService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private currentListId: string = '';
  store = inject(RetrackerListsNavStore);
  

  constructor() {
    this.store.loadLists();

    effect(() => {
      if (this.store.lists() != null && this.store.lists().length > 0) {
        const firstEntry = this.store.lists().at(0) ;
        if (this.currentListId !== firstEntry!.id) { 
          this.currentListId = firstEntry!.id;
          this.router.navigate(['personal', firstEntry!.id],  { relativeTo: this.route });
        }
      }
    });
  }


  openchanged(isOpen: boolean) {
    if (!isOpen) {
      // from backdrop click..
      this.secodaryNavService.sideNavClosed();
    }
  }

}
