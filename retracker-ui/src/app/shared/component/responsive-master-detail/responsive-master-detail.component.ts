import { AfterContentInit, ChangeDetectionStrategy, Component, computed, contentChild, effect, ElementRef, inject, Signal, signal, viewChild, ViewEncapsulation } from '@angular/core';
import { ResponsivnessService } from '../../service/responsivness.service';
import {CdkDrag, CdkDragDrop, CdkDragEnd, CdkDragMove, CdkDragStart, CdkDropList} from '@angular/cdk/drag-drop';
import { MatIcon } from '@angular/material/icon';
import { MatIconButton } from '@angular/material/button';
import { DetailHeaderBarComponent } from './detail-header-bar/detail-header-bar.component';
import { ResponsiveMasterDetailService } from './responsive-master-detail.service';



@Component({
  selector: 'app-responsive-master-detail',
  imports: [CdkDrag, CdkDropList],
  templateUrl: './responsive-master-detail.component.html',
  styleUrl: './responsive-master-detail.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [ResponsiveMasterDetailService]
})
export class ResponsiveMasterDetailComponent {
  private readonly DEFAULT_WIDTH = "66%";  
  private masterDiv = viewChild.required<ElementRef<HTMLDivElement>>('master');
  private containerDiv = viewChild.required<ElementRef<HTMLDivElement>>('container');
  private masterDivWidthDragStart = 0;
  private containerWidthStart = 0;

  service = inject(ResponsiveMasterDetailService);


  masterWidth = signal<string>(this.DEFAULT_WIDTH);
  dividerPosition = signal<string>(this.DEFAULT_WIDTH);


  constructor() {
    effect(() => {
      if (this.service.isMobileMode()){
        this.masterWidth.set("100%");
      }else {
        this.masterWidth.set(this.DEFAULT_WIDTH);
        this.dividerPosition.set(this.DEFAULT_WIDTH);
      }
    });
  }


  moved($event: CdkDragMove<any>) {
    if (this.masterDivWidthDragStart === 0) {
      this.containerWidthStart = this.containerDiv().nativeElement.getBoundingClientRect().width!;
      this.masterDivWidthDragStart = this.masterDiv().nativeElement.getBoundingClientRect().width!;
    }
    const newMasterWidth =  this.masterDivWidthDragStart + $event.source.getFreeDragPosition().x;
    const percentage = (newMasterWidth / this.containerWidthStart * 100) + "%";
    this.masterWidth.set(percentage);
  }

  ended($event: CdkDragEnd<any>) {
    this.dividerPosition.set(this.masterWidth());
    $event.source.reset();
    this.masterDivWidthDragStart = 0;  
  }

  openDetail() {
    this.service.openDetail();
  }

  closeDetail() {
    this.service.closeDetail();
  }
 }
