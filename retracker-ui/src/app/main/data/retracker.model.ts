
export enum RecurrenceTimeUnit {
    DAY = "DAY",
    WEEK = "WEEK",
    MONTH = "MONTH",
    YEAR = "YEAR"
}

export interface RecurrenceConfig {
    recurrenceInterval: number;
    recurrenceTimeUnit: RecurrenceTimeUnit;
}


export interface RetrackerOverviewTask { 
    id: string;
    listId: string;
    name: string;
    dueDate?: Date;
    lastEntryDate?: Date;
    recurrenceConfig?: RecurrenceConfig;
    userCategory: UserCategory
}

export interface RetrackerTask extends RetrackerOverviewTask {
    history: RetrackerHistory[];
}

export interface RetrackerHistory {
    lastDueDate?: Date;
    completionDate: Date;
    postponedDays: number;
    plannedDays: number;
    overdueDays: number;
}

export interface UserCategory {
    categoryName: string;
    categoryColor: CategoryColor
}


export enum CategoryColor {
    SILVER = "silver",
    BLUE = "blue",
    RED = "red",
    GREEN = "green",
    ORANGE = "orange",
    PURPLE = "purple",
    TEAL = "teal",
    PINK = "pink",
    YELLOW = "yellow",
    BROWN = "brown",
    GRAY = "gray"
}

export interface RetrackerList {
    id: string;
    name: string
    shared: boolean;
    defaultList: boolean;
    dueCount: number;
    isInvitation: boolean;
    icon: string;
    isOwner: boolean
}

export interface RetrackerDataChangeRequest {
    id: string;
    name: string;
    userCategory: UserCategory;
    recurrenceConfig?: RecurrenceConfig;
}

export interface MarkRetrackerEntryDoneRequest {
    id: string;
    doneAt: Date;
}


export interface PostponeRetrackerRequest {
    id: string;
    postponedDate: Date;
}

export interface CreateRetrackerEntryRequest {
    listId: string;
    name: string;
    dueDate?: Date;
    lastEntryDate?: Date;
    recurrenceConfig?: RecurrenceConfig;
    userCategory: UserCategory
}

export const TIMEUNITS: string[] = Object.keys(RecurrenceTimeUnit);
export const CATEGORIES: UserCategory[] = [
    { categoryName: 'General', categoryColor: CategoryColor.SILVER },
    { categoryName: 'Administrative', categoryColor: CategoryColor.BLUE },
    { categoryName: 'Personal', categoryColor: CategoryColor.GREEN },
    { categoryName: 'Work/Professional', categoryColor: CategoryColor.RED },
    { categoryName: 'Financial', categoryColor: CategoryColor.ORANGE },
    { categoryName: 'Health & Wellness', categoryColor: CategoryColor.PURPLE },
    { categoryName: 'Home & Maintenance', categoryColor: CategoryColor.TEAL },
    { categoryName: 'Social & Relationships', categoryColor: CategoryColor.PINK },
    { categoryName: 'Education & Learning', categoryColor: CategoryColor.YELLOW },
    { categoryName: 'Creative', categoryColor: CategoryColor.BROWN },
    { categoryName: 'Travel & Logistics', categoryColor: CategoryColor.GRAY }
  ];

export interface CreatedResponse {
    id: string;
}


export enum ShareStatus {
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

export interface User {
    id: string;
    firstName: string;
    lastName: string;
    email: string;
}

export interface ShareListRequest {
    listId: string;
    email: string;
}

export enum ChangeType {
    CHANGED = "CHANGED",
    CREATED = "CREATED",
    DELETED = "DELETED",
}

export interface TaskChangeEvent {
    taskId: string;
    changeType: ChangeType;
    dueCountChanged: boolean;
    userId: string;
}