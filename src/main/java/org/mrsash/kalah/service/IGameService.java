package org.mrsash.kalah.service;

import java.util.List;
import org.mrsash.kalah.dto.GameCreateDto;
import org.mrsash.kalah.dto.GameDto;
import org.mrsash.kalah.dto.GameMoveDto;
import org.mrsash.kalah.dto.GameMoveResultDto;

public interface IGameService {

    /**
     * Getting game state
     *
     * @param gameId game ID as {@link java.util.UUID}
     * @return game state as {@link GameDto}
     */
    GameDto get(String gameId);

    /**
     * Get all games of player by his ID
     *
     * @param playerId player ID as {@link java.util.UUID}
     * @return list games as {@link GameDto} of player
     */
    List<GameDto> getAllByPlayer(String playerId);

    /**
     * Creating new game
     *
     * @param gameCreateDto create body as {@link GameCreateDto}
     * @return new game state as {@link GameDto}
     */
    GameDto create(GameCreateDto gameCreateDto);

    /**
     * Make a move. If the game is finished, return state and remove the game
     *
     * @param gameMoveDto move body as {@link GameMoveDto}
     * @return move resul as {@link GameMoveResultDto}
     */
    GameMoveResultDto move(GameMoveDto gameMoveDto);
}
