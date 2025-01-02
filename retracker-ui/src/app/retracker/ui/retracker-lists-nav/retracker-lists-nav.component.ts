import { ChangeDetectionStrategy, Component, effect, inject, resource } from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import {MatListModule} from '@angular/material/list';
import { IconbuttonComponent } from '../../../shared/component/iconbutton/iconbutton.component';
import { ActivatedRoute, Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { SecodaryNavService } from '../../../shared/service/secodaryNav.service';
import { RetrackerService } from '../../data/retracker.service';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-retracker-lists-nav',
  imports: [MatSidenavModule, MatListModule, IconbuttonComponent, RouterLink, RouterOutlet],
  templateUrl: './retracker-lists-nav.component.html',
  styleUrl: './retracker-lists-nav.component.scss', 
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class RetrackerListsNavComponent {
  secodaryNavService = inject(SecodaryNavService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private retrackerService = inject(RetrackerService);

  constructor() {
    effect(() => {
      if (this.retrackerListsResource.hasValue() && this.retrackerListsResource.value()!.length > 0) {
        const firstEntry = this.retrackerListsResource.value()!.at(0) ;
        this.router.navigate(['personal', firstEntry!.id],  { relativeTo: this.route });
      }
    });
  }

  retrackerListsResource = resource({
    loader: () => {
      return firstValueFrom(this.retrackerService.loadLists());
    }
  });


  openchanged(isOpen: boolean) {
    if (!isOpen) {
      // from backdrop click..
      this.secodaryNavService.sideNavClosed();
    }
  }

}
