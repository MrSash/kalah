package org.mrsash.kalah.mapper;

import org.mrsash.kalah.dto.GameDto;
import org.mrsash.kalah.model.GameEntity;

public interface IGameEntityMapper {

    GameDto toDto(GameEntity entity);

    GameEntity toEntity(GameDto owner);
}
