import { Routes } from "@angular/router";
import { RetrackerListViewComponent } from "../ui/retracker-list-view/retracker-list-view.component";

export const retrackerRoutes: Routes = [
    { path: 'personal/:listid', component: RetrackerListViewComponent,  },
];