package de.lbakker77.retracker.main.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class TaskUpdateRecurrenceConfigTest {
    private final TaskCreator taskCreator = new TaskCreator();

    @Test
    void shouldNotUpdateRecurrenceConfigWhenNewConfigEqualsExistingConfig() {
        // Arrange
        var task = taskCreator.createTask(mock(RetrackerList.class), "Test Task", null, LocalDate.of(2023,1,23), TaskCategory.CREATIVE, new RecurrenceConfig(1, RecurrenceTimeUnit.WEEK));
        var existingConfig = new RecurrenceConfig(1, RecurrenceTimeUnit.WEEK);
        task.updateRecurrenceConfig(existingConfig); // Set initial config
        task.setPostponedDays(1); // Manually set postponed days for testcase only
        var newConfig = new RecurrenceConfig(1, RecurrenceTimeUnit.WEEK);
        var dueDateBeforeUpdate = task.getDueDate();


        // Act
        task.updateRecurrenceConfig(newConfig);

        // Assert
        assertThat(task.getRecurrenceConfig()).isEqualTo(existingConfig);
        assertThat(task.getRecurrenceConfig()).isNotSameAs(newConfig);
        assertEquals(dueDateBeforeUpdate, task.getDueDate());
        assertEquals(1, task.getPostponedDays());
    }

    @Test
    void shouldUpdateRecurrenceConfigSettingNewDueDateIfCompleted() {
        // Arrange
        var task = taskCreator.createTask(mock(RetrackerList.class), "Test Task", null, LocalDate.of(2025,1,2), TaskCategory.CREATIVE, new RecurrenceConfig(1, RecurrenceTimeUnit.WEEK));
        task.setPostponedDays(3);
        var newConfig = new RecurrenceConfig(2, RecurrenceTimeUnit.WEEK);

        // Act
        task.updateRecurrenceConfig(newConfig);

        // Assert
        assertThat(task.getRecurrenceConfig()).isSameAs(newConfig);
        assertThat(task.getDueDate()).isEqualTo(LocalDate.of(2025,1,16)); // two weeks after last completion
        assertThat(task.getPostponedDays()).isZero();
    }

    @Test
    void shouldUpdateRecurrenceConfigNotSettingNewDueDateIfNotYetCompleted() {
        // Arrange
        var task = taskCreator.createTask(mock(RetrackerList.class), "Test Task", null, null, TaskCategory.CREATIVE, new RecurrenceConfig(1, RecurrenceTimeUnit.WEEK));
        var newConfig = new RecurrenceConfig(1, RecurrenceTimeUnit.MONTH);

        // Act
        task.updateRecurrenceConfig(newConfig);

        // Assert
        assertThat(task.getRecurrenceConfig()).isSameAs(newConfig);
        assertThat(task.getDueDate()).isNull();
        assertThat(task.getPostponedDays()).isZero();
    }
}
