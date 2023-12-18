package org.mrsash.kalahapi.mapper.impl;

import org.mrsash.kalahapi.dto.PlayerDto;
import org.mrsash.kalahapi.mapper.IPlayerEntityMapper;
import org.mrsash.kalahapi.model.PlayerEntity;
import org.springframework.stereotype.Component;

@Component
public class PlayerEntityMapper implements IPlayerEntityMapper {

    @Override
    public PlayerDto toDto(PlayerEntity entity) {
        return new PlayerDto(entity.getId(), entity.getName());
    }

    @Override
    public PlayerEntity toEntity(PlayerDto dto) {
        return new PlayerEntity(dto.getId(), dto.getName());
    }
}
