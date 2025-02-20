import { inject, Injectable, signal } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { filter, Subject, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GlobalSearchService {
  router = inject(Router);
  searchEnabled = signal(false);
  search = new Subject<string>();
  
  constructor() {    
    this.router.events.pipe(filter(e => e instanceof NavigationEnd)).subscribe(e => {
      const searchEnabled =  this.router.routerState.root.firstChild?.snapshot.data['enableSearch'] ?? false;
      this.searchEnabled.set(searchEnabled);
    });
  }

  searchChanged(query: string) {
    this.search.next(query);
  }



}
