package de.lbakker77.retracker.main.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import de.lbakker77.retracker.core.usecase.BaseResponse;
import de.lbakker77.retracker.core.usecase.CreatedResponse;
import de.lbakker77.retracker.main.usecase.dtos.RetrackerListDto;
import de.lbakker77.retracker.main.usecase.list.ChangeRetrackerListRequest;
import de.lbakker77.retracker.main.usecase.list.CreateRetrackerListRequest;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static de.lbakker77.retracker.main.integration.IntegrationTestDataFactory.getJwt;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class RetrackerListIntegrationTest extends BaseIntegrationTest{

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
    void shouldCreateANewListProperly() throws Exception {
        var request = new CreateRetrackerListRequest();
        request.setName("Test List");
        request.setIcon("icon");

        var testUser = "create-list-user";
        var responseBody = mockMvc.perform(post("/api/retracker")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(getJwt(testUser)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        var createdResponse = objectMapper.readValue(responseBody, CreatedResponse.class);
        assertThat(createdResponse.getId()).isNotNull();

        var lists = dataFactory.getAllRetrackerListOfUser(testUser);
        assertThat(lists).hasSize(2);
        var createdList = lists.stream().filter(l -> l.id().equals(createdResponse.getId())).findFirst();
        assertThat(createdList).isPresent();
        assertThat(createdList.get().defaultList()).isFalse();
        assertThat(createdList.get().shared()).isFalse();
        assertThat(createdList.get().isInvitation()).isFalse();
        assertThat(createdList.get().dueCount()).isZero();
        assertThat(createdList.get().name()).isEqualTo("Test List");
        assertThat(createdList.get().icon()).isEqualTo("icon");
    }

    @ParameterizedTest
    @MethodSource("provideInvalidListData")
    void shouldNotCreateListForInvalidData(String name, String icon, String errorScenario) throws Exception {
        var request = new CreateRetrackerListRequest();
        request.setName(name);
        request.setIcon(icon);

        try {
            var response = mockMvc.perform(post("/api/retracker")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .with(getJwt("test-user-create-invalid-data")))
                    .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
            var errorResponse = objectMapper.readValue(response, BaseResponse.class);
            assertFalse(errorResponse.isSuccess());
            assertThat(errorResponse.getViolations()).hasSizeGreaterThan(0);
        } catch (AssertionError e) {
            throw new AssertionError("Test failed for scenario: " + errorScenario, e);
        }
    }

    @ParameterizedTest
    @MethodSource("provideInvalidListData")
    void shouldNotChangeListForInvalidData(String name, String icon, String errorScenario) throws Exception {
        final var testUser = "test-user-change-invalid-data";
        var listId = dataFactory.createRetrackerList(testUser, "test-list", "test-icon");

        var changeRequest = new ChangeRetrackerListRequest();
        changeRequest.setId(listId);
        changeRequest.setName(name);
        changeRequest.setIcon(icon);
        try {

            var response = mockMvc.perform(put("/api/retracker")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(changeRequest))
                            .with(getJwt(testUser)))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString();
            var errorResponse = objectMapper.readValue(response, BaseResponse.class);
            assertFalse(errorResponse.isSuccess());
            assertThat(errorResponse.getViolations()).hasSizeGreaterThan(0);
        } catch (AssertionError e) {
            throw new AssertionError("Test failed for scenario: " + errorScenario, e);
        }
    }


    private static Stream<Arguments> provideInvalidListData() {
        return Stream.of(
                Arguments.of("", "icon", "List name cannot be empty"),
                Arguments.of("Valid Name", "", "List icon cannot be empty"),
                Arguments.of(null, "icon", "List name is required"),
                Arguments.of("12345678901234567890", null, "List icon is required"),
                Arguments.of("0123456789012345678901", "icon", "Name should not exceed 20 characters")
        );
    }


    @Test
    void shouldDeleteListProperly() throws Exception {
        var request = new CreateRetrackerListRequest();
        request.setName("Test List 2 2 2 2");
        request.setIcon("icon");

        var testUser = "delete-test-user";
        var responseBody = mockMvc.perform(post("/api/retracker")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(getJwt(testUser)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        var createdResponse = objectMapper.readValue(responseBody, CreatedResponse.class);
        assertThat(createdResponse.getId()).isNotNull();

        mockMvc.perform(delete("/api/retracker/{listId}", createdResponse.getId()) .with(getJwt(testUser))).andExpect(status().isOk());

        var lists = dataFactory.getAllRetrackerListOfUser(testUser);
        assertThat(lists.stream().filter(l -> l.id().equals(createdResponse.getId())).findFirst()).isEmpty();
    }

    @Test
    void shouldPerformChangeToListProperly() throws Exception {
        var testUser = "change-list-user";

        var listId = dataFactory.createRetrackerList(testUser, "test-list", "test-icon");

        var changeRequest = new ChangeRetrackerListRequest();
        changeRequest.setId(listId);
        changeRequest.setName("changed-list");
        changeRequest.setIcon("changed-icon");
        mockMvc.perform(put("/api/retracker")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(changeRequest))
                    .with(getJwt(testUser)))
                .andExpect(status().isOk());

        var changedList = dataFactory.getRetrackerListById(testUser, listId);
        assertThat(changedList).isPresent();
        assertThat(changedList.get().name()).isEqualTo("changed-list");
        assertThat(changedList.get().icon()).isEqualTo("changed-icon");
    }

    @Test
    void cannotChangeListOfOtherUser() throws Exception {
        var testUser = "change-list-user";
        var otherUser = "other-user";

        var listId = dataFactory.createRetrackerList(testUser, "test-list", "test-icon");

        var changeRequest = new ChangeRetrackerListRequest();
        changeRequest.setId(listId);
        changeRequest.setName("changed-list");
        changeRequest.setIcon("changed-icon");
        mockMvc.perform(put("/api/retracker")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(changeRequest))
                    .with(getJwt(otherUser)))
                .andExpect(status().isForbidden());
    }
}
