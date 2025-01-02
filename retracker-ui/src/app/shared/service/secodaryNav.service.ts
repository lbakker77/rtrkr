import { effect, inject, Injectable, signal } from '@angular/core';
import { ResponsivnessService } from './responsivness.service';

@Injectable({
  providedIn: 'root'
})
export class SecodaryNavService {

  private responsivnesService = inject(ResponsivnessService);
  private isOpenSignal = signal(true);

  isOpen = this.isOpenSignal.asReadonly();
  isToggleEnabled = this.responsivnesService.isPhoneOrTablet;

  constructor() { 
    effect(() => {
      if (this.isToggleEnabled()) {
        this.isOpenSignal.set(false);
      }else {
        this.isOpenSignal.set(true);
      }
    });
  }

  toogleSideNav() {
    this.isOpenSignal.set(!this.isOpenSignal());
  }

  sideNavClosed() {
    if (this.isToggleEnabled()){
      this.isOpenSignal.set(false);
    }
  }

  closeSidenav() {
    if (this.isToggleEnabled()){
      this.isOpenSignal.set(false);
    }
  }
}
