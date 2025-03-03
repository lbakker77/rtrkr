package de.lbakker77.retracker.main.integration;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.lbakker77.retracker.core.usecase.CreatedResponse;
import de.lbakker77.retracker.main.domain.RecurrenceTimeUnit;
import de.lbakker77.retracker.main.domain.TaskCategory;
import de.lbakker77.retracker.main.usecase.dtos.RecurrenceConfigDto;
import de.lbakker77.retracker.main.usecase.dtos.RetrackerListDto;
import de.lbakker77.retracker.main.usecase.dtos.RetrackerTaskDto;
import de.lbakker77.retracker.main.usecase.list.CreateRetrackerListRequest;
import de.lbakker77.retracker.main.usecase.sharing.InviteToListRequest;
import de.lbakker77.retracker.main.usecase.task.CreateTaskRequest;
import jakarta.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestComponent
public class IntegrationTestDataFactory {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    public static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor getJwt(String testUser) {
        return SecurityMockMvcRequestPostProcessors.jwt().jwt(t -> t.claim("sub", "test-user")
                .claim("email", getGeneratedEmail(testUser))
                .claim("given_name", getGeneratedGivenName(testUser))
                .claim("family_name", getGeneratedLastName(testUser)));
    }

    public static String getGeneratedEmail(String testUser) {
        return "test@doesnotexist_" + testUser + ".com";
    }
    public static String getGeneratedGivenName(String testUser) {
        return "first" + testUser;
    }
    public static String getGeneratedLastName(String testUser) {
        return "last" + testUser;
    }

    Optional<RetrackerListDto> getRetrackerListById(String testUser, UUID listId) throws Exception {
        var lists = getAllRetrackerListOfUser(testUser);
        return lists.stream().filter(l -> l.id().equals(listId)).findFirst();
    }

    List<RetrackerListDto> getAllRetrackerListOfUser(String testUser) throws Exception {
        var listBody = mockMvc.perform(get("/api/retracker").with(getJwt(testUser)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(listBody, new TypeReference<>() {});
    }

    UUID createRetrackerList(String testUser) throws Exception {
        return createRetrackerList(testUser, "Test List", "icon");
    }

    UUID createRetrackerList(String testUser, String listname, String icon) throws Exception {
        var request = new CreateRetrackerListRequest();
        request.setName(listname);
        request.setIcon(icon);

        var responseBody = mockMvc.perform(post("/api/retracker")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(getJwt(testUser)))
                .andReturn().getResponse().getContentAsString();
        var createdResponse = objectMapper.readValue(responseBody, CreatedResponse.class);
        return createdResponse.getId();
    }

    void inviteUserToRetrackerList(String testUser, String userToInvite, UUID listId) throws Exception {
        var invitationRequest = new InviteToListRequest();
        invitationRequest.setListId(listId);
        invitationRequest.setEmail(getGeneratedEmail(userToInvite));
        mockMvc.perform(post("/api/retracker/share")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invitationRequest))
                        .with(getJwt(testUser)))
                .andExpect(status().isOk());
    }

    UUID getUserIdOfUser(String testuser) throws Exception {
        var uuidString = mockMvc.perform(get("/api/user/id").with(getJwt(testuser)))
                .andReturn().getResponse().getContentAsString();
        return UUID.fromString(objectMapper.readValue(uuidString, String.class));
    }

    void acceptInvitation(UUID listId, String userId) throws Exception {
        mockMvc.perform(post("/api/retracker/{listId}/accept-invitation", listId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")
                        .with(getJwt(userId)))
                .andExpect(status().isOk());
    }

    void rejectInvitation(UUID listId, String userId) throws Exception {
        mockMvc.perform(post("/api/retracker/{listId}/reject-invitation", listId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")
                        .with(getJwt(userId)))
                .andExpect(status().isOk());
    }

    UUID createTask(UUID listId, String testUser, ZonedDateTime dueDate) throws Exception {
        var request = new CreateTaskRequest();
        request.setListId(listId);
        request.setName("Test Task");
        request.setCategory(TaskCategory.PERSONAL);
        request.setDueDate(dueDate);
        var createdResponseString = mockMvc.perform(post("/api/retracker/task")
                        .with(getJwt(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        CreatedResponse createdResponse = objectMapper.readValue(createdResponseString, new TypeReference<>() {});
        assertNotNull(createdResponse.getId());
        return createdResponse.getId();
    }

    UUID createTaskCompleted(UUID listId, String testUser, ZonedDateTime completionDate) throws Exception {
        var request = new CreateTaskRequest();
        request.setListId(listId);
        request.setName("Test Task");
        request.setCategory(TaskCategory.PERSONAL);
        request.setLastEntryDate(completionDate);
        request.setRecurrenceConfig(new RecurrenceConfigDto(2, RecurrenceTimeUnit.MONTH));
        var createdResponseString = mockMvc.perform(post("/api/retracker/task")
                        .with(getJwt(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        CreatedResponse createdResponse = objectMapper.readValue(createdResponseString, new TypeReference<>() {});
        assertNotNull(createdResponse.getId());
        return createdResponse.getId();
    }

    RetrackerTaskDto getTaskById(String testUser, UUID taskId) throws Exception {
        var taskString = mockMvc.perform(get("/api/retracker/task/{taskId}", taskId)
                        .with(getJwt(testUser)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(taskString, new TypeReference<>() {});
    }

}
