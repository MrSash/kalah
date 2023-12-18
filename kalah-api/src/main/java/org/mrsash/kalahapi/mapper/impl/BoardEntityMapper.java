package org.mrsash.kalahapi.mapper.impl;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.mrsash.kalahapi.dto.BoardDto;
import org.mrsash.kalahapi.mapper.IBoardEntityMapper;
import org.springframework.stereotype.Component;

@Component
public class BoardEntityMapper implements IBoardEntityMapper {

    @Override
    public BoardDto toDto(int[] entity) {
        var boardSize = entity.length;
        var player1BigPitPosition = boardSize / 2 - 1;
        var player2BigPitPosition = boardSize - 1;
        return new BoardDto(
                copyArray(entity),
                IntStream.range(0, player1BigPitPosition)
                        .boxed()
                        .collect(Collectors.toMap(it -> it, it -> entity[it])),
                IntStream.range(player1BigPitPosition + 1, player2BigPitPosition)
                        .boxed()
                        .collect(Collectors.toMap(it -> it, it -> entity[it])),
                new BoardDto.BigPitDto(player1BigPitPosition, entity[player1BigPitPosition]),
                new BoardDto.BigPitDto(player2BigPitPosition, entity[player2BigPitPosition])
        );
    }

    @Override
    public int[] toEntity(BoardDto dto) {
        return copyArray(dto.getPits());
    }

    private int[] copyArray(int[] source) {
        return Arrays.copyOf(source, source.length);
    }
}
