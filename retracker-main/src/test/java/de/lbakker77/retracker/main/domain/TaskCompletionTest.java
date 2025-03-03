package de.lbakker77.retracker.main.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TaskCompletionTest {

    @Mock
    private RetrackerList mockRetrackerList;


    @Test
    void shouldAcceptValidCompletionDateAfterLastEntryDate() {
        // Arrange
        LocalDate lastEntryDate = LocalDate.now().minusDays(1);
        var taskCreator = new TaskCreator();
        var task = taskCreator.createTask(mockRetrackerList, "Task", null, lastEntryDate ,TaskCategory.ADMINISTRATIVE, null);
        LocalDate completionDate = LocalDate.now();

        // Act
        task.registerCompletion(completionDate);

        // Assert
        assertThat(task.getHistory()).hasSize(2);
        assertThat(task.getLastEntryDate()).isEqualTo(completionDate);
        assertThat(task.getPostponedDays()).isZero();
        assertThat(task.getDueDate()).isNull();
    }

    @Test
    void shouldDenyCompletionDateEqualToLastEntryDate() {
        // Arrange
        LocalDate lastEntryDate = LocalDate.now();
        var taskCreator = new TaskCreator();
        var task = taskCreator.createTask(mockRetrackerList, "Task", null, lastEntryDate , TaskCategory.CREATIVE, null);
        LocalDate completionDate = LocalDate.now();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> task.registerCompletion(completionDate) );
    }

    @Test
    void shouldCalculateNewDueDateAfterCompletion() {
        // Arrange
        var recurrenceConfig = new RecurrenceConfig(1, RecurrenceTimeUnit.WEEK);
        var taskCreator = new TaskCreator();
        var task = taskCreator.createTask(mockRetrackerList, "Task", null, null, TaskCategory.CREATIVE, recurrenceConfig);
        LocalDate completionDate = LocalDate.of(2025,1,1);

        // Act
        task.registerCompletion(completionDate);

        // Assert
        assertEquals(task.getDueDate(), LocalDate.of(2025,1,8));
    }

    @Test
    void shouldResetPostponedDayAfterCompletion() {
        // Arrange
        var recurrenceConfig = new RecurrenceConfig(1, RecurrenceTimeUnit.WEEK);
        var taskCreator = new TaskCreator();
        var task = taskCreator.createTask(mockRetrackerList, "Task", LocalDate.of(2025,1,1), null, TaskCategory.CREATIVE, recurrenceConfig);
        task.postponeUntil(LocalDate.of(2025,1,2));
        LocalDate completionDate = LocalDate.of(2025,1,2);
        assertEquals(1, task.getPostponedDays());

        // Act
        task.registerCompletion(completionDate);

        // Assert
        assertEquals(0, task.getPostponedDays());
    }

    @Test
    void shouldAddCorrectHistoryEntryAfterCompletion() {
        // Arrange
        var recurrenceConfig = new RecurrenceConfig(1, RecurrenceTimeUnit.WEEK);
        var dueDate = LocalDate.of(2025,1,1);
        var taskCreator = new TaskCreator();
        var task = taskCreator.createTask(mockRetrackerList, "Task", dueDate, null, TaskCategory.CREATIVE, recurrenceConfig);
        task.postponeUntil(LocalDate.of(2025,1,2));
        LocalDate completionDate = LocalDate.of(2025,1,3);

        // Act
        task.registerCompletion(completionDate);

        // Assert
        assertThat(task.getHistory()).hasSize(1);
        var lastEntry = task.getHistory().first();
        assertThat(lastEntry.completionDate()).isEqualTo(completionDate);
        assertThat(lastEntry.postponedDays()).isEqualTo(1);
        assertThat(lastEntry.dueDate()).isEqualTo(LocalDate.of(2025,1,2));
    }

    @Test
    void shouldSetDueDateToNullIfRecurrenceConfigIsNotPresent() {
        // Arrange
        LocalDate completionDate = LocalDate.now();
        var taskCreator = new TaskCreator();
        var task = taskCreator.createTask(mockRetrackerList, "Task", LocalDate.now().plusDays(1), null, TaskCategory.CREATIVE, null);

        // Act
        task.registerCompletion(completionDate);

        // Assert
        assertThat(task.getDueDate()).isNull();
        assertThat(task.getLastEntryDate()).isEqualTo(completionDate);
        assertThat(task.getHistory()).hasSize(1);
    }

    @Test
    void shouldWriteProperHistoryUponSecondCompletion() {
        // Arrange
        var recurrenceConfig = new RecurrenceConfig(1, RecurrenceTimeUnit.WEEK);
        var completionDate = LocalDate.now();
        var formerCompletionDate = LocalDate.now().minusDays(7);
        var taskCreator = new TaskCreator();
        var task = taskCreator.createTask(mockRetrackerList, "Task", null, formerCompletionDate, TaskCategory.CREATIVE, recurrenceConfig);

        // Act
        task.registerCompletion(completionDate);

        // Assert
        assertThat(task.getHistory()).hasSize(2);
        var recentEntry = task.getHistory().first();
        assertThat(recentEntry.completionDate()).isEqualTo(completionDate);
        var formerEntry = task.getHistory().last();
        assertThat(formerEntry.completionDate()).isEqualTo(formerCompletionDate);
    }
}
