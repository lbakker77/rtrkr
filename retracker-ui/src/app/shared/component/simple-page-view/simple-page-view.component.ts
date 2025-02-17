import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-simple-page-view',
  imports: [],
  templateUrl: './simple-page-view.component.html',
  styleUrl: './simple-page-view.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SimplePageViewComponent { }
