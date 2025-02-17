package de.lbakker77.retracker.main.user.entity;

import de.lbakker77.retracker.main.user.entity.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository  extends CrudRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    List<User> findByIdIn(Collection<UUID> ids);
}
