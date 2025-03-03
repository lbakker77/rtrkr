package de.lbakker77.retracker.main.usecase.sharing;

import de.lbakker77.retracker.core.usecase.BaseResponse;
import de.lbakker77.retracker.core.usecase.CommandContext;
import de.lbakker77.retracker.main.domain.RetrackerList;
import de.lbakker77.retracker.main.domain.RetrackerService;
import de.lbakker77.retracker.main.usecase.constants.NotificationConstants;
import de.lbakker77.retracker.notification.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.TimeZone;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AcceptInvitationUseCaseTest {

    @Mock
    private RetrackerService retrackerService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private AcceptInvitationUseCase acceptInvitationUseCase;


    @Test
    void shouldAcceptInvitationAndUpdateListWhenUserIsInvited() {
        // Arrange
        var listId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        var request = new AcceptInvitationRequest(listId);
        var mockList = mock(RetrackerList.class);
        var commandContext = new CommandContext(userId, TimeZone.getDefault());

        when(retrackerService.loadListAndEnsureUserIsInvited(listId, userId)).thenReturn(mockList);
        when(mockList.getId()).thenReturn(listId);

        // Act
        BaseResponse response = acceptInvitationUseCase.handle(request, commandContext);

        // Assert
        verify(mockList).acceptInvitation(userId);
        verify(retrackerService).save(mockList);
        verify(notificationService).deleteInAppNotification(NotificationConstants.createInviteKey(listId, userId));
        assertTrue(response.isSuccess());
    }
}
