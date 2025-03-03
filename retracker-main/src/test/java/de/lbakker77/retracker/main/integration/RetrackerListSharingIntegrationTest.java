package de.lbakker77.retracker.main.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import de.lbakker77.retracker.main.domain.ShareStatus;
import de.lbakker77.retracker.main.usecase.dtos.RetrackerListDto;
import de.lbakker77.retracker.main.usecase.dtos.ShareConfigDto;
import de.lbakker77.retracker.main.usecase.sharing.InviteToListRequest;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static de.lbakker77.retracker.main.integration.IntegrationTestDataFactory.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class RetrackerListSharingIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldReturnDefaultListForNewUser() throws Exception {
        var listBody = mockMvc.perform(get("/api/retracker").with(getJwt("mew-user")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<RetrackerListDto> lists = objectMapper.readValue(listBody, new TypeReference<>() {});
        assertThat(lists).hasSize(1);
        assertThat(lists.getFirst().defaultList()).isTrue();
        assertThat(lists.getFirst().shared()).isFalse();
        assertThat(lists.getFirst().isInvitation()).isFalse();
        assertThat(lists.getFirst().dueCount()).isZero();
        assertThat(lists.getFirst().isOwner()).isTrue();
        assertThat(lists.getFirst().icon()).isNotEmpty();
    }

    @Test
    void canInviteUserToNonDefaultList() throws Exception {
        final var testUser = "invite-user";
        final var invitedUser = "invited-user";
        dataFactory.getAllRetrackerListOfUser(invitedUser); // also creates the user account
        var listId = dataFactory.createRetrackerList(testUser, "invite-list", "test-icon");

        var invitationRequest = new InviteToListRequest();
        invitationRequest.setListId(listId);
        invitationRequest.setEmail(getGeneratedEmail(invitedUser));

        mockMvc.perform(post("/api/retracker/share")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invitationRequest))
                        .with(getJwt(testUser)))
                .andExpect(status().isOk());

        var listsOfInvitedUser = dataFactory.getAllRetrackerListOfUser(invitedUser);

        assertThat(listsOfInvitedUser).hasSize(2);
        var invitedList = listsOfInvitedUser.stream().filter(l -> l.id().equals(listId)).findFirst();
        assertThat(invitedList).isPresent();
        assertThat(invitedList.get().isInvitation()).isTrue();
        assertThat(invitedList.get().shared()).isFalse();
        assertThat(invitedList.get().defaultList()).isFalse();
        assertThat(invitedList.get().dueCount()).isZero();
        assertThat(invitedList.get().icon()).isEqualTo("test-icon");
        assertThat(invitedList.get().name()).isEqualTo("invite-list");
    }

    @Test
    void canAcceptInvitation() throws Exception {
        final var testUser = "invite-user2";
        final var invitedUser = "invited-user-accepting";
        dataFactory.getAllRetrackerListOfUser(invitedUser); // also creates the user account
        var listId = dataFactory.createRetrackerList(testUser, "invite-list", "test-icon");
        dataFactory.inviteUserToRetrackerList(testUser, invitedUser, listId);

        mockMvc.perform(post("/api/retracker/{listId}/accept-invitation", listId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")
                        .with(getJwt(invitedUser)))
                .andExpect(status().isOk());

        var listsOfInvitedUser = dataFactory.getAllRetrackerListOfUser(invitedUser);

        var invitedList = listsOfInvitedUser.stream().filter(l -> l.id().equals(listId)).findFirst();
        assertThat(invitedList).isPresent();
        assertThat(invitedList.get().isInvitation()).isFalse();
        assertThat(invitedList.get().shared()).isTrue();
    }

    @Test
    void canRejectInvitation() throws Exception {
        final var testUser = "invite-user3";
        final var invitedUser = "invited-user-rejecting";
        dataFactory.getAllRetrackerListOfUser(invitedUser); // also creates the user account
        var listId = dataFactory.createRetrackerList(testUser, "invite-list", "test-icon");
        dataFactory.inviteUserToRetrackerList(testUser, invitedUser, listId);

        mockMvc.perform(post("/api/retracker/{listId}/reject-invitation", listId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")
                        .with(getJwt(invitedUser)))
                .andExpect(status().isOk());

        var listsOfInvitedUser = dataFactory.getAllRetrackerListOfUser(invitedUser);

        var invitedList = listsOfInvitedUser.stream().filter(l -> l.id().equals(listId)).findFirst();
        assertThat(invitedList).isEmpty();
    }

    @Test
    void canRemoveUserFromSharedList() throws Exception {
        final var testUser = "invite-user-delete";
        final var invitedUser = "invited-user-temp";
        dataFactory.getAllRetrackerListOfUser(invitedUser); // also creates the user account
        var listId = dataFactory.createRetrackerList(testUser, "invite-list", "test-icon");
        dataFactory.inviteUserToRetrackerList(testUser, invitedUser, listId);
        var userId = dataFactory.getUserIdOfUser(invitedUser);

        mockMvc.perform(delete("/api/retracker/{listId}/share/{userId}", listId, userId)
                        .with(getJwt(testUser)))
                .andExpect(status().isOk());

        var listsOfInvitedUser = dataFactory.getAllRetrackerListOfUser(invitedUser);
        var invitedList = listsOfInvitedUser.stream().filter(l -> l.id().equals(listId)).findFirst();
        assertThat(invitedList).isEmpty();
    }

    @Test
    void canReadShareConfigInformation() throws Exception {
        final var testUser = "share-config-owner";
        final var invitedUser1 = "invited-user-share-config1";
        final var invitedUser2 = "invited-user-share-config2";
        final var invitedUser3 = "invited-user-share-config3";
        dataFactory.getAllRetrackerListOfUser(invitedUser1); // also creates the user account
        dataFactory.getAllRetrackerListOfUser(invitedUser2); // also creates the user account
        var listId = dataFactory.createRetrackerList(testUser, "invite-list", "test-icon");
        dataFactory.inviteUserToRetrackerList(testUser, invitedUser1, listId);
        dataFactory.inviteUserToRetrackerList(testUser, invitedUser2, listId);
        dataFactory.inviteUserToRetrackerList(testUser, invitedUser3, listId);
        dataFactory.acceptInvitation(listId, invitedUser1);
        dataFactory.rejectInvitation(listId, invitedUser2);

        var listBody = mockMvc.perform(get("/api/retracker/{listId}/share", listId)
                        .with(getJwt(testUser)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<ShareConfigDto> shareConfigs = objectMapper.readValue(listBody, new TypeReference<>() {});

        assertThat(shareConfigs).hasSize(4);
        assertTrue(shareConfigs.stream().anyMatch(ShareConfigDto::isOwner));
        assertTrue(shareConfigs.stream().anyMatch(s -> s.email().equals(getGeneratedEmail(invitedUser1))));
        assertTrue(shareConfigs.stream().anyMatch(s -> s.email().equals(getGeneratedEmail(invitedUser2))));
        assertTrue(shareConfigs.stream().anyMatch(s -> s.email().equals(getGeneratedEmail(invitedUser3))));
        var shareConfigOfUser1 = shareConfigs.stream().filter(s -> s.email().equals(getGeneratedEmail(invitedUser1))).findFirst();
        assertThat(shareConfigOfUser1).isPresent();
        assertThat(shareConfigOfUser1.get().isOwner()).isFalse();
        assertThat(shareConfigOfUser1.get().email()).isEqualTo(getGeneratedEmail(invitedUser1));
        assertThat(shareConfigOfUser1.get().firstName()).isEqualTo(getGeneratedGivenName(invitedUser1));
        assertThat(shareConfigOfUser1.get().lastName()).isEqualTo(getGeneratedLastName(invitedUser1));
        assertThat(shareConfigOfUser1.get().status()).isEqualTo(ShareStatus.ACCEPTED);
    }

}
