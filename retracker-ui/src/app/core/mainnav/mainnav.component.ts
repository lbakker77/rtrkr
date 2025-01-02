import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { SecodaryNavService } from '../../shared/service/secodaryNav.service';
import { GlobalSearchComponent } from "./global-search/global-search.component";

@Component({
  selector: 'app-mainnav',
  imports: [MatToolbarModule, MatButtonModule, MatIconModule, GlobalSearchComponent],
  templateUrl: './mainnav.component.html',
  styleUrl: './mainnav.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MainnavComponent { 
  secondaryNavService = inject(SecodaryNavService);

}
