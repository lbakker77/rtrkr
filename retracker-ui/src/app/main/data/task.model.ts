
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

export interface OverviewTask { 
    id: string;
    listId: string;
    name: string;
    dueDate?: Date;
    lastEntryDate?: Date;
    recurrenceConfig?: RecurrenceConfig;
    category: UserCategory
}

export interface Task extends OverviewTask {
    history: TaskHistory[];
}

export interface TaskHistory {
    lastDueDate?: Date;
    completionDate: Date;
    postponedDays: number;
    plannedDays: number;
    overdueDays: number;
}

export enum TaskCategory {
    General = "GENERAL",
    Administrative = "ADMINISTRATIVE",
    Personal = "PERSONAL",
    Work_Professional = "WORK_PROFESSIONAL",
    Financial = "FINANCIAL",
    Health_Wellness = "HEALTH_WELLNESS",
    Home_Maintenance = "HOME_MAINTENANCE",
    Social_Relationships = "SOCIAL_RELATIONSHIPS",
    Education_Learning = "EDUCATION_LEARNING",
    Creative = "CREATIVE",
    Travel_Logistics = "TRAVEL_LOGISTICS",
    Errands_Shopping = "ERRANDS_SHOPPING"
}

export interface UserCategory {
    category: TaskCategory|undefined;
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

export interface ChangeTaskDataRequest {
    id: string;
    name: string;
    category: TaskCategory;
    recurrenceConfig?: RecurrenceConfig;
}

export interface MarkTaskDoneRequest {
    id: string;
    doneAt: Date;
}

export interface PostponeTaskRequest {
    id: string;
    postponedDate: Date;
}

export interface CreateTaskRequest {
    listId: string;
    name: string;
    dueDate?: Date;
    lastEntryDate?: Date;
    recurrenceConfig?: RecurrenceConfig;
    category: TaskCategory|undefined;
}

export const TIMEUNITS: string[] = Object.keys(RecurrenceTimeUnit);

export const CATEGORY_TO_COLOR: {category: TaskCategory, categoryColor: CategoryColor}[] = [
    { category: TaskCategory.General, categoryColor: CategoryColor.SILVER },
    { category: TaskCategory.Administrative, categoryColor: CategoryColor.BLUE },
    { category: TaskCategory.Personal, categoryColor: CategoryColor.GREEN },
    { category: TaskCategory.Work_Professional, categoryColor: CategoryColor.RED },
    { category: TaskCategory.Financial, categoryColor: CategoryColor.ORANGE },
    { category: TaskCategory.Health_Wellness, categoryColor: CategoryColor.PURPLE },
    { category: TaskCategory.Home_Maintenance, categoryColor: CategoryColor.TEAL },
    { category: TaskCategory.Social_Relationships, categoryColor: CategoryColor.PINK },
    { category: TaskCategory.Education_Learning, categoryColor: CategoryColor.YELLOW },
    { category: TaskCategory.Creative, categoryColor: CategoryColor.BROWN },
    { category: TaskCategory.Travel_Logistics, categoryColor: CategoryColor.GRAY },
    { category: TaskCategory.Education_Learning, categoryColor: CategoryColor.GRAY }
  ];

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