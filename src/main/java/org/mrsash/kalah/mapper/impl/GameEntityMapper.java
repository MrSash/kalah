package org.mrsash.kalah.mapper.impl;

import org.mrsash.kalah.dto.GameDto;
import org.mrsash.kalah.mapper.IBoardEntityMapper;
import org.mrsash.kalah.mapper.IGameEntityMapper;
import org.mrsash.kalah.model.GameEntity;
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
