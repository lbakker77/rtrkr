package de.lbakker77.retracker.main.retracker.entity;

import de.lbakker77.retracker.main.retracker.entity.model.RetrackerEntry;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface RetrackerEntryRepository extends CrudRepository<RetrackerEntry, UUID> {
    List<RetrackerEntry> findByRetrackerListId(UUID listId);


    long countByRetrackerListIdAndDueDateLessThanEqual(UUID listId, ZonedDateTime date);
}

