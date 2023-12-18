package org.mrsash.kalah.unit;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mrsash.kalah.dto.LoginDto;
import org.mrsash.kalah.dto.PlayerCreateDto;
import org.mrsash.kalah.dto.PlayerDto;
import org.mrsash.kalah.exception.PlayerNotFoundByNameException;
import org.mrsash.kalah.exception.PlayerNotFoundException;
import org.mrsash.kalah.mapper.IPlayerEntityMapper;
import org.mrsash.kalah.mapper.impl.PlayerEntityMapper;
import org.mrsash.kalah.model.PlayerEntity;
import org.mrsash.kalah.repository.IPlayerRepository;
import org.mrsash.kalah.service.impl.PlayerService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;

public class PlayerServiceUnitTest extends AUnitTest {

    @TestConfiguration
    public static class Config {

        @Bean
        public IPlayerEntityMapper playerEntityMapper() {
            return new PlayerEntityMapper();
        }
    }

    @MockBean
    private IPlayerRepository playerRepository;

    @SpyBean
    private PlayerService playerService;

    @Test
    public void successfullyLogin() {
        //Given
        var name = randomAlphabetic(20);
        var expected = new LoginDto(UUID.randomUUID());
        when(playerRepository.getByName(name))
                .thenReturn(Optional.of(new PlayerEntity(expected.getPlayerId(), name)));

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
        when(playerRepository.findById(expected.getId()))
                .thenReturn(Optional.of(new PlayerEntity(expected.getId(), expected.getName())));

        // When
        var actual = playerService.get(expected.getId().toString());

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
        var newPlayer = new PlayerEntity(UUID.randomUUID(), randomAlphabetic(20));
        when(playerRepository.save(argThat(it -> it.getName().equals(newPlayer.getName())))).thenReturn(newPlayer);

        // When / Then
        assertDoesNotThrow(() -> playerService.create(new PlayerCreateDto(newPlayer.getName())));
    }
}
