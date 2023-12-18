package org.mrsash.kalahapi.mapper;

import org.mrsash.kalahapi.dto.PlayerDto;
import org.mrsash.kalahapi.model.PlayerEntity;

public interface IPlayerEntityMapper {

    PlayerDto toDto(PlayerEntity entity);

    PlayerEntity toEntity(PlayerDto dto);
}
