import { ChangeDetectionStrategy, Component, input } from '@angular/core';

@Component({
  selector: 'app-display-value',
  imports: [],
  templateUrl: './display-value.component.html',
  styleUrl: './display-value.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DisplayValueComponent { 

}
