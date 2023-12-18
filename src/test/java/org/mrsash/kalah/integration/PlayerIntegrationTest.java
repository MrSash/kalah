package org.mrsash.kalah.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.http.HttpStatus;

public class PlayerIntegrationTest extends AIntegrationTest {

    @Test
    public void successfullyLogin() {
        // Given
        var name = RandomStringUtils.randomAlphabetic(20);
        var playerId = restSender.createPlayer(name).getId();

        // When
        var login = restSender.login(name);

        //Then
        assertThat(login)
                .isNotNull()
                .matches(it -> it.getPlayerId() != null)
                .matches(it -> it.getPlayerId().equals(playerId));
    }

    @Test
    public void throwExceptionIfTryToCreatePlayerWithTooShortName() {
        // When / Then
        restSender.createPlayerException(RandomStringUtils.randomAlphabetic(2), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void throwExceptionIfTryToCreatePlayerWithNullName() {
        // When / Then
        restSender.createPlayerException(null, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void throwExceptionIfTryToCreatePlayerWithExistsName() {
        // Given
        var name = RandomStringUtils.randomAlphabetic(20);
        restSender.createPlayer(name);

        // When / Then
        restSender.createPlayerException(name, HttpStatus.CONFLICT);
    }

    @Test
    public void throwExceptionIfTryToLoginWithNotExistsPlayer() {
        // When / Then
        restSender.loginException(RandomStringUtils.randomAlphabetic(20), HttpStatus.NOT_FOUND);
    }

    @Test
    public void throwExceptionIfTryToLoginWithTooShortName() {
        // When / Then
        restSender.loginException(RandomStringUtils.randomAlphabetic(2), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void throwExceptionIfTryToLoginWithNullName() {
        // When / Then
        restSender.loginException(null, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void successfullyGetPlayer() {
        // Given
        var name = RandomStringUtils.randomAlphabetic(20);
        restSender.createPlayer(name);
        var playerId = restSender.login(name).getPlayerId();

        // When
        var player = restSender.getPlayer(playerId);

        //Then
        assertThat(player)
                .isNotNull()
                .matches(it -> it.getId() != null)
                .matches(it -> StringUtils.isNotBlank(it.getName()));
    }

    @Test
    public void throwExceptionIfTryToGetNotExistsPlayer() {
        // When / Then
        restSender.getPlayerException(UUID.randomUUID(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void successfullyCreatePlayer() {
        // Given
        var name = RandomStringUtils.randomAlphabetic(20);

        // When
        var player = restSender.createPlayer(name);

        //Then
        assertThat(player)
                .isNotNull()
                .matches(it -> it.getId() != null)
                .matches(it -> it.getName().equals(player.getName()));
    }
}
