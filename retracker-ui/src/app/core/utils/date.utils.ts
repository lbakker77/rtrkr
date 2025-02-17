export function toISOStringDateOnly(date: Date): string {
    return date.toISOString().split('T')[0];
}

export function todayAsDate(): Date {
    const now = new Date();
    return new Date(now.getFullYear(), now.getMonth(), now.getDate());
}

export function addDays(date: Date, days: number): Date {
    return new Date(date.getFullYear(), date.getMonth(), date.getDate() + days);
}