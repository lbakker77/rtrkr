package de.lbakker77.retracker.main.controller;


import de.lbakker77.retracker.main.usecase.dtos.RetrackerTaskDto;
import de.lbakker77.retracker.main.usecase.dtos.RetrackerTaskOverviewDto;
import de.lbakker77.retracker.main.usecase.task.*;
import de.lbakker77.retracker.core.usercase.BaseResponse;
import de.lbakker77.retracker.core.usercase.CreatedResponse;
import de.lbakker77.retracker.core.usercase.UseCaseExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/retracker/task")
@RequiredArgsConstructor()
public class TaskController {
    private final RetrackerTaskReader retrackerTaskReader;
    private final UseCaseExecutor useCaseExecutor;


    @GetMapping("/overview/all")
    public List<RetrackerTaskOverviewDto> getAllRetrackerTasks() {
        return retrackerTaskReader.getAllRetrackerTasks();
    }

    @GetMapping("/overview/{listId}")
    public List<RetrackerTaskOverviewDto> getRetrackerList(@PathVariable UUID listId) {
        return retrackerTaskReader.getRetrackerTasks(listId);
    }

    @GetMapping("/{entryId}")
    public RetrackerTaskDto getRetrackerEntryById(@PathVariable UUID entryId) {
        return retrackerTaskReader.getRetrackerTaskById(entryId);
    }

    @PutMapping()
    public BaseResponse updateRetrackerEntry(@RequestBody ChangeRetrackerDataRequest command) {
        return useCaseExecutor.execute(command);
    }

    @PostMapping("/done")
    public BaseResponse markDone(@RequestBody MarkRetrackerTaskDoneRequest request) {
        return useCaseExecutor.execute(request);
    }

    @PostMapping("/postpone")
    public BaseResponse postpone(@RequestBody PostponeRetrackerTaskRequest request) {
        return useCaseExecutor.execute(request);
    }

    @PostMapping("/{id}/undoLastCompletion")
    public BaseResponse undoLastCompletion(@PathVariable UUID id) {
        return useCaseExecutor.execute(new UndoLastCompletionRequest(id));
    }

    @DeleteMapping("/{id}")
    public BaseResponse delete(@PathVariable UUID id) {
        var deleteRequest = new DeleteRetrackerTaskRequest();
        deleteRequest.setId(id);
        return useCaseExecutor.execute(deleteRequest);
    }

    @PostMapping()
    public CreatedResponse createTask(@RequestBody CreateRetrackerTaskRequest request)  {
        return useCaseExecutor.execute(request);
    }

    @PostMapping("/{id}/set-manual-due-date")
    public BaseResponse setManualDueDate(@PathVariable UUID id, @RequestBody ZonedDateTime manuelDueDate )  {
        return useCaseExecutor.execute(new SetManualDueDateRequest(id, manuelDueDate)  );
    }

}
