package de.lbakker77.retracker.main.domain;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RetrackerListTest {
    private final RetrackerListCreator retrackerListCreator = new RetrackerListCreator();
    @Test
    void shouldCreateNewListWhenAllRequiredParametersAreProvided() {
        var ownerId = UUID.randomUUID();
        RetrackerList retrackerList = retrackerListCreator.createRetrackerList("Test List", ownerId, "icon");

        assertThat(retrackerList.getName()).isEqualTo("Test List");
        assertThat(retrackerList.getOwnerId()).isEqualTo(ownerId);
        assertThat(retrackerList.getIcon()).isEqualTo("icon");
    }

    @Test
    void shouldNotCreateListWhenNameIsMissing() {
        var ownerId = UUID.randomUUID();
        assertThrows(IllegalArgumentException.class, () -> retrackerListCreator.createRetrackerList("", ownerId, "icon"));
    }

    @Test
    void shouldNotCreateListWhenIconIsMissing() {
        var ownerId = UUID.randomUUID();
        assertThrows(IllegalArgumentException.class, () -> retrackerListCreator.createRetrackerList("Test List", ownerId, ""));
    }

    @Test
    void shouldCreateDefaultListCorrectly() {
        var ownerId = UUID.randomUUID();
        RetrackerList retrackerList = retrackerListCreator.createDefaultList(ownerId);

        assertThat(retrackerList.getName()).isEqualTo("default");
        assertThat(retrackerList.getOwnerId()).isEqualTo(ownerId);
        assertThat(retrackerList.getIcon()).isEqualTo("schedule");
        assertTrue(retrackerList.isDefaultList());
        assertFalse(retrackerList.isShared());
    }


    @Test
    void shouldStoreInvitationsCorrectly() {
        var retrackerList = new RetrackerList();
        var userId1 = UUID.randomUUID();
        var userId2 = UUID.randomUUID();
        retrackerList.inviteUser(userId1);
        retrackerList.inviteUser(userId2);
        retrackerList.setOwnerId(UUID.randomUUID());

        assertTrue(retrackerList.isInvited(userId1));
        assertTrue(retrackerList.isInvited(userId2));
        assertFalse(retrackerList.hasAccess(userId1));
        assertFalse(retrackerList.isShared());

        assertTrue(retrackerList.getShareConfigEntries().stream().anyMatch(shareConfig -> shareConfig.getSharedWithUserId().equals(userId1) && shareConfig.getStatus() == ShareStatus.PENDING));
        assertTrue(retrackerList.getShareConfigEntries().stream().anyMatch(shareConfig -> shareConfig.getSharedWithUserId().equals(userId2) && shareConfig.getStatus() == ShareStatus.PENDING));
    }

    @Test
    void shouldPreventDuplicateInvitations() {
        var retrackerList = new RetrackerList();
        UUID userId = UUID.randomUUID();
        retrackerList.inviteUser(userId);
        assertThrows(IllegalStateException.class, () -> retrackerList.inviteUser(userId));
    }

    @Test
    void shouldPreventOwnerFromBeingInvited() {
        var retrackerList = new RetrackerList();
        UUID userId = UUID.randomUUID();
        retrackerList.setOwnerId(userId);
        assertThrows(IllegalArgumentException.class, () -> retrackerList.inviteUser(userId));
    }

    @Test
    void shouldAcceptInvitations() {
        var retrackerList = new RetrackerList();
        UUID userId1 = UUID.randomUUID();
        retrackerList.setOwnerId(UUID.randomUUID());
        retrackerList.inviteUser(userId1);
        retrackerList.acceptInvitation(userId1);

        assertFalse(retrackerList.isInvited(userId1));
        assertTrue(retrackerList.hasAccess(userId1));
        assertTrue(retrackerList.isShared());
        assertTrue(retrackerList.getShareConfigEntries().stream().noneMatch(shareConfig -> shareConfig.getSharedWithUserId().equals(userId1) && shareConfig.getStatus() == ShareStatus.PENDING));
        assertTrue(retrackerList.getShareConfigEntries().stream().anyMatch(shareConfig -> shareConfig.getSharedWithUserId().equals(userId1) && shareConfig.getStatus() == ShareStatus.ACCEPTED));
    }

    @Test
    void shouldDenyInvitationsWhenNotInvited() {
        var retrackerList = new RetrackerList();
        UUID userId = UUID.randomUUID();
        retrackerList.setOwnerId(UUID.randomUUID());
        assertThrows(IllegalArgumentException.class, () -> retrackerList.acceptInvitation(userId));
    }

    @Test
    void shouldPreventManualModificationOfSharedConfigEntries() {
        var retrackerList = new RetrackerList();
        var sharedConfigs = retrackerList.getShareConfigEntries();
        var manualConfig = new ShareConfig(UUID.randomUUID(), ShareStatus.ACCEPTED, ZonedDateTime.now());

        assertThrows(UnsupportedOperationException.class, () -> sharedConfigs.add(manualConfig));
    }

    @Test
    void shouldRemoveAccessToListIfShared() {
        var retrackerList = new RetrackerList();
        UUID userId = UUID.randomUUID();
        retrackerList.setOwnerId(UUID.randomUUID());
        retrackerList.inviteUser(userId);
        retrackerList.acceptInvitation(userId);
        retrackerList.removeAccess(userId);

        assertFalse(retrackerList.hasAccess(userId));
        assertFalse(retrackerList.isShared());
        assertTrue(retrackerList.getShareConfigEntries().isEmpty());
    }

    @Test
    void shouldChangeNameAndIcon() {
        var retrackerList = new RetrackerList();
        retrackerList.changeName("New List Name");
        retrackerList.changeIcon("new_icon");

        assertThat(retrackerList.getName()).isEqualTo("New List Name");
        assertThat(retrackerList.getIcon()).isEqualTo("new_icon");
    }

}
