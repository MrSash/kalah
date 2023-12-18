package org.mrsash.kalahapi.repository;

import java.util.List;
import java.util.UUID;
import org.mrsash.kalahapi.model.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGameRepository extends JpaRepository<GameEntity, UUID> {

    List<GameEntity> findAllByOwnerId(UUID ownedId);
}
