package org.mrsash.kalahapi.repository;

import java.util.Optional;
import org.mrsash.kalahapi.model.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPlayerRepository extends JpaRepository<PlayerEntity, String> {

    Optional<PlayerEntity> getByName(String name);
}
