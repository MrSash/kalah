package org.mrsash.kalahapi.mapper;

import org.mrsash.kalahapi.dto.BoardDto;

public interface IBoardEntityMapper {

    BoardDto toDto(int[] entity);

    int[] toEntity(BoardDto dto);
}