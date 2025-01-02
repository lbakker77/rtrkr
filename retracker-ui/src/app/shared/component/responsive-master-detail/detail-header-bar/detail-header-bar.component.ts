import { ChangeDetectionStrategy, Component, inject, output } from '@angular/core';
import { MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { ResponsiveMasterDetailService } from '../responsive-master-detail.service';

@Component({
  selector: 'app-detail-header-bar',
  imports: [MatIcon, MatIconButton],
  templateUrl: './detail-header-bar.component.html',
  styleUrl: './detail-header-bar.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DetailHeaderBarComponent {
  responsivMasterDetail = inject(ResponsiveMasterDetailService);

  closeDetail() {
    this.responsivMasterDetail?.closeDetail();
  }
}
