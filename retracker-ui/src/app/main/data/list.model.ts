

export interface RetrackerList {
    id: string;
    name: string;
    shared: boolean;
    defaultList: boolean;
    dueCount: number;
    isInvitation: boolean;
    icon: string;
    isOwner: boolean;
}export enum ShareStatus {
    PENDING = "PENDING",
    ACCEPTED = "ACCEPTED",
    REJECTED = "REJECTED"
}


export interface ShareConfig {
    userId: string;
    email: string;
    firstName: string;
    lastName: string;
    status: ShareStatus;
    sharedAt: Date;
    isOwner: boolean;
}
export interface ShareListRequest {
    listId: string;
    email: string;
}

