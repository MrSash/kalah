package org.mrsash.kalahapi.mapper.impl;

import java.util.UUID;
import org.mrsash.kalahapi.dto.LoginDto;
import org.mrsash.kalahapi.dto.PlayerCreateDto;
import org.mrsash.kalahapi.dto.PlayerDto;
import org.mrsash.kalahapi.mapper.IPlayerEntityMapper;
import org.mrsash.kalahapi.model.PlayerEntity;
import org.springframework.stereotype.Component;

@Component
public class PlayerEntityMapper implements IPlayerEntityMapper {

    @Override
    public PlayerDto toDto(PlayerEntity entity) {
        return new PlayerDto(UUID.fromString(entity.getId()), entity.getName());
    }

    @Override
    public LoginDto toLoginDto(PlayerEntity entity) {
        return new LoginDto(UUID.fromString(entity.getId()));
    }

    @Override
    public PlayerEntity toEntity(PlayerCreateDto dto) {
        return new PlayerEntity(UUID.randomUUID().toString(), dto.getName());
    }
}
