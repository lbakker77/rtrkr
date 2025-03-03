package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.core.BaseResponse;
import de.lbakker77.retracker.core.CommandContext;
import de.lbakker77.retracker.main.domain.*;
import de.lbakker77.retracker.main.usecase.dtos.RecurrenceConfigDto;
import de.lbakker77.retracker.main.usecase.mapper.RetrackerMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.TimeZone;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChangeTaskDataUseCaseTest {

    @Mock
    private RetrackerMapper retrackerMapper;

    @Mock
    private RetrackerService retrackerService;

    @Mock
    private ApplicationEventPublisher events;

    @Mock
    Task mockTask;

    @InjectMocks
    private ChangeTaskDataUseCase changeTaskDataUseCase;



    @Test
    void shouldSuccessfullyChangeTaskNameWhenValidNameIsProvided() {
        // Arrange
        String newName = "Updated Task Name";
        ChangeTaskDataRequest request = new ChangeTaskDataRequest(newName, null, TaskCategory.CREATIVE);
        request.setId(UUID.randomUUID());
        CommandContext context = new CommandContext(UUID.randomUUID(), TimeZone.getDefault());
        
        // Act
        BaseResponse response = changeTaskDataUseCase.handleTaskChange(mockTask, request, context);

        // Assert
        verify(mockTask).changeName(newName);
        assertThat(response.isSuccess()).isTrue();
    }

    @Test
    void shouldUpdateRecurrenceConfigWhenValidConfigIsProvided() {
        // Arrange
        UUID taskId = UUID.randomUUID();
        RecurrenceConfigDto recurrenceConfigDto = new RecurrenceConfigDto(2, RecurrenceTimeUnit.WEEK);
        ChangeTaskDataRequest request = new ChangeTaskDataRequest("DoesNotMatter", recurrenceConfigDto, TaskCategory.CREATIVE);
        request.setId(taskId);
        CommandContext context = new CommandContext(UUID.randomUUID(), TimeZone.getDefault());
        
        RecurrenceConfig recurrenceConfig = new RecurrenceConfig(2, RecurrenceTimeUnit.WEEK);
        when(retrackerMapper.toRecurrenceConfig(recurrenceConfigDto)).thenReturn(recurrenceConfig);
    
        // Act
        BaseResponse response = changeTaskDataUseCase.handleTaskChange(mockTask, request, context);
    
        // Assert
        verify(mockTask).updateRecurrenceConfig(new RecurrenceConfig(2, RecurrenceTimeUnit.WEEK));
        verify(mockTask, never()).deleteRecurrenceConfig();

        assertThat(response.isSuccess()).isTrue();
    }

    @Test
    void shouldDeleteRecurrenceConfigWhenNullConfigIsProvided() {
        // Arrange
        UUID taskId = UUID.randomUUID();
        ChangeTaskDataRequest request = new ChangeTaskDataRequest("DoesNotMatter", null, TaskCategory.CREATIVE);
        request.setId(taskId);
        CommandContext context = new CommandContext(UUID.randomUUID(), TimeZone.getDefault());

        // Act
        BaseResponse response = changeTaskDataUseCase.handleTaskChange(mockTask, request, context);

        // Assert
        verify(mockTask).deleteRecurrenceConfig();
        verify(mockTask, never()).updateRecurrenceConfig(any());
        assertThat(response.isSuccess()).isTrue();
    }
    

@Test
void shouldChangeCategoryWhenValidCategoryIsProvided() {
    // Arrange
    UUID taskId = UUID.randomUUID();
    TaskCategory newCategory = TaskCategory.ADMINISTRATIVE;
    ChangeTaskDataRequest request = new ChangeTaskDataRequest("DoesNotMatter", null, newCategory);
    request.setId(taskId);
    CommandContext context = new CommandContext(UUID.randomUUID(), TimeZone.getDefault());

    // Act
    BaseResponse response = changeTaskDataUseCase.handleTaskChange(mockTask, request, context);

    // Assert
    verify(mockTask).changeCategory(newCategory);
    assertThat(response.isSuccess()).isTrue();
}

    @Test
    void shouldSaveTaskAfterAllChangesAreApplied() {
        // Arrange
        UUID taskId = UUID.randomUUID();
        String newName = "Updated Task Name";
        RecurrenceConfigDto recurrenceConfigDto = new RecurrenceConfigDto(3, RecurrenceTimeUnit.DAY);
        TaskCategory newCategory = TaskCategory.PERSONAL;
        ChangeTaskDataRequest request = new ChangeTaskDataRequest(newName, recurrenceConfigDto, newCategory);
        request.setId(taskId);
        CommandContext context = new CommandContext(UUID.randomUUID(), TimeZone.getDefault());

        RecurrenceConfig recurrenceConfig = new RecurrenceConfig(3, RecurrenceTimeUnit.DAY);
        when(retrackerMapper.toRecurrenceConfig(recurrenceConfigDto)).thenReturn(recurrenceConfig);

        // Act
        BaseResponse response = changeTaskDataUseCase.handleTaskChange(mockTask, request, context);

        // Assert
        verify(retrackerService).save(mockTask);
        assertThat(response.isSuccess()).isTrue();
    }

}