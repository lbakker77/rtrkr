package de.lbakker77.retracker.main.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskUndoLastCompletionTest {

    @Mock
    private RetrackerList mockRetrackerList;

    @Test
    void undoLastCompletionResetsToFormerStateIfNoRecurranceConfigProvided() {
        LocalDate lastEntryDate = LocalDate.now().minusDays(1);
        var taskCreator = new TaskCreator();
        var task = taskCreator.createTask(mockRetrackerList, "Task", null, lastEntryDate ,TaskCategory.ADMINISTRATIVE, null);

        task.undoLastCompletion();

        // Assert
        assertNull(task.getDueDate());
        assertTrue(task.getHistory().isEmpty());
        assertNull(task.getLastEntryDate());
    }

    @Test
    void undoLastCompletionResetsToFormerStateIfRecurranceConfigProvided() {
        LocalDate lastEntryDate = LocalDate.now().minusDays(1);
        var taskCreator = new TaskCreator();
        var task = taskCreator.createTask(mockRetrackerList, "Task", null, lastEntryDate ,TaskCategory.ADMINISTRATIVE, new RecurrenceConfig(1, RecurrenceTimeUnit.WEEK));

        task.undoLastCompletion();

        // Assert
        assertNull(task.getDueDate());
        assertTrue(task.getHistory().isEmpty());
        assertNull(task.getLastEntryDate());
    }

    @Test
    void undoLastCompletioFailsIfNotCompletedYet() {
        var taskCreator = new TaskCreator();
        var task = taskCreator.createTask(mockRetrackerList, "Task", null, null ,TaskCategory.ADMINISTRATIVE, new RecurrenceConfig(1, RecurrenceTimeUnit.WEEK));

        var exception = assertThrows(IllegalStateException.class, task::undoLastCompletion);

        // Assert
        assertTrue(exception.getMessage().contains("not been completed"));
    }

    @Test
    void undoLastCompletionCorrectlyRestoresLastState() {
        var taskCreator = new TaskCreator();
        var task = taskCreator.createTask(mockRetrackerList, "Task", null , LocalDate.of(2025,12,2) ,TaskCategory.ADMINISTRATIVE, new RecurrenceConfig(1, RecurrenceTimeUnit.WEEK));

        var manualHistorySet = new TreeSet<EntryHistory>();
        manualHistorySet.addAll(task.getHistory());
        manualHistorySet.add(new EntryHistory(LocalDate.of(2025,12,11), LocalDate.of(2025,12,8), 2, LocalDate.of(2025,12,2)));
        task.setHistory(manualHistorySet);
        assertEquals(0, task.getPostponedDays());

        task.undoLastCompletion();

        // Assert
        assertEquals(LocalDate.of(2025,12,8), task.getDueDate());
        assertEquals(1, task.getHistory().size());
        assertEquals(LocalDate.of(2025,12,2), task.getLastEntryDate());
        assertEquals(2, task.getPostponedDays());
    }
}
