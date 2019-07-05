export class Fact {
    taskId?: number;
    subject: string;
    predicate: string;
    object: string;
    algorithm: string;
    averageTruthValue?: number;
    results?: Map<string, number>;
}