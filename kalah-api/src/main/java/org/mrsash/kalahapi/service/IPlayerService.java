package org.mrsash.kalahapi.service;

import org.mrsash.kalahapi.dto.LoginDto;
import org.mrsash.kalahapi.dto.PlayerCreateDto;
import org.mrsash.kalahapi.dto.PlayerDto;

public interface IPlayerService {

    LoginDto login(String playerId);

    PlayerDto get(String playerId);

    void create(PlayerCreateDto playerCreateDto);
}
