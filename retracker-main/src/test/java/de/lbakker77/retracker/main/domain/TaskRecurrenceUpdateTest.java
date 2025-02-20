package de.lbakker77.retracker.main.domain;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TaskRecurrenceUpdateTest {

    @Mock
    private RetrackerList mockRetrackerList;

    @Test
    void shouldUpdateRecurrenceConfigWhenNewConfigIsDifferent() {
        // Arrange
        var firstCompletionDate = LocalDate.of(2025, 1, 1);
        Task task = TaskCreator.createTask(mockRetrackerList, "Test Task", null, firstCompletionDate, new UserCategory("Test","red"), new RecurrenceConfig(1, RecurrenceTimeUnit.WEEK));
        var formerDueDate = task.getDueDate();
        task.postponeUntil(formerDueDate.plusDays(1));
        var postponedDueDate = task.getDueDate();

        // Act
        task.updateRecurrenceConfig(new RecurrenceConfig(2, RecurrenceTimeUnit.WEEK) );

        // Assert
        assertEquals(new RecurrenceConfig(2, RecurrenceTimeUnit.WEEK), task.getRecurrenceConfig());
        assertNotEquals(task.getDueDate(), postponedDueDate, "Due date should be updated with new recurrence interval");
        assertThat(task.getDueDate()).isAfter(formerDueDate);
        assertThat(task.getPostponedDays()).isEqualTo(0);
    }

    @Test
    void shouldNotUpdateRecurrenceConfigWhenNewConfigIsSame() {
        // Arrange
        var dueDate = LocalDate.of(2025, 1, 1);
        var recurrenceConfig = new RecurrenceConfig(1, RecurrenceTimeUnit.WEEK);
        Task task = TaskCreator.createTask(mockRetrackerList, "Test Task", dueDate, null, new UserCategory("Test","red"), recurrenceConfig);
        var initialRecurrenceConfig = task.getRecurrenceConfig();
        task.postponeUntil(dueDate.plusDays(1));
        var dueDateBeforeUpdate = task.getDueDate();

        // Act
        task.updateRecurrenceConfig( new RecurrenceConfig(1, RecurrenceTimeUnit.WEEK));

        // Assert
        assertEquals(initialRecurrenceConfig, task.getRecurrenceConfig(), "Recurrence config should not change");
        assertEquals(dueDateBeforeUpdate, task.getDueDate(), "Due date should not change");
        assertEquals(1, task.getPostponedDays(), "Postponed days should remain 0");
    }

    @Test
    void shouldNotModifyDueDateWhenNoLastEntry() {
        // Arrange
        Task task = TaskCreator.createTask(mockRetrackerList, "Test Task", LocalDate.of(2025, 1, 1), null, new UserCategory("Test","red"), new RecurrenceConfig(1, RecurrenceTimeUnit.WEEK));
        task.postponeUntil(LocalDate.of(2025, 1, 2));
        LocalDate initialDueDate = task.getDueDate();

        // Act
        task.updateRecurrenceConfig(new RecurrenceConfig(2, RecurrenceTimeUnit.DAY));

        // Assert
        assertEquals(new RecurrenceConfig(2, RecurrenceTimeUnit.DAY), task.getRecurrenceConfig(), "Recurrence config should be updated");
        assertEquals(initialDueDate, task.getDueDate(), "Due date should not change when there's no last entry");
        assertEquals(1, task.getPostponedDays(), "Postponed days should remain 1");
    }

}