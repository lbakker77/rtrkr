import { Routes } from '@angular/router';
import { RetrackerListsNavComponent } from './retracker/ui/retracker-lists-nav/retracker-lists-nav.component';
import { retrackerRoutes } from './retracker/routes/retrackerRoutes';

export const routes: Routes = [
    { path: 'retracker', component: RetrackerListsNavComponent, children: retrackerRoutes },
    { path: '',   redirectTo: '/retracker', pathMatch: 'full' }
];