package de.lbakker77.retracker.core.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface RetrackerListRepository  extends CrudRepository<RetrackerList, UUID> {

    @Query("SELECT list FROM RetrackerList list LEFT JOIN list.shareConfigEntries sc WHERE list.ownerId = ?1 OR (sc.sharedWithUserId = ?1 and sc.status = de.lbakker77.retracker.core.domain.ShareStatus.ACCEPTED) order by list.defaultList asc,list.shared asc, list.name asc ")
    List<RetrackerList> findRetrackerListsForUser(UUID userId);

    @Query("SELECT list FROM RetrackerList list LEFT JOIN list.shareConfigEntries sc WHERE sc.sharedWithUserId = ?1 and sc.status = de.lbakker77.retracker.core.domain.ShareStatus.PENDING order by list.name asc ")
    List<RetrackerList> findRetrackerInvitationsForUser(UUID userId);

    List<RetrackerList> findByOwnerId(UUID ownerId);

    List<RetrackerList> findByShareConfigEntriesSharedWithUserId(UUID userId);
}
