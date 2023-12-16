package org.mrsash.kalahapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.mrsash.kalahapi.dto.LoginDto;
import org.mrsash.kalahapi.dto.PlayerCreateDto;
import org.mrsash.kalahapi.dto.PlayerDto;
import org.mrsash.kalahapi.exception.PlayerNotFoundByNameException;
import org.mrsash.kalahapi.exception.PlayerNotFoundException;
import org.mrsash.kalahapi.mapper.IPlayerEntityMapper;
import org.mrsash.kalahapi.repository.IPlayerRepository;
import org.mrsash.kalahapi.service.IPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PlayerService implements IPlayerService {

    private final IPlayerRepository playerRepository;
    private final IPlayerEntityMapper playerEntityMapper;

    @Autowired
    public PlayerService(IPlayerRepository playerRepository, IPlayerEntityMapper playerEntityMapper) {
        this.playerRepository = playerRepository;
        this.playerEntityMapper = playerEntityMapper;
    }

    @Override
    public LoginDto login(String name) {
        log.debug("Try to login player with name '{}'", name);
        var result = playerEntityMapper.toLoginDto(playerRepository.getByName(name)
                .orElseThrow(() -> new PlayerNotFoundByNameException(name)));
        log.debug("Successfully login player with name '{}'", name);
        return result;
    }

    @Override
    public PlayerDto get(String playerId) {
        log.debug("Try to get player with id '{}'", playerId);
        var result = playerEntityMapper.toDto(playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(playerId)));
        log.debug("Successfully get player with id '{}'", playerId);
        return result;
    }

    @Transactional
    @Override
    public void create(PlayerCreateDto playerCreateDto) {
        log.debug("Try to create player with next data: '{}'", playerCreateDto.getName());
        var playerId = playerRepository.save(playerEntityMapper.toEntity(playerCreateDto)).getId();
        log.debug("Successfully create player with id '{}'", playerId);
    }
}
