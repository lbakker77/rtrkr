package de.lbakker77.retracker.main.controller;

import de.lbakker77.retracker.core.usecase.BaseResponse;
import de.lbakker77.retracker.core.usecase.CreatedResponse;
import de.lbakker77.retracker.core.usecase.UseCaseExecutor;
import de.lbakker77.retracker.main.usecase.dtos.RetrackerListDto;
import de.lbakker77.retracker.main.usecase.dtos.ShareConfigDto;
import de.lbakker77.retracker.main.usecase.list.ChangeRetrackerListRequest;
import de.lbakker77.retracker.main.usecase.list.CreateRetrackerListRequest;
import de.lbakker77.retracker.main.usecase.list.DeleteRetrackerListRequest;
import de.lbakker77.retracker.main.usecase.list.RetrackerListReader;
import de.lbakker77.retracker.main.usecase.sharing.*;
import de.lbakker77.retracker.user.usecase.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/retracker")
@RequiredArgsConstructor
public class RetrackerListController {
    private final RetrackerListReader retrackerListReader;
    private final RetrackerShareReader retrackerShareReader;
    private final UseCaseExecutor useCaseExecutor;


    @GetMapping()
    public List<RetrackerListDto> retrackerLists() {
        return retrackerListReader.getAllRetrackerLists();
    }

    @PostMapping
    public CreatedResponse create(@RequestBody CreateRetrackerListRequest request) {
        return useCaseExecutor.execute(request);
    }

    @DeleteMapping("/{listId}")
    public BaseResponse delete(@PathVariable UUID listId) {
        return useCaseExecutor.execute(new DeleteRetrackerListRequest(listId), BaseResponse.class);
    }

    @GetMapping("/{listId}/due-count")
    public long getDueCount(@PathVariable UUID listId) {
        return retrackerListReader.getDueCount(listId);
    }

    @PutMapping
    public BaseResponse change(@RequestBody ChangeRetrackerListRequest request) {
        return useCaseExecutor.execute(request);
    }

    @GetMapping("/{listId}/known-users-to-share-with")
    public List<UserDto> getKnownUsers(@PathVariable UUID listId) {
        return retrackerShareReader.getKnownUsersToShareWith(listId);
    }

    @PostMapping("/share")
    public BaseResponse shareRetrackerList(@RequestBody InviteToListRequest request) {
       return useCaseExecutor.execute(request);
    }

    @GetMapping("{listId}/share")
    public List<ShareConfigDto> getShareConfigs(@PathVariable UUID listId) {
        return retrackerShareReader.getShareConfigs(listId);
    }

    @DeleteMapping("/{listId}/share/{userId}")
    public BaseResponse deleteShare(@PathVariable UUID listId, @PathVariable UUID userId) {
        return useCaseExecutor.execute(new ShareListRemoveRequest(listId, userId));
    }

    @PostMapping("/{listId}/accept-invitation")
    public BaseResponse acceptInvitation(@PathVariable UUID listId)  {
        return useCaseExecutor.execute(new AcceptInvitationRequest(listId));
    }

    @PostMapping("/{listId}/reject-invitation")
    public BaseResponse rejectInvitation(@PathVariable UUID listId)  {
        return useCaseExecutor.execute(new RejectInvitationRequest(listId));
    }
}
