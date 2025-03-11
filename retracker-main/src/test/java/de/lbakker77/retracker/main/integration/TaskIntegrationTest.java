package de.lbakker77.retracker.main.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import de.lbakker77.retracker.core.CreatedResponse;
import de.lbakker77.retracker.main.domain.RecurrenceTimeUnit;
import de.lbakker77.retracker.main.domain.TaskCategory;
import de.lbakker77.retracker.main.usecase.dtos.RecurrenceConfigDto;
import de.lbakker77.retracker.main.usecase.dtos.RetrackerTaskDto;
import de.lbakker77.retracker.main.usecase.dtos.TaskCategoryDto;
import de.lbakker77.retracker.main.usecase.task.ChangeTaskDataRequest;
import de.lbakker77.retracker.main.usecase.task.CreateTaskRequest;
import de.lbakker77.retracker.main.usecase.task.MarkTaskDoneRequest;
import de.lbakker77.retracker.main.usecase.task.PostponeTaskRequest;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static de.lbakker77.retracker.main.integration.IntegrationTestDataFactory.getJwt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TaskIntegrationTest extends BaseIntegrationTest {
    @Test
    void shouldCreateAndReadTaskProperly() throws Exception {
        final var testUser = "task-create-user";
        var listId = dataFactory.createRetrackerList(testUser);
        final var testDueDate = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
        var request = new CreateTaskRequest();
        request.setListId(listId);
        request.setName("Test Task");
        request.setCategory(TaskCategory.PERSONAL);
        request.setDueDate(testDueDate);
        var createdResponseString = mockMvc.perform(post("/api/retracker/task")
                .with(getJwt(testUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        CreatedResponse createdResponse = objectMapper.readValue(createdResponseString, new TypeReference<>() {});
        assertNotNull(createdResponse.getId());
        var taskId = createdResponse.getId();

        var taskString = mockMvc.perform(get("/api/retracker/task/{taskId}", taskId)
                        .with(getJwt(testUser)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        RetrackerTaskDto task = objectMapper.readValue(taskString, new TypeReference<>() {});

        assertThat(task.id()).isEqualTo(taskId);
        assertThat(task.name()).isEqualTo("Test Task");
        assertThat(task.dueDate()).isEqualTo(testDueDate);
        assertThat(task.category().category()).isEqualTo(TaskCategory.PERSONAL);
        assertThat(task.recurrenceConfig()).isNull();
        assertThat(task.lastEntryDate()).isNull();

        var list = dataFactory.getRetrackerListById( testUser, listId);
        assertThat(list).isPresent();
        assertThat(list.get().dueCount()).isOne();
    }

    @Test
    void shouldCreateTaskAlreadyCompletedWithRecurrence() throws Exception {
        final var testUser = "task-create-completed-user";
        final var completionDate = LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault());
        var listId = dataFactory.createRetrackerList(testUser);
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

        var task = dataFactory.getTaskById(testUser, createdResponse.getId());
        assertThat(task.lastEntryDate()).isEqualTo(completionDate);
        assertThat(task.dueDate()).isEqualTo(completionDate.plusMonths(2));
        assertThat(task.recurrenceConfig()).isNotNull();
        assertThat(task.history()).hasSize(1);
        var historyEntry = task.history().getFirst();
        assertThat(historyEntry.completionDate()).isEqualTo(completionDate);
        assertThat(historyEntry.dueDate()).isNull();
        assertThat(historyEntry.overdueDays()).isZero();
        assertThat(historyEntry.postponedDays()).isZero();
    }

    @Test
    void canCompleteTask() throws Exception {
        final var testUser = "task-complete-user";
        final var testDueDate = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault());
        var listId = dataFactory.createRetrackerList(testUser);

        var taskId = dataFactory.createTask(listId , testUser, testDueDate);

        var markDoneRequest = new MarkTaskDoneRequest();
        markDoneRequest.setId(taskId);
        markDoneRequest.setDoneAt(testDueDate);

        mockMvc.perform(post("/api/retracker/task/done", taskId)
               .with(getJwt(testUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(markDoneRequest)))
                .andExpect(status().isOk());

        var task = dataFactory.getTaskById(testUser, taskId);

        assertThat(task.lastEntryDate()).isEqualTo(testDueDate);
        assertThat(task.dueDate()).isNull();
    }

    @Test
    void canPostponeTask() throws Exception {
        final var testUser = "task-postpone-user";
        final var testDueDate = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault());
        final var testPostponedDate = testDueDate.plusDays(2);
        var listId = dataFactory.createRetrackerList(testUser);

        var taskId = dataFactory.createTask(listId, testUser, testDueDate);

        var postponeRequest = new PostponeTaskRequest();
        postponeRequest.setId(taskId);
        postponeRequest.setPostponedDate(testPostponedDate);

        mockMvc.perform(post("/api/retracker/task/postpone", taskId)
               .with(getJwt(testUser))
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(postponeRequest)))
               .andExpect(status().isOk());

        var task = dataFactory.getTaskById(testUser, taskId);

        assertThat(task.dueDate()).isEqualTo(testPostponedDate);
        assertThat(task.lastEntryDate()).isNull();
    }

    @Test
    void canDeleteTask() throws Exception {
        final var testUser = "task-delete-user";
        var listId = dataFactory.createRetrackerList(testUser);
        var taskId = dataFactory.createTask(listId, testUser, ZonedDateTime.now());

        mockMvc.perform(delete("/api/retracker/task/{taskId}", taskId)
               .with(getJwt(testUser)))
               .andExpect(status().isOk());

        mockMvc.perform(get("/api/retracker/task/{taskId}", taskId)
                        .with(getJwt(testUser)))
                .andExpect(status().isNotFound());

    }

    @Test
    void canChangeTaskProperly() throws Exception {
        final var testUser = "task-change-user";
        var listId = dataFactory.createRetrackerList(testUser);
        var taskId = dataFactory.createTask(listId, testUser, ZonedDateTime.now());

        var changeRequest = new ChangeTaskDataRequest();
        changeRequest.setId(taskId);
        changeRequest.setName("Changed Task");
        changeRequest.setCategory(TaskCategory.EDUCATION_LEARNING);
        changeRequest.setRecurrenceConfig(new RecurrenceConfigDto( 1, RecurrenceTimeUnit.WEEK));

        mockMvc.perform(put("/api/retracker/task")
               .with(getJwt(testUser))
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(changeRequest)))
               .andExpect(status().isOk());

        var task = dataFactory.getTaskById(testUser, taskId);

        assertThat(task.name()).isEqualTo("Changed Task");
        assertThat(task.category().category()).isEqualTo(TaskCategory.EDUCATION_LEARNING);
        assertThat(task.recurrenceConfig()).isNotNull();
        assertThat(task.recurrenceConfig().recurrenceInterval()).isEqualTo(1);
        assertThat(task.recurrenceConfig().recurrenceTimeUnit()).isEqualTo(RecurrenceTimeUnit.WEEK);
    }

    @Test
    void canSetManualDueDateForTaskWithoutDueDate() throws Exception {
        final var testUser = "task-set-due-date-user";
        var listId = dataFactory.createRetrackerList(testUser);
        var taskId = dataFactory.createTask(listId, testUser, null);

        var newDueDate = LocalDate.now().plusDays(7).atStartOfDay(ZoneId.systemDefault());

        mockMvc.perform(post("/api/retracker/task/{id}/set-manual-due-date", taskId)
               .with(getJwt(testUser))
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(newDueDate)))
               .andExpect(status().isOk());

        var task = dataFactory.getTaskById(testUser, taskId);

        assertThat(task.dueDate()).isEqualTo(newDueDate);
    }

    @Test
    void shouldReturnAllCategoriesInGerman() throws Exception {
        var categories = mockMvc.perform(get("/api/retracker/task/categories")
                .with(getJwt("test-user"))
                        .locale(Locale.GERMAN)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andReturn().getResponse().getContentAsString();

        List<TaskCategoryDto> categoryList = objectMapper.readValue(categories, new TypeReference<>() {});

        TaskCategory[] allCategories = TaskCategory.values();
        assertThat(categoryList).hasSize(allCategories.length);
        assertTrue(Arrays.stream(allCategories).allMatch(c -> categoryList.stream().anyMatch(list -> list.category().equals(c))));
        assertTrue(categoryList.stream().anyMatch(list -> list.categoryName().equals("Allgemein")));
    }

    @Test
    void shouldReturnAllCategoriesInEnglish() throws Exception {
        var categories = mockMvc.perform(get("/api/retracker/task/categories")
                        .with(getJwt("test-user"))
                        .locale(Locale.ENGLISH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<TaskCategoryDto> categoryList = objectMapper.readValue(categories, new TypeReference<>() {});

        assertTrue(categoryList.stream().anyMatch(list -> list.categoryName().equals("General")));
    }

    @Test
    void canUndoLastCompletionOfTask() throws Exception {
        final var testUser = "undo-completion-user";
        var listId = dataFactory.createRetrackerList(testUser);
        var taskId = dataFactory.createTaskCompleted(listId, testUser, ZonedDateTime.now());


        mockMvc.perform(post("/api/retracker/task/{taskId}/undoLastCompletion", taskId)
               .with(getJwt(testUser)))
               .andExpect(status().isOk());

        var task = dataFactory.getTaskById(testUser, taskId);

        assertThat(task.lastEntryDate()).isNull();
        assertThat(task.history()).isEmpty();
    }
}
