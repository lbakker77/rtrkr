package de.lbakker77.retracker.main.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskPostponeUntilTest {

    @Mock
    private RetrackerList mockRetrackerList;

    @Test
    void postPoneUntilWorksForDueDateAfterCurrentDueDate() {
        // Arrange
        var taskCreator = new TaskCreator();
        var task = taskCreator.createTask(mockRetrackerList, "Test Task",  LocalDate.of(2025, 1, 1), null, TaskCategory.CREATIVE, null);
        var currentDueDate = LocalDate.of(2025, 1, 2);

        // Act
        task.postponeUntil(currentDueDate);

        // Assert
        assertEquals(currentDueDate, task.getDueDate());
        assertEquals(1, task.getPostponedDays());
    }

    @Test
    void postPoneUntilFailsForDueDateEqualsCurrentDueDate() {
        // Arrange
        var taskCreator = new TaskCreator();
        var task = taskCreator.createTask(mockRetrackerList, "Test Task",  LocalDate.of(2025, 1, 1), null, TaskCategory.CREATIVE, null);
        var newDueDate = LocalDate.of(2025, 1, 1);

        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> task.postponeUntil(newDueDate));

        // Assert
        assertTrue(exception.getMessage().contains("New due date must be after current due date"));
    }

    @Test
    void postPoneUntilIncreasesPostponedDaysCorrectly() {
        // Arrange
        var taskCreator = new TaskCreator();
        var task = taskCreator.createTask(mockRetrackerList, "Test Task",  LocalDate.of(2025, 1, 1), null, TaskCategory.CREATIVE, null);
        var currentDueDate = LocalDate.of(2025, 1, 5);
        // manually set postponsed days to 2 (package private)
        task.setPostponedDays(2);

        // Act
        task.postponeUntil(currentDueDate);

        // Assert
        assertEquals(currentDueDate, task.getDueDate());
        assertEquals(6, task.getPostponedDays());
    }

    @Test
    void postponeUntiWithTaskNotPlannedFailes() {
        // Arrange
        var taskCreator = new TaskCreator();
        var task = taskCreator.createTask(mockRetrackerList, "Task", null, null, TaskCategory.CREATIVE, null);
        final var estDateToPostpone = LocalDate.of(2025, 1, 2);
        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> task.postponeUntil(estDateToPostpone));

        // Assert
        assertTrue(exception.getMessage().contains("Task must be planned"));
    }

    @Test
    void postPoneUntilFailsOfNewDateIsNull() {
        // Arrange
        var taskCreator = new TaskCreator();
        var task = taskCreator.createTask(mockRetrackerList, "Test Task",  LocalDate.of(2025, 1, 1), null, TaskCategory.CREATIVE, null);
        // manually set postponsed days to 2 (package private)
        task.setPostponedDays(2);

        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> task.postponeUntil(null));

        // Assert
        assertTrue(exception.getMessage().contains("New due date cannot be null"));

    }
}
