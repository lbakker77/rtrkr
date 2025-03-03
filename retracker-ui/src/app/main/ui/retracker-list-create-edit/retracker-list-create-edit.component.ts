import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { CreatedResponse } from "../../../shared/data/response.model";
import { RetrackerList } from "../../data/list.model";
import { CommonModule } from '@angular/common';
import { ListService } from '../../data/list.service';

export interface RetrackerListCreateResult {
    listId: string
    configureShare: boolean
}

@Component({
  selector: 'app-retracker-list-create-edit',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatIconModule
  ],
  templateUrl: './retracker-list-create-edit.component.html',
  styleUrls: ['./retracker-list-create-edit.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class RetrackerListCreateEditComponent {
  private fb = inject(FormBuilder);
  private listService = inject(ListService);
  private dialogRef = inject(MatDialogRef<RetrackerListCreateEditComponent>);
  data: RetrackerList = inject(MAT_DIALOG_DATA);

  form: FormGroup = this.fb.group({
    name: ['', [Validators.required, Validators.maxLength(20)]],
    icon: ['', Validators.required],
    iconSearch: ['']
  });

  icons: string[] = [
    'home', 'work', 'shopping_cart', 'favorite', 'star', 'check_circle',
    'schedule', 'event', 'person', 'group', 'school', 'book', 'local_library',
    'fitness_center', 'restaurant', 'local_cafe', 'local_bar', 'local_mall',
    'local_grocery_store', 'local_hospital', 'local_pharmacy', 'local_laundry_service',
    'local_hotel', 'local_airport', 'directions_car', 'directions_bus', 'directions_bike',
    'directions_walk', 'beach_access', 'ac_unit', 'wb_sunny', 'cloud', 'whatshot'
  ];

  filteredIcons: string[] = this.icons;

  constructor() {
    if (this.data) {
        this.form.patchValue({ name: this.data.name, icon: this.data.icon });
    }
    this.form.get('iconSearch')?.valueChanges.subscribe(value => {
      this.filterIcons(value);
    });
  }

  filterIcons(value: string): void {
    const filterValue = value.toLowerCase();
    this.filteredIcons = this.icons.filter(icon => icon.toLowerCase().includes(filterValue));
  }
  save(andShare: boolean = false) {
    if (this.form.valid) {
      const name = this.form.get('name')!.value;
      const icon = this.form.get('icon')!.value;
      if (this.data) {
        this.listService.updateList(this.data.id, name, icon).subscribe(() => {
          this.dialogRef.close({ configureShare: andShare });
        });
      } else {
       this.listService.createList(name, icon).subscribe((createdResponse: CreatedResponse) => {
          this.dialogRef.close({configureShare: andShare, listId: createdResponse.id } );
        });
      }
    }
  }

  cancel() {
    this.dialogRef.close();
  }
}
