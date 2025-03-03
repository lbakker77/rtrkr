package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.core.usecase.BaseResponse;
import de.lbakker77.retracker.core.usecase.CommandContext;
import de.lbakker77.retracker.main.domain.RetrackerService;
import de.lbakker77.retracker.main.domain.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.TimeZone;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UndoLastCompletionUseCaseTest {

    @Mock
    private RetrackerService retrackerService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private Task mockTask;

    @InjectMocks
    private UndoLastCompletionUseCase undoLastCompletionUseCase;


    @Test
    void shouldSuccessfullyUndoLastCompletionOfTask() {
        // Arrange
        UndoLastCompletionRequest request = new UndoLastCompletionRequest(UUID.randomUUID());
        CommandContext context = new CommandContext(UUID.randomUUID(), TimeZone.getDefault());

        // Act
        BaseResponse response = undoLastCompletionUseCase.handleTaskChange(mockTask, request, context);

        // Assert
        verify(mockTask).undoLastCompletion();
        verify(retrackerService).save(mockTask);
        assertThat(response.isSuccess()).isTrue();
    }
}