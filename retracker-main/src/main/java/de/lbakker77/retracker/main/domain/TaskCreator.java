package de.lbakker77.retracker.main.domain;

import org.springframework.util.Assert;

import java.time.LocalDate;

public class TaskCreator {

    public static Task createTask(RetrackerList list, String name, LocalDate dueDate, LocalDate completionDate, UserCategory userCategory, RecurrenceConfig recurrenceConfig) {
        Assert.notNull(userCategory, "User category must not be null");
        var task = new Task(list, name, dueDate, recurrenceConfig, userCategory);
        if (completionDate != null) {
            task.registerCompletion(completionDate);
        }
        return task;
    }
}
