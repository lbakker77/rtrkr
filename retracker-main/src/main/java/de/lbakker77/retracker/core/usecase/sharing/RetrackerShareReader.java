package de.lbakker77.retracker.main.core.usecase.sharing;

import de.lbakker77.retracker.main.core.domain.RetrackerService;
import de.lbakker77.retracker.main.core.domain.ShareConfig;
import de.lbakker77.retracker.main.core.domain.ShareStatus;
import de.lbakker77.retracker.main.core.usecase.dtos.ShareConfigDto;
import de.lbakker77.retracker.main.user.UserService;
import de.lbakker77.retracker.main.user.usecase.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RetrackerShareReader {
    private final RetrackerService retrackerService;
    private final UserService userService;

    public List<ShareConfigDto> getShareInfo(UUID listId) {
        var currentUserId = userService.getUserIdOrCreateIfNew();

        var list = retrackerService.loadRetrackerListAndEnsureAccess(listId, currentUserId);

        var userIds = new HashSet<UUID>();
        userIds.add(list.getOwnerId());
        userIds.addAll(list.getShareConfigEntries().stream()
                .map(ShareConfig::getSharedWithUserId)
                .collect(Collectors.toSet()));

        var userDtos = userService.retrieveUserInfos(userIds);
        var userDtoMap = userDtos.stream()
                .collect(Collectors.toMap(UserDto::id, dto -> dto));

        var shareConfigs = new ArrayList<ShareConfigDto>();

        // Add owner
        var ownerDto = userDtoMap.get(list.getOwnerId());
        shareConfigs.add(new ShareConfigDto(
                ownerDto.id(),
                ownerDto.email(),
                ownerDto.firstName(),
                ownerDto.lastName(),
                ShareStatus.ACCEPTED,
                null,
                true
        ));

        // Add shared users
        shareConfigs.addAll(list.getShareConfigEntries().stream()
                .map(shareConfig -> {
                    UserDto userDto = userDtoMap.get(shareConfig.getSharedWithUserId());
                    return new ShareConfigDto(
                            userDto.id(),
                            userDto.email(),
                            userDto.firstName(),
                            userDto.lastName(),
                            shareConfig.getStatus(),
                            shareConfig.getSharedAt(),
                            false // Not the owner
                    );
                })
                .toList());

        return shareConfigs;
    }

    public List<UserDto> getKnownUsersToShareWith(UUID listId) {
        var currentUserId = userService.getUserIdOrCreateIfNew();

        var currentList = retrackerService.loadRetrackerListAndEnsureAccess(listId, currentUserId);
        var currentListSharedUserIds = currentList.getShareConfigEntries().stream().map(ShareConfig::getSharedWithUserId).toList();
        var allKnowUserIds = retrackerService.getSharedUserIds(currentUserId);
        var relevantUsers =  allKnowUserIds.stream().filter(id -> !currentListSharedUserIds.contains(id)).collect(Collectors.toSet());

        return userService.retrieveUserInfos(relevantUsers);
    }

}
