import { ChangeDetectionStrategy, Component, effect, inject, input, model, output, signal } from '@angular/core';
import {FormControl, FormGroup, FormGroupDirective, FormsModule, NgForm, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonToggleModule} from '@angular/material/button-toggle';
import { ErrorStateMatcher, MatOption } from '@angular/material/core';
import { MatCheckbox } from '@angular/material/checkbox';
import { MatSelect, MatSelectTrigger } from '@angular/material/select';
import { CategoryIconComponent } from '../../shared/category-icon/category-icon.component';
import { MatButtonModule } from '@angular/material/button';
import { CATEGORY_TO_COLOR, RecurrenceTimeUnit, ChangeTaskDataRequest, TIMEUNITS, UserCategory } from '../../../data/task.model';
import { TaskEditorStore } from '../task-editor.store';
import { UserCategoryService } from '../../../data/user-category.service';


export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}

@Component({
  selector: 'app-retracker-editor-basedata',
  imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonToggleModule, MatCheckbox, MatSelect , MatOption, CategoryIconComponent, MatSelectTrigger, MatButtonModule],
  templateUrl: './retracker-editor-basedata.component.html',
  styleUrl: './retracker-editor-basedata.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
 
export class RetrackerEditorBasedataComponent {
  private store = inject(TaskEditorStore);
  userCategoryService = inject(UserCategoryService);
  editEnded = output<string | null>();


  retrackerForm = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.maxLength(30)]),
    configureRecurrance: new FormControl(true),
    recurrenceConfig: new FormGroup({
      recurrenceInterval: new FormControl(1),
      recurrenceTimeUnit: new FormControl(RecurrenceTimeUnit.YEAR)
    }),
    userCategory: new FormControl<UserCategory|undefined>(undefined)
  });

  timeunits = TIMEUNITS;
  categories = this.userCategoryService.categories;
  matcher = new ErrorStateMatcher();

  constructor() {
    effect(() => {
      const entry = this.store.selectedEntry();
      if (!entry) return;
      if (!this.userCategoryService.isReady()) return;

      const selectedCategory = this.categories()!.find(c => c.category === entry.category.category);
      this.retrackerForm.patchValue({
        name: entry.name,
        userCategory: selectedCategory,
        recurrenceConfig: entry.recurrenceConfig,
        configureRecurrance: entry.recurrenceConfig != null
      });
    });
  }

  formToRetrackerDataChangeRequest(): ChangeTaskDataRequest {
    const { name, configureRecurrance, recurrenceConfig, userCategory } = this.retrackerForm.value;
    return {
      id: this.store.selectedEntry()!.id,
      name: name!,
      category: userCategory!.category,
      recurrenceConfig: configureRecurrance ? { recurrenceInterval: recurrenceConfig?.recurrenceInterval!, recurrenceTimeUnit: recurrenceConfig?.recurrenceTimeUnit!} : undefined
    };
  }

  save() {
    this.store.updateData(this.formToRetrackerDataChangeRequest());
  }

  abort() {
    this.store.abortEdit()
  }

}
