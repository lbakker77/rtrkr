import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { GlobalSearchService } from '../../service/global-search.service';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-global-search', 
  imports: [MatIconModule, FormsModule, MatButtonModule],
  templateUrl: './global-search.component.html',
  styleUrl: './global-search.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GlobalSearchComponent {

  globalSearchService = inject(GlobalSearchService);
  value: string = '';
  valChanged($event: Event) {
    this.globalSearchService.searchChanged(this.value);
  }
  clear() {
    this.value = '';
    this.globalSearchService.searchChanged(this.value);
  }
}
