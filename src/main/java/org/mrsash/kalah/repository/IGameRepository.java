package org.mrsash.kalah.repository;

import java.util.List;
import java.util.UUID;
import org.mrsash.kalah.model.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGameRepository extends JpaRepository<GameEntity, UUID> {

    List<GameEntity> findAllByOwnerId(UUID ownedId);
}
