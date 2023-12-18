package org.mrsash.kalahapi.service.impl;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.mrsash.kalahapi.dto.LoginDto;
import org.mrsash.kalahapi.dto.PlayerCreateDto;
import org.mrsash.kalahapi.dto.PlayerDto;
import org.mrsash.kalahapi.exception.PlayerAlreadyExistsByNameException;
import org.mrsash.kalahapi.exception.PlayerNotFoundByNameException;
import org.mrsash.kalahapi.exception.PlayerNotFoundException;
import org.mrsash.kalahapi.mapper.IPlayerEntityMapper;
import org.mrsash.kalahapi.model.PlayerEntity;
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
        var result = playerEntityMapper.toDto(playerRepository.getByName(name)
                .orElseThrow(() -> new PlayerNotFoundByNameException(name)));
        log.debug("Successfully logger in player with name '{}'", name);
        return new LoginDto(result.getId());
    }

    @Override
    public PlayerDto get(String playerId) {
        log.debug("Try to get player with id '{}'", playerId);
        var playerIdAsUuid = UUID.fromString(playerId);
        var result = playerEntityMapper.toDto(playerRepository.findById(playerIdAsUuid)
                .orElseThrow(() -> new PlayerNotFoundException(playerIdAsUuid)));
        log.debug("Successfully got player with id '{}'", playerId);
        return result;
    }

    @Transactional
    @Override
    public PlayerDto create(PlayerCreateDto playerCreateDto) {
        var name = playerCreateDto.getName();
        log.debug("Try to create player with next data: '{}'", name);
        if (playerRepository.existsByName(name)) {
            throw new PlayerAlreadyExistsByNameException(name);
        }
        var result = playerRepository.save(PlayerEntity.builder()
                .name(name)
                .build()
        );
        log.debug("Successfully created player with next data: '{}'", result);
        return playerEntityMapper.toDto(result);
    }
}
