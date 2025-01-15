import { Routes } from '@angular/router';
import { RetrackerListsNavComponent } from './retracker/ui/retracker-lists-nav/retracker-lists-nav.component';
import { retrackerRoutes } from './retracker/routes/retrackerRoutes';
import { WelcomeComponent } from './public/ui/welcome/welcome.component';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
    { path: 'retracker', component: RetrackerListsNavComponent, children: retrackerRoutes, canActivate:[authGuard], data: { title: 'Retracker', enableSearch: true } },
    { path: 'welcome', component: WelcomeComponent },
    { path: '',   redirectTo: '/welcome', pathMatch: 'full' }
];