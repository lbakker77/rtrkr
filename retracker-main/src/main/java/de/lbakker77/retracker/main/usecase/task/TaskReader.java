package de.lbakker77.retracker.main.usecase.task;

import de.lbakker77.retracker.core.UserTimeZoneService;
import de.lbakker77.retracker.main.domain.RetrackerService;
import de.lbakker77.retracker.main.domain.TaskCategory;
import de.lbakker77.retracker.main.domain.TaskRepository;
import de.lbakker77.retracker.main.usecase.dtos.RetrackerTaskDto;
import de.lbakker77.retracker.main.usecase.dtos.RetrackerTaskOverviewDto;
import de.lbakker77.retracker.main.usecase.dtos.TaskCategoryDto;
import de.lbakker77.retracker.main.usecase.mapper.RetrackerMapper;
import de.lbakker77.retracker.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskReader {
    private final TaskRepository retrackerTaskRepository;
    private final RetrackerService retrackerService;
    private final RetrackerMapper retrackerMapper;
    private final UserTimeZoneService userTimeZoneService;
    private final UserService userService;


    public List<RetrackerTaskOverviewDto> getRetrackerTasks(UUID retrackerListId) {
        retrackerService.loadRetrackerListAndEnsureAccess(retrackerListId, userService.getUserIdOrCreateIfNew());
        return retrackerMapper.toRetrackerOverviewTaskDtos(retrackerTaskRepository.findByRetrackerListIdOrderByDueDateAscNameAsc(retrackerListId), userTimeZoneService.getUserTimeZone());
    }

    public RetrackerTaskDto getRetrackerTaskById(UUID retrackerEntryId) {
        var task = retrackerService.loadTaskAndEnsureAccess(retrackerEntryId, userService.getUserIdOrCreateIfNew());
        return retrackerMapper.toRetrackerTaskDto(task, userTimeZoneService.getUserTimeZone());
    }

    public List<RetrackerTaskOverviewDto> getAllRetrackerTasks() {
        var userId = userService.getCurrentUserId();
        var allTasks = retrackerTaskRepository.findAllTasksOfUser(userId);
        return retrackerMapper.toRetrackerOverviewTaskDtos(allTasks, userTimeZoneService.getUserTimeZone());
    }

    public List<TaskCategoryDto> getAllTaskCategories() {
        var categories = TaskCategory.values();
        return Arrays.stream(categories).map(retrackerMapper::toTaskCategoryDto).toList();
    }
}
