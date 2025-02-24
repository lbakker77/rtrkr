import { Routes } from '@angular/router';
import { RetrackerListsNavComponent } from './main/ui/retracker-lists-nav/retracker-lists-nav.component';
import { retrackerRoutes } from './main/routes/retrackerRoutes';
import { WelcomeComponent } from './public/ui/welcome/welcome.component';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
    { path: 'retracker', component: RetrackerListsNavComponent, children: retrackerRoutes, canActivate:[authGuard], data: { title: 'Retracker', enableSearch: true } },
    { path: '', component: WelcomeComponent },
];