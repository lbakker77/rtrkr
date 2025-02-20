package de.lbakker77.retracker.main.usecase.constants;

import java.util.UUID;

public class NotificationConstants {
    private NotificationConstants() {}

    public static final String INVITE_KEY_PREFIX = "invite_to_list:";

    public static final String INVITE_ACTION_COMMAND = "show_invite:";

    public static String createInviteKey(UUID listId, UUID userId) {
        return INVITE_KEY_PREFIX + listId.toString() + ":" + userId.toString();
    }

    public static String createInviteActionCommand(UUID listId) {
        return INVITE_ACTION_COMMAND + listId.toString();
    }
}
