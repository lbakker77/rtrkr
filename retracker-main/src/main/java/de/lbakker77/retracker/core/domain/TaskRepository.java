package de.lbakker77.retracker.core.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TaskRepository extends CrudRepository<Task, UUID> {
    List<Task> findByRetrackerListIdOrderByDueDateAscNameAsc(UUID listId);

    long countByRetrackerListIdAndDueDateLessThanEqual(UUID listId, LocalDate date);

    @Query("SELECT task FROM Task task JOIN task.retrackerList list LEFT JOIN list.shareConfigEntries sc WHERE list.ownerId = ?1 OR (sc.sharedWithUserId = ?1 and sc.status = de.lbakker77.retracker.core.domain.ShareStatus.ACCEPTED) order by task.dueDate asc, task.name asc")
    List<Task> findAllTasksOfUser(UUID userId);
}

