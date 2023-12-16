package org.mrsash.kalahapi.unit;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mrsash.kalahapi.dto.LoginDto;
import org.mrsash.kalahapi.dto.PlayerCreateDto;
import org.mrsash.kalahapi.dto.PlayerDto;
import org.mrsash.kalahapi.exception.PlayerNotFoundByNameException;
import org.mrsash.kalahapi.exception.PlayerNotFoundException;
import org.mrsash.kalahapi.mapper.impl.PlayerEntityMapper;
import org.mrsash.kalahapi.model.PlayerEntity;
import org.mrsash.kalahapi.repository.IPlayerRepository;
import org.mrsash.kalahapi.service.IPlayerService;
import org.mrsash.kalahapi.service.impl.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {PlayerService.class, PlayerEntityMapper.class})
public class PlayerServiceUnitTest extends AUnitTest {

    @MockBean
    private IPlayerRepository playerRepository;

    @Autowired
    private IPlayerService playerService;

    @Test
    public void successfullyLogin() {
        //Given
        var name = randomAlphabetic(20);
        var expected = new LoginDto(UUID.randomUUID());
        when(playerRepository.getByName(name))
                .thenReturn(Optional.of(new PlayerEntity(expected.getPlayerId().toString(), name)));

        // When
        var actual = playerService.login(name);

        // Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void throwExceptionIfPlayerNotFoundByName() {
        // Given
        var name = randomAlphabetic(20);

        // When / Then
        assertThrows(PlayerNotFoundByNameException.class, () -> playerService.login(name));
    }

    @Test
    public void successfullyGetPlayer() {
        //Given
        var expected = new PlayerDto(UUID.randomUUID(), randomAlphabetic(20));
        when(playerRepository.findById(expected.getPlayerId().toString()))
                .thenReturn(Optional.of(new PlayerEntity(expected.getPlayerId().toString(), expected.getName())));

        // When
        var actual = playerService.get(expected.getPlayerId().toString());

        // Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void throwExceptionIfPlayerNotFoundById() {
        // Given
        var playerId = UUID.randomUUID().toString();

        // When / Then
        assertThrows(PlayerNotFoundException.class, () -> playerService.get(playerId));
    }

    @Test
    public void successfullyCreatePlayer() {
        //Given
        var newPlayer = new PlayerEntity(UUID.randomUUID().toString(), randomAlphabetic(20));
        when(playerRepository.save(argThat(it -> it.getName().equals(newPlayer.getName())))).thenReturn(newPlayer);

        // When / Then
        assertDoesNotThrow(() -> playerService.create(new PlayerCreateDto(newPlayer.getName())));
    }
}
