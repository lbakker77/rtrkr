import { CATEGORY_TO_COLOR, CreateTaskRequest, RecurrenceTimeUnit, OverviewTask, TIMEUNITS, UserCategory } from '../../data/task.model';
import { RetrackerList } from "../../data/list.model";
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
import { TaskStore } from '../../data/task.store';
import { TaskService } from '../../data/task.service';
import { A11yModule, CdkTrapFocus } from '@angular/cdk/a11y';
import { MatIconModule } from '@angular/material/icon';
import { ErrorDisplayComponent } from '../../../shared/component/error-display/error-display.component';
import { UserCategoryService } from '../../data/user-category.service';

@Component({
  selector: 'app-retracker-create',
  imports: [A11yModule ,ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonToggleModule, MatCheckbox, MatSelectModule, MatButtonModule, CategoryIconComponent, MatRadioModule, MatDatepickerModule, MatIconModule, ErrorDisplayComponent],
  templateUrl: './retracker-create.component.html',
  styleUrl: './retracker-create.component.scss',
  providers: [provideNativeDateAdapter()],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class RetrackerCreateComponent implements OnInit {
  private userCategoryService = inject(UserCategoryService);
  private service = inject(TaskService)
  listId = input.required<string>();
  lists = input.required<RetrackerList[]>();
  abort = output<void>();
  saved = output<OverviewTask>();

  retrackerForm = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.maxLength(30)]),
    configureRecurrance: new FormControl(true),
    recurrenceConfig: new FormGroup({
      recurrenceInterval: new FormControl(1, Validators.required),
      recurrenceTimeUnit: new FormControl(RecurrenceTimeUnit.WEEK)
    }),
    userCategory: new FormControl<UserCategory|undefined>(undefined),
    completionChoice: new FormControl<"logCompleted" | "logLater" | "plan">('logCompleted', Validators.required),
    dueDate: new FormControl<Date>(new Date(), Validators.required),
    lastEntryDate: new FormControl<Date>(new Date(), Validators.required),
    list: new FormControl<RetrackerList|undefined>(undefined),
  });

  categories = this.userCategoryService.categoriesWithAuto;
  isReady = this.userCategoryService.isReady;
  timeunits = TIMEUNITS; 

  constructor() { 
    effect(() => {
      if (this.isReady()) {
        this.retrackerForm.patchValue({ userCategory: this.categories()![0] });
      }
    });
  }

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
      const request: CreateTaskRequest = {
        listId: formValue.list?.id!,
        name: formValue.name!,
        category: formValue.userCategory!.category, 
        recurrenceConfig: formValue.configureRecurrance ? {
          recurrenceInterval: formValue.recurrenceConfig?.recurrenceInterval!,
          recurrenceTimeUnit: formValue.recurrenceConfig?.recurrenceTimeUnit!
        } : undefined,
        dueDate: formValue.completionChoice === 'plan' ? formValue.dueDate! : undefined,
        lastEntryDate: formValue.completionChoice === 'logCompleted' ? formValue.lastEntryDate! : undefined
      };
      this.service.create(request).pipe(take(1)).subscribe(
        (response) => {
          const entry = {
            ...request,
            id: response.id,
            category: formValue.userCategory!,
          } as OverviewTask;
          this.saved.emit(entry);
        }
      );
    }

  }

}
