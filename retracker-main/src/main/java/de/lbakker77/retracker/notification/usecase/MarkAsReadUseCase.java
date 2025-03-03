package de.lbakker77.retracker.notification.usecase;

import de.lbakker77.retracker.core.BaseResponse;
import de.lbakker77.retracker.core.BaseUseCaseHandler;
import de.lbakker77.retracker.core.CommandContext;
import de.lbakker77.retracker.notification.domain.UserNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MarkAsReadUseCase extends BaseUseCaseHandler<MarkAsReadRequest, BaseResponse> {

    private final UserNotificationRepository notificationRepository;

    @Override
    public BaseResponse handle(MarkAsReadRequest request, CommandContext context) {
        var notification = notificationRepository.findById(request.getNotificationId())
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        notification.setRead(true);
        notificationRepository.save(notification);

        return BaseResponse.ofSuccess();
    }
}