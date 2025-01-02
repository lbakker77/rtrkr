import { computed, effect, inject, Injectable, signal } from '@angular/core';
import { ResponsivnessService } from '../../service/responsivness.service';

@Injectable()
export class ResponsiveMasterDetailService {
  private responsivnesService = inject(ResponsivnessService);
  private isDetailOpenedSignal = signal<boolean>(false);
  isMobileMode = computed(() => this.responsivnesService.isPhoneOrTablet());
  isDetailOpened = this.isDetailOpenedSignal.asReadonly();

  constructor() {
    effect(() => {
      if (this.responsivnesService.isPhoneOrTablet()){
        this.closeDetail();
      }
    });
  }

  closeDetail() {
    this.isDetailOpenedSignal.set(false);
  }

  openDetail() {
    this.isDetailOpenedSignal.set(true);
  }
}
