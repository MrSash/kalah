package org.mrsash.kalahapi.mapper;

import org.mrsash.kalahapi.dto.GameDto;
import org.mrsash.kalahapi.model.GameEntity;

public interface IGameEntityMapper {

    GameDto toDto(GameEntity entity);

    GameEntity toEntity(GameDto owner);
}
