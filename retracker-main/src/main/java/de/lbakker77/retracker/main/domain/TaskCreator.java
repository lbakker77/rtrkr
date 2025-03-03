package de.lbakker77.retracker.main.domain;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;

@Service
public class TaskCreator {

    public Task createTask(RetrackerList list, String name, LocalDate dueDate, LocalDate completionDate, TaskCategory category, RecurrenceConfig recurrenceConfig) {
        Assert.notNull(list, "Retracker list must not be null");
        Assert.notNull(category, "Task category must not be null");
        if (dueDate != null && completionDate != null) {
            throw new IllegalArgumentException("Due date and completion date cannot both be set");
        }
        var task = new Task(list, name, dueDate, recurrenceConfig, category);
        if (completionDate != null) {
            task.registerCompletion(completionDate);
        }
        return task;
    }
}
