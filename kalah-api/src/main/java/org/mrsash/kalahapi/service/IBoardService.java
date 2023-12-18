package org.mrsash.kalahapi.service;

import org.mrsash.kalahapi.dto.BoardResultDto;
import org.mrsash.kalahapi.dto.GameDto;

public interface IBoardService {

    /**
     * Create new board
     * Within the home assignment it is just a stub but can easily expand
     *
     * @return new board as int array
     */
    int[] create();

    /**
     * Calculate a move
     *
     * @param position move on position
     * @param game     game data as {@link GameDto}
     * @return move result as {@link BoardResultDto}
     */
    BoardResultDto calculate(int position, GameDto game);
}
