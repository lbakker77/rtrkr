package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.core.CommandContext;
import de.lbakker77.retracker.core.CreatedResponse;
import de.lbakker77.retracker.main.TaskCreatedEvent;
import de.lbakker77.retracker.main.domain.*;
import de.lbakker77.retracker.main.usecase.dtos.RecurrenceConfigDto;
import de.lbakker77.retracker.main.usecase.mapper.RetrackerMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.TimeZone;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTaskUseCaseTest {

    @Mock
    private ApplicationEventPublisher events;

    @Mock
    private RetrackerService retrackerService;

    @Mock
    private RetrackerMapper retrackerMapper;

    @Mock
    private TaskCreator taskCreator;

    @Mock
    private Task mockTask;

    @Mock
    private RetrackerList mockList;

    @InjectMocks
    private CreateTaskUseCase createTaskUseCase;
    

@Test
void shouldCreateTaskWithAllFieldsAndReturnValidResponse() {
    // Arrange
    var userId = UUID.randomUUID();
    var listId = UUID.randomUUID();
    String taskName = "New Task";
    ZonedDateTime dueDate = ZonedDateTime.now().plusDays(7);
    ZonedDateTime lastEntryDate = ZonedDateTime.now().minusDays(1);
    TaskCategory category = TaskCategory.CREATIVE;
    RecurrenceConfigDto recurrenceConfigDto = new RecurrenceConfigDto(1, RecurrenceTimeUnit.WEEK);

    CreateTaskRequest request = new CreateTaskRequest(listId, taskName, dueDate, lastEntryDate, category, recurrenceConfigDto);
    CommandContext commandContext = new CommandContext(userId, TimeZone.getDefault());
    RecurrenceConfig mockRecurrenceConfig = mock(RecurrenceConfig.class);
    when(retrackerService.loadRetrackerListAndEnsureAccess(listId, userId)).thenReturn(mockList);
    when(retrackerMapper.toRecurrenceConfig(recurrenceConfigDto)).thenReturn(mockRecurrenceConfig);
    when(taskCreator.createTask(
            any(RetrackerList.class),
            anyString(),
            any(LocalDate.class),
            any(LocalDate.class),
            any(TaskCategory.class),
            any(RecurrenceConfig.class))
    ).thenReturn(mockTask);

    // Act
    CreatedResponse response = createTaskUseCase.handle(request, commandContext);

    // Assert
    assertThat(response).isNotNull();
    assertTrue(response.isSuccess());
    verify(retrackerService).loadRetrackerListAndEnsureAccess(listId, userId);
    verify(retrackerMapper).toRecurrenceConfig(recurrenceConfigDto);
    verify(retrackerService).save(any(Task.class));
    verify(events).publishEvent(any(TaskCreatedEvent.class));
    verify(taskCreator).createTask(
            mockList,
            taskName,
            dueDate.toLocalDate(),
            lastEntryDate.toLocalDate(),
            category,
            mockRecurrenceConfig
        );
    }
}
