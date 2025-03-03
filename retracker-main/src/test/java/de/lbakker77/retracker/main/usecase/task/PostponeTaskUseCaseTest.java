package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.core.BaseResponse;
import de.lbakker77.retracker.core.CommandContext;
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
class PostponeTaskUseCaseTest {

    @Mock
    private RetrackerService retrackerService;

    @Mock
    private ApplicationEventPublisher eventPublisher;


    @InjectMocks
    private PostponeTaskUseCase postponeTaskUseCase;



@Test
void shouldHandleRequestsWithDatesInDifferentTimeZones() {
    // Arrange
    ZonedDateTime postponedDate = ZonedDateTime.of(2023, 1, 1, 23, 0, 0, 0, ZoneId.of("UTC"));
    PostponeTaskRequest request = new PostponeTaskRequest();
    request.setPostponedDate(postponedDate);
    
    Task task = mock(Task.class);
    CommandContext context = new CommandContext(UUID.randomUUID(), TimeZone.getTimeZone("Europe/Berlin"));

    // Act
    BaseResponse response = postponeTaskUseCase.handleTaskChange(task, request, context);
    
    // Assert
    verify(task).postponeUntil(LocalDate.of(2023, 1, 2));
    verify(retrackerService).save(task);
    assertThat(response.isSuccess()).isTrue();
}
}
