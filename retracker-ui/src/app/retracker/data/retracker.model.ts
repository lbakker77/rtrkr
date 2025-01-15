
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


export interface RetrackerOverviewEntry { 
    id: string;
    name: string;
    dueDate?: Date;
    lastEntryDate?: Date;
    recurrenceConfig?: RecurrenceConfig;
    userCategory: UserCategory
}

export interface RetrackerEntry extends RetrackerOverviewEntry {
    history: RetrackerHistory[];
}

export interface RetrackerHistory {
    lastDueDate?: Date;
    completionDate: Date;
    postponedDays?: number;
}

export interface UserCategory {
    categoryName: string;
    categoryColor: CategoryColor
}


export enum CategoryColor {
    RED = "RED",
    BLUE = "BLUE",
    GREEN = "GREEN",
    ORANGE = "ORANGE"
}

export interface RetrackerList {
    id: string;
    name: string
    shared: boolean;
    defaultList: boolean;
    dueCount: number;
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
export const CATEGORIES: UserCategory[] = [//+
  { categoryName: 'Private', categoryColor: CategoryColor.BLUE },
  { categoryName: 'Work', categoryColor: CategoryColor.RED },
  { categoryName: 'Family', categoryColor: CategoryColor.GREEN },
  { categoryName: 'Friends', categoryColor: CategoryColor.ORANGE }
];


export interface CreatedResponse {
    id: string;
}
