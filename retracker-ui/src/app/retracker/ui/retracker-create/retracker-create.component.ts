import { CATEGORIES, CreateRetrackerEntryRequest, RecurrenceTimeUnit, RetrackerList, RetrackerOverviewEntry, TIMEUNITS, UserCategory } from '../../data/retracker.model';
import { ChangeDetectionStrategy, Component, effect, inject, input, model, OnInit, output, signal } from '@angular/core';
import {FormControl, FormGroup, FormGroupDirective, FormsModule, NgForm, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonToggleModule} from '@angular/material/button-toggle';
import { ErrorStateMatcher, MatOption, provideNativeDateAdapter } from '@angular/material/core';
import { MatCheckbox } from '@angular/material/checkbox';
import { MatSelect, MatSelectModule, MatSelectTrigger } from '@angular/material/select';
import { MatAutocomplete } from '@angular/material/autocomplete';
import { NgClass } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { delay, of, take } from 'rxjs';
import { CategoryIconComponent } from "../shared/category-icon/category-icon.component";
import { MatRadioModule } from '@angular/material/radio';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { RetrackerListViewStore } from '../retracker-list-view/retracker-list-view.store';
import { RetrackerService } from '../../data/retracker.service';
import { A11yModule, CdkTrapFocus } from '@angular/cdk/a11y';
import { MatIconModule } from '@angular/material/icon';
import { ErrorDisplayComponent } from '../../../shared/component/error-display/error-display.component';

@Component({
  selector: 'app-retracker-create',
  imports: [A11yModule ,ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonToggleModule, MatCheckbox, MatSelectModule, MatButtonModule, CategoryIconComponent, MatRadioModule, MatDatepickerModule, MatIconModule, ErrorDisplayComponent],
  templateUrl: './retracker-create.component.html',
  styleUrl: './retracker-create.component.scss',
  providers: [provideNativeDateAdapter()],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class RetrackerCreateComponent implements OnInit {

  service = inject(RetrackerService)
  listId = input.required<string>();
  lists = input.required<RetrackerList[]>();
  abort = output<void>();
  saved = output<RetrackerOverviewEntry>();

  retrackerForm = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.maxLength(30)]),
    configureRecurrance: new FormControl(true),
    recurrenceConfig: new FormGroup({
      recurrenceInterval: new FormControl(1, Validators.required),
      recurrenceTimeUnit: new FormControl(RecurrenceTimeUnit.WEEK)
    }),
    userCategory: new FormControl<UserCategory|undefined>(CATEGORIES[0]),
    completionChoice: new FormControl<"logCompleted" | "logLater" | "plan">('logCompleted', Validators.required),
    dueDate: new FormControl<Date>(new Date(), Validators.required),
    lastEntryDate: new FormControl<Date>(new Date(), Validators.required),
    list: new FormControl<RetrackerList|undefined>(undefined),
  });

  categories = CATEGORIES;
  timeunits = TIMEUNITS; 

  ngOnInit(): void {
    if (this.listId()) {
      const selectedList = this.lists().find((list) => list.id === this.listId());
      this.retrackerForm.controls.list.setValue(selectedList);
    } 
    else {
      this.retrackerForm.controls.list.setValue(this.lists()[0]);
    }
  }

  close() {
    this.abort.emit();
  }
  abortClicked() {
    this.abort.emit();
  }
  saveClicked() {
    if (this.retrackerForm.valid) {
      const formValue = this.retrackerForm.value;
      const request: CreateRetrackerEntryRequest = {
        listId: formValue.list?.id!,
        name: formValue.name!,
        userCategory: formValue.userCategory!, 
        recurrenceConfig: formValue.configureRecurrance ? {
          recurrenceInterval: formValue.recurrenceConfig?.recurrenceInterval!,
          recurrenceTimeUnit: formValue.recurrenceConfig?.recurrenceTimeUnit!
        } : undefined,
        dueDate: formValue.completionChoice === 'plan' ? formValue.dueDate! : undefined,
        lastEntryDate: formValue.completionChoice === 'logCompleted' ? formValue.lastEntryDate! : undefined
      };
      this.service.create(request).pipe(take(1)).subscribe(
        (response) => {
          const entry = Object.assign({id: response.id}, request) as RetrackerOverviewEntry;
          this.saved.emit(entry);
        }
      );
    }

  }

}
