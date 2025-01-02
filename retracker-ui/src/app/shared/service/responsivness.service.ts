import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { computed, inject, Injectable, signal } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ResponsivnessService {
  private breakPointObserver = inject(BreakpointObserver); 
  private destroy$ = new Subject<void>();
 
  private isDesktopSignal = signal(false);
  private isPhoneOrTabletSignal = signal(false);
  private isTabletSignal = signal(false);
  private isPhoneSignal = signal(false);

  isPhone = this.isPhoneSignal.asReadonly();
  isTablet = this.isTabletSignal.asReadonly();
  isPhoneOrTablet = computed(() => this.isPhone() || this.isTablet());
  isDesktop = computed(() => !this.isPhone() && !this.isTablet());

  constructor() { 
    this.breakPointObserver.observe([Breakpoints.Small, Breakpoints.XSmall, Breakpoints.Medium]).pipe(takeUntil(this.destroy$)).subscribe(result => {
        const isPhone = result.breakpoints[Breakpoints.XSmall] && !result.breakpoints[Breakpoints.Small];
        const isTablet = !isPhone && result.breakpoints[Breakpoints.Small] && !result.breakpoints[Breakpoints.Medium];
        this.isPhoneSignal.set(isPhone);
        this.isTabletSignal.set(isTablet);
      });
  }

  stopService() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
