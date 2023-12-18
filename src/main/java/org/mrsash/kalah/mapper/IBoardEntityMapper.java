package org.mrsash.kalah.mapper;

import org.mrsash.kalah.dto.BoardDto;

public interface IBoardEntityMapper {

    BoardDto toDto(int[] entity);

    int[] toEntity(BoardDto dto);
}