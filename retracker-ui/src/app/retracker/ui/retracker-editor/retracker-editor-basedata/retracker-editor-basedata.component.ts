import { ChangeDetectionStrategy, Component, effect, inject, input, model, output, signal } from '@angular/core';
import {FormControl, FormGroup, FormGroupDirective, FormsModule, NgForm, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonToggleModule} from '@angular/material/button-toggle';
import { ErrorStateMatcher, MatOption } from '@angular/material/core';
import { MatCheckbox } from '@angular/material/checkbox';
import { MatSelect, MatSelectTrigger } from '@angular/material/select';
import { MatAutocomplete } from '@angular/material/autocomplete';
import { NgClass } from '@angular/common';
import { CategoryIconComponent } from '../../shared/category-icon/category-icon.component';
import { MatButtonModule } from '@angular/material/button';
import { CATEGORIES, CategoryColor, RecurrenceTimeUnit, RetrackerDataChangeRequest, RetrackerEntry, TIMEUNITS, UserCategory } from '../../../data/retracker.model';
import { delay, of } from 'rxjs';
import { RetrackerEditorStore } from '../retracker-editor.store';


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
  private store = inject(RetrackerEditorStore);
  editEnded = output<string | null>();


  retrackerForm = new FormGroup({
    name: new FormControl('', Validators.required),
    configureRecurrance: new FormControl(true),
    recurrenceConfig: new FormGroup({
      recurrenceInterval: new FormControl(1),
      recurrenceTimeUnit: new FormControl(RecurrenceTimeUnit.YEAR)
    }),
    userCategory: new FormControl<UserCategory|undefined>(undefined)
  });

  timeunits = TIMEUNITS;
  categories = CATEGORIES;
  matcher = new ErrorStateMatcher();

  constructor() {
    effect(() => {
      const entry = this.store.selectedEntry();
      if (!entry) return;
      const category = this.categories.find(c => c.categoryName === entry.userCategory.categoryName);

      this.retrackerForm.patchValue({
        name: entry.name,
        userCategory: category,
        recurrenceConfig: entry.recurrenceConfig,
        configureRecurrance: entry.recurrenceConfig != null
      });
    });
  }

  formToRetrackerDataChangeRequest(): RetrackerDataChangeRequest {
    const { name, configureRecurrance, recurrenceConfig, userCategory } = this.retrackerForm.value;
    return {
      id: this.store.selectedEntry()!.id,
      name: name!,
      userCategory: userCategory!,
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
