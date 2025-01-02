import { ChangeDetectionStrategy, Component, computed, ElementRef, inject, input, output, signal } from '@angular/core';
import { RetrackerOverviewEntry } from '../../data/retracker.model';
import {MatCardModule} from '@angular/material/card';
import { DatePipe, NgClass } from '@angular/common';
import { MatIcon } from '@angular/material/icon';
import { FocusableOption, FocusOrigin } from '@angular/cdk/a11y';
import { RecurranceConfigViewComponent } from "../shared/recurrance-config-view/recurrance-config-view.component";
import { DueDateViewComponent } from "../shared/due-date-view/due-date-view.component";
import { UniqueSelectionDispatcher } from '@angular/cdk/collections';
import { ResponsivnessService } from '../../../shared/service/responsivness.service';
import { ResponsiveClassDirective } from '../../../shared/component/responsive-class.directive';
import { CategoryIconComponent } from '../shared/category-icon/category-icon.component';

@Component({
  selector: 'app-retracker-overview-entry-select',
  imports: [MatCardModule, DatePipe,  RecurranceConfigViewComponent, DueDateViewComponent, ResponsiveClassDirective, CategoryIconComponent],
  templateUrl: './retracker-overview-entry-select.component.html',
  styleUrl: './retracker-overview-entry-select.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class RetrackerOverviewEntrySelectComponent {
  responsivenessService = inject(ResponsivnessService);
  selectionDispatcher = inject(UniqueSelectionDispatcher);
  element = inject(ElementRef);
  
  item = input.required<RetrackerOverviewEntry>();
  selected = output<string>();
  imSelected = input.required<boolean>();

  dueIn = computed(() => {
    if (this.item().dueDate) {
      const msDiff = this.item().dueDate!.valueOf() - Date.now().valueOf();
      return Math.ceil(msDiff / 1000 / 3600 / 24);
    }  
    return null;
  });
  overDueDays = computed(() => this.dueIn() ? Math.abs(this.dueIn()!) : null);

  constructor() {
    this.selectionDispatcher.listen((id, name) => {
      if (id === RetrackerOverviewEntrySelectComponent.name){
        const itsMe = name === this.item().id;
        // this.imSelected.set(name === this.item().id);
      }
    })
  }

  select() {
    //this.imSelected.set(true);
    this.selected.emit(this.item().id);
    // this.selectionDispatcher.notify(RetrackerOverviewEntrySelectComponent.name, this.item().id);
  }
}
