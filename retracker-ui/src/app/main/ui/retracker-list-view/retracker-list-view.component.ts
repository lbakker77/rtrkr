import { ChangeDetectionStrategy, Component, effect, inject, output, signal, viewChild, viewChildren } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TaskStore } from '../../data/task.store';
import { RetrackerOverviewSelectEntryComponent } from './retracker-overview-select-entry/retracker-overview-select-entry.component';
import { RetrackerEditorComponent } from "../retracker-editor/retracker-editor.component";
import { ResponsiveMasterDetailComponent } from "../../../shared/component/responsive-master-detail/responsive-master-detail.component";
import { MasterPartDirective } from '../../../shared/component/responsive-master-detail/master-part.directive';
import { DetailPartDirective } from '../../../shared/component/responsive-master-detail/detail-part.directive';
import { MatFabButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { MasterAbsolutePositionedDirective } from "../../../shared/component/responsive-master-detail/master-absolute-positioned.directive";
import { OverviewTask } from '../../data/task.model';
import { RetrackerCreateComponent } from "../retracker-create/retracker-create.component";
import { ViewportScroller } from '@angular/common';
import { ListStore } from '../../data/list.store';
import { MatMenuModule } from '@angular/material/menu';
import { RetrackerListEditMenuComponent } from "../retracker-list-edit-menu/retracker-list-edit-menu.component";

@Component({
  selector: 'app-retracker-list-view',
  imports: [RetrackerListEditMenuComponent, MatMenuModule, RetrackerOverviewSelectEntryComponent, RetrackerEditorComponent, ResponsiveMasterDetailComponent, MasterPartDirective, DetailPartDirective, MatIcon, MatFabButton, MasterAbsolutePositionedDirective, MasterAbsolutePositionedDirective, RetrackerCreateComponent, RetrackerListEditMenuComponent],
  templateUrl: './retracker-list-view.component.html',
  styleUrl: './retracker-list-view.component.scss',
  providers: [TaskStore],
  changeDetection: ChangeDetectionStrategy.OnPush,
}) 
export class RetrackerListViewComponent  {

  private responiveMasterDetailView = viewChild(ResponsiveMasterDetailComponent);
  private selectComponents = viewChildren(RetrackerOverviewSelectEntryComponent);

  private route = inject(ActivatedRoute);
  title = signal("");
  store = inject(TaskStore);
  listsStore = inject(ListStore);
  scroller = inject(ViewportScroller);
  dueCount = output<number>();
  listId = this.route.snapshot.paramMap.get('listId');


  constructor () {
    this.route.paramMap.subscribe((paramMap) => {
      const listId = paramMap.get("listid");
      if (listId === "all") {
        this.title.set("All Lists");
        this.store.loadAll();
        this.store.unselectEntry();
      }
      else if (listId) {
        this.title.set(listId);
        this.store.loadByListname(listId);
        this.store.unselectEntry();
      }
    });

    effect(() => {
      if(this.selectComponents()){
        const newEntryToScrollTo = this.selectComponents().find(c => c.imSelected());
        if(newEntryToScrollTo) {
          const nativeElement = newEntryToScrollTo?.element.nativeElement;
          if (nativeElement.getBoundingClientRect().top > window.innerHeight) {
            nativeElement.scrollIntoView({ behavior:'smooth' });
          }
          if (nativeElement.getBoundingClientRect().top < 0) {
            nativeElement.scrollIntoView({ behavior:'smooth' });
          }
        }
      }
    });

    effect(() => {
      if (this.store.id()){
        const currentList = this.listsStore.lists().filter(l => l.id === this.store.id());
        if (!this.store.isLoading() && currentList.length && this.listsStore.lists().filter(l => l.id === this.store.id())[0].dueCount !== this.store.dueEntriesCount()) {
          this.listsStore.updateDueCount(this.store.id(), this.store.dueEntriesCount());
        }  
      }else {
        if (!this.store.isLoading() && this.listsStore.overallDueCount()!== this.store.dueEntriesCount()) {
            for (const list of this.listsStore.lists()) {
              const dueCountOfEntries = this.store.getDueCountByListId(list.id);
              if (dueCountOfEntries !== list.dueCount) {
                this.listsStore.updateDueCount(list.id, dueCountOfEntries);
              }
            }
        }
      }
    });
  }

  selected(taskId: string) {
    this.store.selectEntry(taskId);
    this.responiveMasterDetailView()?.openDetail();
  }

  entryChanged($event: OverviewTask|undefined) {
    if ($event && typeof $event.id ==='string') {
      this.store.updateEntry($event);
    }
  }

  entryDeleted(id: string){
    this.store.removeSelected(id);
    this.store.unselectEntry();
    this.responiveMasterDetailView()?.closeDetail(); 
  }

  newEntry() {
    this.store.openNewEntryDialog();
  }

  closeCreate() {
    this.store.closeNewEntryDialog();
  }

  registerNewEntry($event: OverviewTask) {
    this.store.addEntryAndSelect($event);
    this.store.closeNewEntryDialog();
  }


}
