import { computed, Directive, inject, input } from '@angular/core';
import { ResponsivnessService } from '../service/responsivness.service';

export type ResponsiveCssClasses = "desktop" |"phone" | "tablet" | "phoneOrTablet"


@Directive({
  selector: '[responsiveClass]',
  host: {
    '[class.desktop]': 'isDesktop()', 
    '[class.phone]': 'isPhone()', 
    '[class.tablet]': 'isTablet()', 
    '[class.phoneOrTablet]': 'isPhoneOrTablet()', 
  },
})
export class ResponsiveClassDirective {
  responsiveClass = input.required<ResponsiveCssClasses[]>();
  responsivnessService = inject(ResponsivnessService);

  isDesktop = computed(() => this.responsiveClass().includes("desktop")  && this.responsivnessService.isDesktop());
  isPhone = computed(() => this.responsiveClass().includes("phone") && this.responsivnessService.isPhone());
  isTablet = computed(() => this.responsiveClass().includes("tablet") && this.responsivnessService.isTablet());
  isPhoneOrTablet = computed(() => this.responsiveClass().includes("phoneOrTablet") && this.responsivnessService.isPhoneOrTablet());



}


