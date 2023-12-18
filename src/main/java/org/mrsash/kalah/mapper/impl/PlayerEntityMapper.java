package org.mrsash.kalah.mapper.impl;

import org.mrsash.kalah.dto.PlayerDto;
import org.mrsash.kalah.mapper.IPlayerEntityMapper;
import org.mrsash.kalah.model.PlayerEntity;
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
