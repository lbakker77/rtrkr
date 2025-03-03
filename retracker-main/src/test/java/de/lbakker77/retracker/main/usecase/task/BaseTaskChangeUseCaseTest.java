package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.core.usecase.BaseResponse;
import de.lbakker77.retracker.core.usecase.CommandContext;
import de.lbakker77.retracker.main.TaskChangedEvent;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseTaskChangeUseCaseTest {

    @Mock
    private RetrackerService retrackerService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private Task mockTask;

    @InjectMocks
    private TestBaseTaskChangeUseCase useCase;
    

    private static class TestBaseTaskChangeUseCase extends BaseTaskChangeUseCase<BaseTaskChangeRequest, BaseResponse> {

        public TestBaseTaskChangeUseCase(RetrackerService retrackerService, ApplicationEventPublisher events) {
            super(retrackerService, events);
        }

        @Override
        protected BaseResponse handleTaskChange(Task entry, BaseTaskChangeRequest request, CommandContext context) {
            return BaseResponse.ofSuccess();
        }
    }

    @Test
    void shouldLoadTaskAndEnsureAccessWithValidUserIdAndTaskIdAndPublishEvent() {
        // Arrange
        BaseTaskChangeRequest request = new BaseTaskChangeRequest();
        request.setId(UUID.randomUUID());
        var userId = UUID.randomUUID();
        CommandContext context = new CommandContext(userId, TimeZone.getDefault());

        when(retrackerService.loadTaskAndEnsureAccess(any(UUID.class), any(UUID.class))).thenReturn(mockTask);
        var dueDateBeforeChange = LocalDate.of(2025,1,1);
        when(mockTask.getDueDate()).thenReturn(dueDateBeforeChange);

        // Act
        BaseResponse response = useCase.handle(request, context);

        // Assert
        verify(retrackerService).loadTaskAndEnsureAccess(any(UUID.class), any(UUID.class));

        var argument = ArgumentCaptor.forClass(TaskChangedEvent.class);

        verify(eventPublisher).publishEvent( argument.capture());

        assertNotNull(argument.getValue());
        assertThat(argument.getValue().formerDueDate()).isSameAs(dueDateBeforeChange);
        assertThat(argument.getValue().task()).isSameAs(mockTask);
        assertThat(argument.getValue().userId()).isEqualByComparingTo(userId);

        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
    }
}
