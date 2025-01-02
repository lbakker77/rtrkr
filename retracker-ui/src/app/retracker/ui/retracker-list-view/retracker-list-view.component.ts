import { ChangeDetectionStrategy, Component, effect, inject, signal, viewChild, viewChildren } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ResponsivnessService } from '../../../shared/service/responsivness.service';
import { RetrackerListViewStore } from './retracker-list-view.store';
import { RetrackerOverviewEntrySelectComponent } from '../retracker-overview-entry-select/retracker-overview-entry-select.component';
import { RetrackerEditorComponent } from "../retracker-editor/retracker-editor.component";
import { ResponsiveMasterDetailComponent } from "../../../shared/component/responsive-master-detail/responsive-master-detail.component";
import { MasterPartDirective } from '../../../shared/component/responsive-master-detail/master-part.directive';
import { DetailPartDirective } from '../../../shared/component/responsive-master-detail/detail-part.directive';
import { MatFabButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { MasterAbsolutePositionedDirective } from "../../../shared/component/responsive-master-detail/master-absolute-positioned.directive";
import { RetrackerOverviewEntry } from '../../data/retracker.model';
import { RetrackerCreateComponent } from "../retracker-create/retracker-create.component";
import { ViewportScroller } from '@angular/common';

@Component({
  selector: 'app-retracker-list-view',
  imports: [RetrackerOverviewEntrySelectComponent, RetrackerEditorComponent, ResponsiveMasterDetailComponent, MasterPartDirective, DetailPartDirective, MatIcon, MatFabButton, MasterAbsolutePositionedDirective, MasterAbsolutePositionedDirective, RetrackerCreateComponent],
  templateUrl: './retracker-list-view.component.html',
  styleUrl: './retracker-list-view.component.scss',
  providers: [RetrackerListViewStore],
  changeDetection: ChangeDetectionStrategy.OnPush,
}) 
export class RetrackerListViewComponent  {
  private responiveMasterDetailView = viewChild(ResponsiveMasterDetailComponent);
  private selectComponents = viewChildren(RetrackerOverviewEntrySelectComponent);

  private route = inject(ActivatedRoute);
  title = signal("");
  store = inject(RetrackerListViewStore);
  scroller = inject(ViewportScroller);
  selectedId = signal<string | undefined>(undefined);

  constructor () {
    this.route.paramMap.subscribe((paramMap) => {
      const category = paramMap.get("category");
      if (category) {
        this.title.set(category);
        console.log("category: " + category);  // Debugging only, remove in production
        this.store.loadByListname(category);
        this.selectedId.set(undefined);
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
  }

  selected(retrackerId: string) {
    this.store.selectEntry(retrackerId);
    this.responiveMasterDetailView()?.openDetail();
  }

  entryChanged($event: RetrackerOverviewEntry|undefined) {
    const idToDelete = this.selectedId();
    if ($event && typeof $event.id ==='string') {
      this.store.updateEntry($event);
    }
  }

  entryDeleted(id: string){
    this.store.removeSelected(id);
    this.selectedId.set(undefined);
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
