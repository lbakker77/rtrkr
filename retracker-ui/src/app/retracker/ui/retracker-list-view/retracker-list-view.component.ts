import { ChangeDetectionStrategy, Component, effect, inject, output, signal, viewChild, viewChildren } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RetrackerListViewStore } from './retracker-list-view.store';
import { RetrackerOverviewSelectEntryComponent } from './retracker-overview-select-entry/retracker-overview-select-entry.component';
import { RetrackerEditorComponent } from "../retracker-editor/retracker-editor.component";
import { ResponsiveMasterDetailComponent } from "../../../shared/component/responsive-master-detail/responsive-master-detail.component";
import { MasterPartDirective } from '../../../shared/component/responsive-master-detail/master-part.directive';
import { DetailPartDirective } from '../../../shared/component/responsive-master-detail/detail-part.directive';
import { MatFabButton, MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { MasterAbsolutePositionedDirective } from "../../../shared/component/responsive-master-detail/master-absolute-positioned.directive";
import { RetrackerOverviewEntry } from '../../data/retracker.model';
import { RetrackerCreateComponent } from "../retracker-create/retracker-create.component";
import { ViewportScroller } from '@angular/common';
import { RetrackerListsNavStore } from '../retracker-lists-nav/retracker-lists-nav.store';
import { MatMenuModule } from '@angular/material/menu';

@Component({
  selector: 'app-retracker-list-view',
  imports: [MatMenuModule, RetrackerOverviewSelectEntryComponent, RetrackerEditorComponent, ResponsiveMasterDetailComponent, MasterPartDirective, DetailPartDirective, MatIcon, MatFabButton, MasterAbsolutePositionedDirective, MasterAbsolutePositionedDirective, RetrackerCreateComponent, MatIconButton],
  templateUrl: './retracker-list-view.component.html',
  styleUrl: './retracker-list-view.component.scss',
  providers: [RetrackerListViewStore],
  changeDetection: ChangeDetectionStrategy.OnPush,
}) 
export class RetrackerListViewComponent  {
  private responiveMasterDetailView = viewChild(ResponsiveMasterDetailComponent);
  private selectComponents = viewChildren(RetrackerOverviewSelectEntryComponent);

  private route = inject(ActivatedRoute);
  title = signal("");
  store = inject(RetrackerListViewStore);
  listsStore = inject(RetrackerListsNavStore);
  scroller = inject(ViewportScroller);
  dueCount = output<number>();

  constructor () {
    this.route.paramMap.subscribe((paramMap) => {
      const category = paramMap.get("listid");
      if (category) {
        this.title.set(category);
        console.log("listid: " + category);  
        this.store.loadByListname(category);
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

      if (!this.store.isLoading() && this.listsStore.lists().filter(l => l.id === this.store.id())[0].dueCount !== this.store.dueEntriesCount()) {
        this.listsStore.updateDueCount(this.store.id(), this.store.dueEntriesCount());

      }
    });
  }

  selected(retrackerId: string) {
    this.store.selectEntry(retrackerId);
    this.responiveMasterDetailView()?.openDetail();
  }

  entryChanged($event: RetrackerOverviewEntry|undefined) {
    const idToDelete = this.store.selectedId();
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

  registerNewEntry($event: RetrackerOverviewEntry) {
    this.store.addEntry($event);
    this.store.closeNewEntryDialog();
  }
    
    
}
