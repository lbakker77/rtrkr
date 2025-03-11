import { Routes } from '@angular/router';
import { RetrackerListsNavComponent } from './main/ui/retracker-lists-nav/retracker-lists-nav.component';
import { retrackerRoutes } from './main/routes/retrackerRoutes';
import { WelcomeComponent } from './public/ui/welcome/welcome.component';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
    { path: 'retracker', 
        canActivate:[authGuard],
        data: { title: 'Retracker', enableSearch: true },
        loadComponent: () => import('./main/ui/retracker-lists-nav/retracker-lists-nav.component').then(m => m.RetrackerListsNavComponent), 
        loadChildren: () => import('./main/routes/retrackerRoutes').then(m => m.retrackerRoutes) ,
    },
    { path: '', component: WelcomeComponent },
];