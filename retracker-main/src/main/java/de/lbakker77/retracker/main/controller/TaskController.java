package de.lbakker77.retracker.main.controller;


import de.lbakker77.retracker.core.BaseResponse;
import de.lbakker77.retracker.core.CreatedResponse;
import de.lbakker77.retracker.core.UseCaseExecutor;
import de.lbakker77.retracker.main.usecase.dtos.RetrackerTaskDto;
import de.lbakker77.retracker.main.usecase.dtos.RetrackerTaskOverviewDto;
import de.lbakker77.retracker.main.usecase.dtos.TaskCategoryDto;
import de.lbakker77.retracker.main.usecase.task.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/retracker/task")
@RequiredArgsConstructor()
public class TaskController {
    private final TaskReader taskReader;
    private final UseCaseExecutor useCaseExecutor;


    @GetMapping("/overview/all")
    public List<RetrackerTaskOverviewDto> getAllRetrackerTasks() {
        return taskReader.getAllRetrackerTasks();
    }

    @GetMapping("/overview/{listId}")
    public List<RetrackerTaskOverviewDto> getRetrackerList(@PathVariable UUID listId) {
        return taskReader.getRetrackerTasks(listId);
    }

    @GetMapping("/{id}")
    public RetrackerTaskDto getRetrackerEntryById(@PathVariable UUID id) {
        return taskReader.getRetrackerTaskById(id);
    }

    @PutMapping()
    public BaseResponse updateRetrackerEntry(@RequestBody ChangeTaskDataRequest command) {
        return useCaseExecutor.execute(command);
    }

    @PostMapping("/done")
    public BaseResponse markDone(@RequestBody MarkTaskDoneRequest request) {
        return useCaseExecutor.execute(request);
    }

    @PostMapping("/postpone")
    public BaseResponse postpone(@RequestBody PostponeTaskRequest request) {
        return useCaseExecutor.execute(request);
    }

    @PostMapping("/{id}/undoLastCompletion")
    public BaseResponse undoLastCompletion(@PathVariable UUID id) {
        return useCaseExecutor.execute(new UndoLastCompletionRequest(id));
    }

    @DeleteMapping("/{id}")
    public BaseResponse delete(@PathVariable UUID id) {
        var deleteRequest = new DeleteTaskRequest();
        deleteRequest.setId(id);
        return useCaseExecutor.execute(deleteRequest);
    }

    @PostMapping()
    public CreatedResponse createTask(@RequestBody CreateTaskRequest request)  {
        return useCaseExecutor.execute(request);
    }

    @PostMapping("/{id}/set-manual-due-date")
    public BaseResponse setManualDueDate(@PathVariable UUID id, @RequestBody ZonedDateTime manuelDueDate )  {
        return useCaseExecutor.execute(new SetManualDueDateRequest(id, manuelDueDate)  );
    }

    @GetMapping("/categories")
    public List<TaskCategoryDto> getAllTaskCategories() {
        return taskReader.getAllTaskCategories();
    }

}
