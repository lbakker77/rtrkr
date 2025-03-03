package de.lbakker77.retracker.main.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TasktSimpleTest {
    @Mock
    private RetrackerList mockRetrackerList;
    private final TaskCreator taskCreator = new TaskCreator();

    Task createTestTask() {
        return taskCreator.createTask(mockRetrackerList, "Test Task", null, null,TaskCategory.CREATIVE, null);
    }

    @Test
    void shouldChangeNameIfNewNameIsNotEmpty(){
        // Arrange
        Task task = createTestTask();
        String newName = "New Name";

        // Act
        task.setName(newName);

        // Assert
        assertThat(task.getName()).isEqualTo(newName);
    }

    @Test
    void shouldNotChangeNameIfNewNameIsEmptyString(){
        // Arrange
        Task task = createTestTask();
        String newName = "";

        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> task.changeName(newName));

        // Assert
        assertThat(exception.getMessage()).contains("Name must not be empty");
    }

    @Test
    void shouldChangeTaskCategoryIfNewCategoryIsNotNull(){
        // Arrange
        Task task = createTestTask();
        TaskCategory newCategory = TaskCategory.FINANCIAL;

        // Act
        task.setCategory(newCategory);

        // Assert
        assertThat(task.getCategory()).isEqualTo(newCategory);
    }

    @Test
    void shouldNotChangeTaskCategoryIfNewCategoryIsNull(){
        // Arrange
        Task task = createTestTask();
        TaskCategory newCategory = null;

        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> task.changeCategory(newCategory));

        // Assert
        assertThat(exception.getMessage()).contains("Category must not be null");
    }

    @Test
    void shouldAllowSettingManualDueDateIfCurrentDueDateIsNull(){
        // Arrange
        Task task = createTestTask();
        LocalDate newDueDate = LocalDate.of(2025, 1, 1);
        assertThat(task.getDueDate()).isNull();

        // Act
        task.setManualDueDate(newDueDate);

        // Assert
        assertThat(task.getDueDate()).isEqualTo(newDueDate);
    }

    @Test
    void shouldNotAllowSettingManualDueDateIfCurrentDueDateIsNotNull(){
        // Arrange
        Task task = createTestTask();
        task.setDueDate(LocalDate.of(2025, 1, 1));
        LocalDate newDueDate = LocalDate.of(2025, 1, 2);

        // Act
        var exception = assertThrows(IllegalStateException.class, () -> task.setManualDueDate(newDueDate));

        // Assert
        assertThat(exception.getMessage()).contains("Due date is already set for this task");
    }

    @Test
    void shouldDeleteRecurrenceConfigIfItIsSet(){
        // Arrange
        Task task = createTestTask();
        task.setRecurrenceConfig(new RecurrenceConfig(1, RecurrenceTimeUnit.WEEK));
        task.setPostponedDays(1);

        // Act
        task.deleteRecurrenceConfig();

        // Assert
        assertThat(task.getRecurrenceConfig()).isNull();
        assertThat(task.getPostponedDays()).isZero();
    }
}
