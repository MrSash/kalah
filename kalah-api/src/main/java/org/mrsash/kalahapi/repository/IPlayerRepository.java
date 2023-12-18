package org.mrsash.kalahapi.repository;

import java.util.Optional;
import java.util.UUID;
import org.mrsash.kalahapi.model.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPlayerRepository extends JpaRepository<PlayerEntity, UUID> {

    Optional<PlayerEntity> getByName(String name);

    boolean existsByName(String name);
}
