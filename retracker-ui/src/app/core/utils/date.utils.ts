export function toISOStringDateOnly(date: Date): string {
    return date.toISOString().split('T')[0];
}