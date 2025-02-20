import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { ResponsiveClassDirective } from '../../shared/component/responsive-class.directive';
import { GlobalSearchService } from './global-search.service';

@Component({
  selector: 'app-global-search', 
  imports: [MatIconModule, FormsModule, MatButtonModule, ResponsiveClassDirective],
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
