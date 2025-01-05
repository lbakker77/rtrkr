package de.lbakker77.retracker.main.retracker.entity;

import de.lbakker77.retracker.main.retracker.entity.model.RetrackerList;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface RetrackerListRepository  extends CrudRepository<RetrackerList, UUID> {

    @Query("SELECT list FROM RetrackerList list LEFT JOIN list.shareConfigEntries sc WHERE list.ownerId = ?1 OR sc.sharedWithUserId = ?1")
    List<RetrackerList> findRetrackerListsForUser(UUID userId);

}
