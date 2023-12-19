package org.mrsash.kalah.unit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mrsash.kalah.dto.BoardDto;
import org.mrsash.kalah.dto.BoardResultDto;
import org.mrsash.kalah.dto.GameCreateDto;
import org.mrsash.kalah.dto.GameDto;
import org.mrsash.kalah.dto.GameMoveDto;
import org.mrsash.kalah.dto.PlayerDto;
import org.mrsash.kalah.exception.GameIncorrectTurnException;
import org.mrsash.kalah.exception.GameNotFoundException;
import org.mrsash.kalah.mapper.IBoardEntityMapper;
import org.mrsash.kalah.mapper.IGameEntityMapper;
import org.mrsash.kalah.mapper.impl.BoardEntityMapper;
import org.mrsash.kalah.mapper.impl.GameEntityMapper;
import org.mrsash.kalah.model.GameEntity;
import org.mrsash.kalah.model.TurnType;
import org.mrsash.kalah.repository.IGameRepository;
import org.mrsash.kalah.service.IBoardService;
import org.mrsash.kalah.service.IPlayerService;
import org.mrsash.kalah.service.impl.GameService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

public class GameServiceUnitTest extends AUnitTest {

    @TestConfiguration
    public static class Config {

        @Bean
        public IBoardEntityMapper boardEntityMapper() {
            return new BoardEntityMapper();
        }

        @Bean
        public IGameEntityMapper gameEntityMapper(IBoardEntityMapper boardEntityMapper) {
            return new GameEntityMapper(boardEntityMapper);
        }
    }

    @MockBean
    private IGameRepository gameRepository;

    @MockBean
    private IBoardService boardService;

    @MockBean
    private IPlayerService playerService;

    @SpyBean
    private GameService gameService;

    @Test
    public void successfullyGetGame() {
        //Given
        var playerId = createPlayer().getId();
        var pits = getDefaultPits();
        var expected = new GameDto(
                UUID.randomUUID(),
                playerId,
                TurnType.PLAYER_1,
                new BoardDto(
                        pits,
                        Map.of(
                                0, pits[0],
                                1, pits[1],
                                2, pits[2],
                                3, pits[3],
                                4, pits[4],
                                5, pits[5]
                        ),
                        Map.of(
                                7, pits[7],
                                8, pits[8],
                                9, pits[9],
                                10, pits[10],
                                11, pits[11],
                                12, pits[12]
                        ),
                        new BoardDto.BigPitDto(6, pits[6]),
                        new BoardDto.BigPitDto(13, pits[13])

                )
        );
        when(gameRepository.findById(expected.getId()))
                .thenReturn(Optional.of(new GameEntity(
                        expected.getId(),
                        playerId,
                        expected.getTurn(),
                        expected.getBoard().getPits()
                )));

        // When
        var actual = gameService.get(expected.getId().toString());

        // Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void throwExceptionIfGameNotFoundById() {
        // Given
        var gameId = UUID.randomUUID().toString();

        // When / Then
        assertThrows(GameNotFoundException.class, () -> gameService.get(gameId));
        verify(gameRepository, times(0)).save(any());
        verify(gameRepository, times(0)).deleteById(any());
    }

    @Test
    public void successfullyCreateGame() {
        //Given
        var player = createPlayer();
        var newGame = createGameEntity();
        newGame.setOwnerId(player.getId());
        when(gameRepository.save(any())).thenReturn(newGame);
        when(playerService.get(any())).thenReturn(player);

        // When / Then
        assertDoesNotThrow(() -> gameService.create(new GameCreateDto(player.getId().toString())));
        verify(gameRepository).save(any());
        verify(gameRepository, times(0)).deleteById(any());
    }

    @Test
    public void throwExceptionIfItIsNotPlayer1Turn() {
        // Given
        var game = createGameEntity();
        game.setTurn(TurnType.PLAYER_2);
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        // When / Then
        assertThrows(
                GameIncorrectTurnException.class,
                () -> gameService.move(game.getId().toString(), new GameMoveDto(0, TurnType.PLAYER_1))
        );
        verify(gameRepository, times(0)).save(any());
        verify(gameRepository, times(0)).deleteById(any());
    }

    @Test
    public void throwExceptionIfItIsNotPlayer2Turn() {
        // Given
        var game = createGameEntity();
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        // When / Then
        assertThrows(
                GameIncorrectTurnException.class,
                () -> gameService.move(game.getId().toString(), new GameMoveDto(0, TurnType.PLAYER_2))
        );
        verify(gameRepository, times(0)).save(any());
        verify(gameRepository, times(0)).deleteById(any());
    }

    @Test
    public void successfullyMove() {
        // Given
        var game = createGameEntity();
        var position = 2;
        var boardResult = new BoardResultDto(
                false,
                TurnType.PLAYER_2,
                BoardDto.builder()
                        .pits(game.getBoard())
                        .build()
        );
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));
        when(boardService.calculate(eq(position), argThat(it -> it.getId().equals(game.getId()))))
                .thenReturn(boardResult);

        // When
        var result = gameService.move(game.getId().toString(), new GameMoveDto(position, TurnType.PLAYER_1));

        // Then
        assertFalse(result.isFinished());
        assertThat(result.getGame().getTurn()).isEqualTo(boardResult.getNextTurn());
        assertThat(result.getGame().getBoard()).isEqualTo(boardResult.getBoard());
        verify(gameRepository).save(any());
        verify(gameRepository, times(0)).deleteById(any());
    }

    @Test
    public void successfullyMoveAndFinish() {
        // Given
        var game = createGameEntity();
        var position = 5;
        var boardResult = new BoardResultDto(
                true,
                TurnType.PLAYER_1,
                BoardDto.builder()
                        .pits(game.getBoard())
                        .build()
        );
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));
        when(boardService.calculate(eq(position), argThat(it -> it.getId().equals(game.getId()))))
                .thenReturn(boardResult);

        // When
        var result = gameService.move(game.getId().toString(), new GameMoveDto(position, TurnType.PLAYER_1));

        // Then
        assertTrue(result.isFinished());
        assertThat(result.getGame().getTurn()).isEqualTo(boardResult.getNextTurn());
        assertThat(result.getGame().getBoard()).isEqualTo(boardResult.getBoard());
        verify(gameRepository, times(0)).save(any());
        verify(gameRepository).deleteById(any());
    }

    private PlayerDto createPlayer() {
        return new PlayerDto(UUID.randomUUID(), RandomStringUtils.randomAlphabetic(20));
    }

    private GameEntity createGameEntity() {
        return new GameEntity(
                UUID.randomUUID(),
                createPlayer().getId(),
                TurnType.PLAYER_1,
                getDefaultPits()
        );
    }

    private int[] getDefaultPits() {
        return new int[]{6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0};
    }
}
