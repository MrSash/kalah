package org.mrsash.kalah.service;

import org.mrsash.kalah.dto.LoginDto;
import org.mrsash.kalah.dto.PlayerCreateDto;
import org.mrsash.kalah.dto.PlayerDto;

public interface IPlayerService {

    /**
     * Login for authorization player in the system. Within home assignment works only as stub
     *
     * @param name player name
     * @return {@link LoginDto} with player ID
     */
    LoginDto login(String name);

    /**
     * Getting player state
     *
     * @param playerId player ID as {@link java.util.UUID}
     * @return player state as {@link PlayerDto}
     */
    PlayerDto get(String playerId);

    /**
     * Creating new player
     *
     * @param playerCreateDto create body as {@link PlayerCreateDto}
     * @return new player state as {@link PlayerDto}
     */
    PlayerDto create(PlayerCreateDto playerCreateDto);
}
