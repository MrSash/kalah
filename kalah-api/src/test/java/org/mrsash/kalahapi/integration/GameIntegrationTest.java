package org.mrsash.kalahapi.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mrsash.kalahapi.model.TurnType;
import org.springframework.http.HttpStatus;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

public class GameIntegrationTest extends AIntegrationTest {

    @Test
    public void successfullyGetGame() {
        // Given
        var playerId = restSender.createPlayer(RandomStringUtils.randomAlphabetic(20)).getId();
        var gameId = restSender.createGame(playerId.toString()).getId();

        // When
        var game = restSender.getGame(gameId);

        //Then
        assertThat(game)
                .isNotNull()
                .matches(it -> it.getId().equals(gameId));
    }

    @Test
    public void throwExceptionIfTryToGetNotExistsPlayer() {
        // When / Then
        restSender.getGameException(UUID.randomUUID(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void successfullyCreateAGame() {
        // Given
        var playerId = restSender.createPlayer(RandomStringUtils.randomAlphabetic(20)).getId();

        // When / Then
        assertThat(restSender.createGame(playerId.toString()))
                .isNotNull()
                .matches(it -> it.getId() != null)
                .matches(it -> it.getOwnerId().equals(playerId))
                .matches(it -> it.getTurn().equals(TurnType.PLAYER_1))
                .matches(it -> Arrays.equals(
                        it.getBoard().getPits(),
                        new int[]{6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0}
                ))
                .matches(it -> it.getBoard().getPlayer1BigPit().getPosition() == 6)
                .matches(it -> it.getBoard().getPlayer2BigPit().getPosition() == 13)
                .matches(it -> it.getBoard().getPlayer1Pits().equals(Map.of(
                        0, 6,
                        1, 6,
                        2, 6,
                        3, 6,
                        4, 6,
                        5, 6
                )))
                .matches(it -> it.getBoard().getPlayer2Pits().equals(Map.of(
                        7, 6,
                        8, 6,
                        9, 6,
                        10, 6,
                        11, 6,
                        12, 6
                )))
                .matches(it -> it.getBoard().getPlayer1BigPit().getValue() == 0)
                .matches(it -> it.getBoard().getPlayer2BigPit().getValue() == 0);
    }

    @Test
    public void throwExceptionIfTryToCreateGameWithNullOwnerId() {
        // When / Then
        restSender.createGameException(null, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void successfullyGetAllPlayerGames() {
        // Given
        var playerId = restSender.createPlayer(RandomStringUtils.randomAlphabetic(20)).getId();
        var game1 = restSender.createGame(playerId.toString());
        var game2 = restSender.createGame(playerId.toString());
        var game3 = restSender.createGame(playerId.toString());

        // When
        var games = restSender.getAllPlayerGames(playerId);

        //Then
        assertThat(games)
                .hasSize(3)
                .containsAll(List.of(game1, game2, game3));
    }

    @Test
    public void successfullyGetAllPlayerGamesIfPlayerDidNotCreateGames() {
        // Given
        var playerId = restSender.createPlayer(RandomStringUtils.randomAlphabetic(20)).getId();

        // When
        var games = restSender.getAllPlayerGames(playerId);

        //Then
        assertThat(games).hasSize(0);
    }

    @Test
    public void successfullyGetAllPlayerGamesIfPlayerDoesNotExists() {
        // When / Then
        assertThat(restSender.getAllPlayerGames(UUID.randomUUID())).hasSize(0);
    }

    @Test
    public void successfullyMove() {
        // Given
        var playerId = restSender.createPlayer(RandomStringUtils.randomAlphabetic(20)).getId();
        var gameId = restSender.createGame(playerId.toString()).getId();

        // When
        restSender.move(gameId, 0, TurnType.PLAYER_1);
        restSender.move(gameId, 1, TurnType.PLAYER_1);
        restSender.move(gameId, 9, TurnType.PLAYER_2);
        restSender.move(gameId, 5, TurnType.PLAYER_1);
        var lastState = restSender.move(gameId, 10, TurnType.PLAYER_2);

        // Then
        assertFalse(lastState.isFinished());
        assertThat(lastState.getGame().getBoard().getPits())
                .isEqualTo(new int[]{3, 2, 9, 9, 9, 0, 3, 8, 8, 1, 0, 9, 9, 2});
    }

    @Test
    public void throwExceptionIfTryToMoveWithNotYourTurn() {
        // Given
        var playerId = restSender.createPlayer(RandomStringUtils.randomAlphabetic(20)).getId();
        var gameId = restSender.createGame(playerId.toString()).getId();
        restSender.move(gameId, 0, TurnType.PLAYER_1);
        restSender.move(gameId, 1, TurnType.PLAYER_1);

        // When / Then
        restSender.moveException(gameId, 2, TurnType.PLAYER_1, HttpStatus.BAD_REQUEST);
    }
}
