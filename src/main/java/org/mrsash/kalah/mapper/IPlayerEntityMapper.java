package org.mrsash.kalah.mapper;

import org.mrsash.kalah.dto.PlayerDto;
import org.mrsash.kalah.model.PlayerEntity;

public interface IPlayerEntityMapper {

    PlayerDto toDto(PlayerEntity entity);

    PlayerEntity toEntity(PlayerDto dto);
}
