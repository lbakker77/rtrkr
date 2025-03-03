package de.lbakker77.retracker.main.usecase.sharing;

import de.lbakker77.retracker.core.BaseResponse;
import de.lbakker77.retracker.core.CommandContext;
import de.lbakker77.retracker.main.domain.RetrackerList;
import de.lbakker77.retracker.main.domain.RetrackerService;
import de.lbakker77.retracker.notification.NotificationService;
import de.lbakker77.retracker.user.UserService;
import de.lbakker77.retracker.user.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InviteToListUseCaseTest {

    @Mock
    private RetrackerService retrackerService;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private InviteToListUseCase inviteToListUseCase;


    @Test
    void shouldSuccessfullyInviteNewUserToList() {
        // Arrange
        UUID listId = UUID.randomUUID();
        UUID currentUserId = UUID.randomUUID();
        String email = "newuser@example.com";
        UUID newUserId = UUID.randomUUID();
        var currentUser = new UserDto(UUID.randomUUID(), "John", "Doe", "johndoe@example.com");
        RetrackerList mockList = mock(RetrackerList.class);
        InviteToListRequest request = new InviteToListRequest(listId, email);
        CommandContext commandContext = new CommandContext(currentUserId, TimeZone.getDefault());

        when(retrackerService.loadRetrackerListAndEnsureAccess(listId, currentUserId)).thenReturn(mockList);
        when(userService.getUserId(email)).thenReturn(Optional.empty());
        when(userService.inviteUser(email)).thenReturn(newUserId);
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(mockList.getId()).thenReturn(listId);
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("Invitation sent");

        // Act
        BaseResponse response = inviteToListUseCase.handle(request, commandContext);

        // Assert
        verify(mockList).inviteUser(newUserId);
        verify(retrackerService).save(mockList);
        verify(notificationService).sendInAppNotification(anyString(), eq(newUserId), eq("Invitation sent"), anyString());
        assertThat(response.isSuccess()).isTrue();
    }


}
