package de.lbakker77.retracker.main.domain;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class TaskCreatorTest {
    @Mock
    private RetrackerList mockRetrackerList;
    private final TaskCreator taskCreator = new TaskCreator();

    @Test
    void shouldCreateTaskWithoutCompletionDueDateAndRecurrenceConfig() {
        // Arrange & Act
        var task = taskCreator.createTask(mockRetrackerList, "Test Task", null, null, TaskCategory.CREATIVE, null);

        // Assert
        assertThat(task.getName()).isEqualTo("Test Task");
        assertThat(task.getCategory()).isEqualTo(TaskCategory.CREATIVE);
        assertThat(task.getLastEntryDate()).isNull();
        assertThat(task.getDueDate()).isNull();
        assertThat(task.getRecurrenceConfig()).isNull();
    }

    @Test
    void shouldCreateTaskWithoutCompletionDateAndRecurrenceConfigButCompletionDateSet() {
        // Arrange & Act
        var task = taskCreator.createTask(mockRetrackerList, "Test Task", null , LocalDate.of(2025,12,20), TaskCategory.CREATIVE, null);

        // Assert
        assertThat(task.getName()).isEqualTo("Test Task");
        assertThat(task.getCategory()).isEqualTo(TaskCategory.CREATIVE);
        assertThat(task.getLastEntryDate()).isEqualTo(LocalDate.of(2025,12,20));
        assertThat(task.getDueDate()).isNull();
        assertThat(task.getRecurrenceConfig()).isNull();
    }

    @Test
    void shouldCreateTaskWithCompletionDateAndRecurrenceConfig() {
        // Arrange
        var recurrenceConfig = new RecurrenceConfig(1, RecurrenceTimeUnit.WEEK);
        LocalDate completionDate = LocalDate.of(2025, 12, 20);

        // Act
        var task = taskCreator.createTask(mockRetrackerList, "Test Task", null, completionDate, TaskCategory.CREATIVE, recurrenceConfig);

        // Assert
        assertThat(task.getName()).isEqualTo("Test Task");
        assertThat(task.getCategory()).isEqualTo(TaskCategory.CREATIVE);
        assertThat(task.getLastEntryDate()).isEqualTo(completionDate);
        assertThat(task.getDueDate()).isEqualTo(LocalDate.of(2025,12,27));
        assertThat(task.getRecurrenceConfig()).isEqualTo(recurrenceConfig);
        assertThat(task.getHistory().size()).isOne();
    }

    @Test
    void shouldCreateTaskWithDueDatePlannedAndRecurrenceConfig() {
        // Arrange
        var recurrenceConfig = new RecurrenceConfig(1, RecurrenceTimeUnit.MONTH);
        LocalDate dueDate = LocalDate.of(2025, 12, 20);

        // Act
        var task = taskCreator.createTask(mockRetrackerList, "Test Task", dueDate, null, TaskCategory.CREATIVE, recurrenceConfig);

        // Assert
        assertThat(task.getName()).isEqualTo("Test Task");
        assertThat(task.getCategory()).isEqualTo(TaskCategory.CREATIVE);
        assertThat(task.getLastEntryDate()).isNull();
        assertThat(task.getDueDate()).isEqualTo(dueDate);
        assertThat(task.getRecurrenceConfig()).isEqualTo(recurrenceConfig);
    }

}
