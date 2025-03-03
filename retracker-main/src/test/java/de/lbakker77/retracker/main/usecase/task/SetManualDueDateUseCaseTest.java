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

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SetManualDueDateUseCaseTest {

    @Mock
    private RetrackerService retrackerService;

    @Mock
    private ApplicationEventPublisher eventPublisher;


    @InjectMocks
    private SetManualDueDateUseCase setManualDueDateUseCase;



    @Test
    void shouldConvertManualDueDateToCorrectTimeZone() {
        // Arrange
        ZonedDateTime manualDueDate = ZonedDateTime.of(2025, 1, 1, 23, 0, 0, 0, ZoneId.of("UTC"));
        Task mockTask = mock(Task.class);
        SetManualDueDateRequest request = new SetManualDueDateRequest(UUID.randomUUID(), manualDueDate);
        CommandContext context = new CommandContext(UUID.randomUUID(), TimeZone.getTimeZone("Europe/Berlin"));

        // Act
        BaseResponse response = setManualDueDateUseCase.handleTaskChange(mockTask, request, context);

        // Assert
        verify(mockTask).setManualDueDate(LocalDate.of(2025, 1, 2));
        verify(retrackerService).save(mockTask);
        assertThat(response.isSuccess()).isTrue();
    }
}
