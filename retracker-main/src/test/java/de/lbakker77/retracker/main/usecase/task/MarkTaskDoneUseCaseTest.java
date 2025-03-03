package de.lbakker77.retracker.main.usecase.task;

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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MarkTaskDoneUseCaseTest {

    @Mock
    private RetrackerService retrackerService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private MarkTaskDoneUseCase markTaskDoneUseCase;


    @Test
    void shouldHandleTimezoneDifferencesCorrectlyWhenConvertingDoneAtToLocalDate() {
        // Arrange
        ZonedDateTime doneAt = ZonedDateTime.of(2025, 1, 1, 23, 0, 0, 0, ZoneId.of("UTC"));
        MarkTaskDoneRequest request = new MarkTaskDoneRequest();
        request.setDoneAt(doneAt);
        CommandContext context = new CommandContext(UUID.randomUUID(), TimeZone.getTimeZone("Europe/Berlin"));
        Task task = mock(Task.class);

        // Act
        markTaskDoneUseCase.handleTaskChange(task, request, context);

        // Assert
        verify(task).registerCompletion(LocalDate.of(2025, 1, 2));
        verify(retrackerService).save(task);
    }

    @Test
    void shouldHandleTimezoneDifferencesCorrectlyWhenConvertingDoneAtToLocalDate2() {
        // Arrange
        ZonedDateTime doneAt = ZonedDateTime.of(2025, 1, 1, 22, 59, 59, 0, ZoneId.of("UTC"));
        MarkTaskDoneRequest request = new MarkTaskDoneRequest();
        request.setDoneAt(doneAt);
        CommandContext context = new CommandContext(UUID.randomUUID(), TimeZone.getTimeZone("Europe/Berlin"));
        Task task = mock(Task.class);

        // Act
        markTaskDoneUseCase.handleTaskChange(task, request, context);

        // Assert
        verify(task).registerCompletion(LocalDate.of(2025, 1, 1));
        verify(retrackerService).save(task);
    }
}
