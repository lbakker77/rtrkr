package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.core.BaseResponse;
import de.lbakker77.retracker.core.CommandContext;
import de.lbakker77.retracker.main.TaskDeletedEvent;
import de.lbakker77.retracker.main.domain.RetrackerList;
import de.lbakker77.retracker.main.domain.RetrackerService;
import de.lbakker77.retracker.main.domain.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.TimeZone;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteTaskUseCaseTest {

    @Mock
    private RetrackerService retrackerService;

    @Mock
    private ApplicationEventPublisher events;

    @InjectMocks
    private DeleteTaskUseCase deleteRetrackerTaskUseCase;


    @Test
    void shouldDeleteTaskAndPublishEvent() {
        // Arrange
        var taskId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        DeleteTaskRequest command = new DeleteTaskRequest();
        command.setId(taskId);
        CommandContext commandContext = new CommandContext(userId, TimeZone.getDefault());
        
        Task mockTask = mock(Task.class);
        RetrackerList mockRetrackerList = mock(RetrackerList.class);
        LocalDate mockDueDate = LocalDate.now();
        
        when(retrackerService.loadTaskAndEnsureAccess(taskId, userId)).thenReturn(mockTask);
        when(mockTask.getRetrackerList()).thenReturn(mockRetrackerList);
        when(mockTask.getDueDate()).thenReturn(mockDueDate);
    
        // Act
        BaseResponse response = deleteRetrackerTaskUseCase.handle(command, commandContext);
    
        // Assert
        verify(retrackerService).loadTaskAndEnsureAccess(taskId, userId);
        verify(retrackerService).delete(mockTask);

        var argument = ArgumentCaptor.forClass(TaskDeletedEvent.class);

        verify(events).publishEvent( argument.capture());

        assertNotNull(argument.getValue());
        assertThat(argument.getValue().formerDueDate()).isSameAs(mockDueDate);
        assertThat(argument.getValue().deletedTask()).isSameAs(mockTask);
        assertThat(argument.getValue().userId()).isEqualByComparingTo(userId);

        assertThat(response.isSuccess()).isTrue();
    }
}
