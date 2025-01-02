import { ChangeDetectionStrategy, Component, computed, input, Signal, ViewChild } from '@angular/core';
import { MatBadgeModule } from '@angular/material/badge';
import { MatRippleModule } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLinkActive } from '@angular/router';

@Component({
  selector: 'a[iconbutton]',
  imports: [MatIconModule, RouterLinkActive, MatRippleModule, MatBadgeModule],
  templateUrl: './iconbutton.component.html',
  styleUrl: './iconbutton.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class IconbuttonComponent {
  icon = input("help");
  title = input("unknown");
  badgeCounter = input(0);
  badgeHidden = computed(() => this.badgeCounter() == 0);
}
