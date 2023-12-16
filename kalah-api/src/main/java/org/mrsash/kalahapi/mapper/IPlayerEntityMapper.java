package org.mrsash.kalahapi.mapper;

import org.mrsash.kalahapi.dto.LoginDto;
import org.mrsash.kalahapi.dto.PlayerCreateDto;
import org.mrsash.kalahapi.dto.PlayerDto;
import org.mrsash.kalahapi.model.PlayerEntity;

public interface IPlayerEntityMapper {

    PlayerDto toDto(PlayerEntity entity);

    LoginDto toLoginDto(PlayerEntity entity);

    PlayerEntity toEntity(PlayerCreateDto dto);
}
