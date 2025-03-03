import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormField, MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ShareStore } from '../../data/share.store';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { AuthStore } from '../../../core/service/auth.store';
import { ShareConfig } from "../../data/list.model";
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ListStore } from '../../data/list.store';
import { SimplePageViewComponent } from '../../../shared/component/simple-page-view/simple-page-view.component';

export interface ShareListDialogData {
  listName: string;
  listId: string;
}

@Component({
  selector: 'app-share-list',
  imports: [MatDialogModule, MatFormField, MatInputModule, ReactiveFormsModule, MatButtonModule, MatAutocompleteModule, MatIconModule, MatTableModule, SimplePageViewComponent, RouterLink],
  templateUrl: './share-list.component.html',
  styleUrl: './share-list.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [ShareStore]
})
export class ShareListComponent {
  readonly listsStore = inject(ListStore);
  store = inject(ShareStore);
  readonly listId = inject(ActivatedRoute).snapshot.paramMap.get('listId');
  displayedColumns: string[] = ['email', 'name', 'status', 'actions'];
  list = computed(() => this.listsStore.lists().find(list => list.id === this.listId)!);

  constructor() {
    this.store.loadShareConfig(this.listId!);
    this.store.loadKnowUsers(this.listId!);
  }

  shareForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email])
  });

  shareWithUser() {
    if (this.shareForm.valid) {
      this.store.shareWithUser(this.shareForm.value.email!);
      this.shareForm.reset();
    }
  } 

  delete(element: ShareConfig) {
    this.store.deleteAccess(element.userId);
  }

}
