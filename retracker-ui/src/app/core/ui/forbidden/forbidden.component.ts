import { ChangeDetectionStrategy, Component } from '@angular/core';
import { SimplePageViewComponent } from '../../../shared/component/simple-page-view/simple-page-view.component';

@Component({
  selector: 'app-forbidden',
  imports: [SimplePageViewComponent],
  templateUrl: './forbidden.component.html',
  styleUrl: './forbidden.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ForbiddenComponent { }
