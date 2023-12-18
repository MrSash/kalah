package org.mrsash.kalahapi.mapper.impl;

import org.mrsash.kalahapi.dto.GameDto;
import org.mrsash.kalahapi.mapper.IBoardEntityMapper;
import org.mrsash.kalahapi.mapper.IGameEntityMapper;
import org.mrsash.kalahapi.model.GameEntity;
import org.springframework.stereotype.Component;

@Component
public class GameEntityMapper implements IGameEntityMapper {

    private final IBoardEntityMapper boardEntityMapper;

    public GameEntityMapper(IBoardEntityMapper boardEntityMapper) {
        this.boardEntityMapper = boardEntityMapper;
    }

    @Override
    public GameDto toDto(GameEntity entity) {
        return new GameDto(
                entity.getId(),
                entity.getOwnerId(),
                entity.getTurn(),
                boardEntityMapper.toDto(entity.getBoard())
        );
    }

    @Override
    public GameEntity toEntity(GameDto dto) {
        return new GameEntity(
                dto.getId(),
                dto.getOwnerId(),
                dto.getTurn(),
                boardEntityMapper.toEntity(dto.getBoard())
        );
    }
}
