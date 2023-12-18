package org.mrsash.kalah.repository;

import java.util.Optional;
import java.util.UUID;
import org.mrsash.kalah.model.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPlayerRepository extends JpaRepository<PlayerEntity, UUID> {

    Optional<PlayerEntity> getByName(String name);

    boolean existsByName(String name);
}
