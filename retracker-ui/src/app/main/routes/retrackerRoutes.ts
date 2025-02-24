import { Routes } from "@angular/router";
import { RetrackerListViewComponent } from "../ui/retracker-list-view/retracker-list-view.component";
import { RetrackerInvitationComponent } from "../ui/retracker-invitation/retracker-invitation.component";
import { ShareListComponent } from "../ui/share-list/share-list.component";
import { authGuard } from "../../core/guards/auth.guard";

export const retrackerRoutes: Routes = [
    { path: ':listid', component: RetrackerListViewComponent, canActivate:[authGuard] },
    { path: 'invitation/:listId', component: RetrackerInvitationComponent, canActivate:[authGuard]  },
    { path: 'share/:listId', component: ShareListComponent, canActivate:[authGuard]  },
];
