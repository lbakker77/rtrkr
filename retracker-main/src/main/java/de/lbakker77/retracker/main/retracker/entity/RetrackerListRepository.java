package de.lbakker77.retracker.main.retracker.entity;

import de.lbakker77.retracker.main.retracker.entity.model.RetrackerList;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface RetrackerListRepository  extends CrudRepository<RetrackerList, UUID> {
}
