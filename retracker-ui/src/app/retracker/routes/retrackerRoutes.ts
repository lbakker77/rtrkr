import { Routes } from "@angular/router";
import { RetrackerListViewComponent } from "../ui/retracker-list-view/retracker-list-view.component";
import { RetrackerInvitationComponent } from "../ui/retracker-invitation/retracker-invitation.component";
import { ShareListComponent } from "../ui/share-list/share-list.component";

export const retrackerRoutes: Routes = [
    { path: ':listid', component: RetrackerListViewComponent },
    { path: 'invitation/:listId', component: RetrackerInvitationComponent },
    { path: 'share/:listId', component: ShareListComponent },
];
