package de.lbakker77.retracker.main.retracker.controller;


import de.lbakker77.retracker.main.retracker.usecase.*;
import de.lbakker77.retracker.main.retracker.usecase.dtos.RetrackerEntryDto;
import de.lbakker77.retracker.main.retracker.usecase.dtos.RetrackerListDto;
import de.lbakker77.retracker.main.retracker.usecase.dtos.RetrackerOverviewEntryDto;
import de.lbakker77.retracker.main.shared.usercase.BaseResponse;
import de.lbakker77.retracker.main.shared.usercase.CreatedResponse;
import de.lbakker77.retracker.main.shared.usercase.UseCaseExecutor;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/retracker")
@AllArgsConstructor
public class RetrackerEntryController {
    private final RetrackerDataReader retrackerDataReader;
    private final UseCaseExecutor useCaseExecutor;

    @GetMapping()
    public List<RetrackerListDto> retrackerLists() {
        return retrackerDataReader.getAllRetrackerLists();
    }

    @GetMapping("overview/{listId}")
    public List<RetrackerOverviewEntryDto> getRetrackerList(@PathVariable UUID listId) {
        return retrackerDataReader.getRetrackerEntries(listId);
    }

    @GetMapping("/entry/{entryId}")
    public RetrackerEntryDto getRetrackerEntryById(@PathVariable UUID entryId) {
        return retrackerDataReader.getRetrackerEntryById(entryId);
    }

    @PutMapping("/entry")
    public BaseResponse updateRetrackerEntry(@RequestBody ChangeRetrackerDataRequest command) {
        return useCaseExecutor.execute(command);
    }

    @PostMapping("/entry/done")
    public BaseResponse markDone(@RequestBody MarkRetrackerEntryDoneRequest request) {
        return useCaseExecutor.execute(request);
    }

    @PostMapping("/entry/postpone")
    public BaseResponse postpone(@RequestBody PostponeRetrackerRequest request) {
        return useCaseExecutor.execute(request);
    }

    @DeleteMapping("/entry/{id}")
    public BaseResponse postpone(@PathVariable UUID id) {
        var deleteRequest = new DeleteRetrackerEntryRequest();
        deleteRequest.setId(id);
        return useCaseExecutor.execute(deleteRequest);
    }

    @PostMapping("/entry")
    public CreatedResponse generateEntry(@RequestBody CreateRetrackerEntryRequest request)  {
        return useCaseExecutor.execute(request);
    }

}
