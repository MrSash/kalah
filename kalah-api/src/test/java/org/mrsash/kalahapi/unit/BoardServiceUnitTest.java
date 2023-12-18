package org.mrsash.kalahapi.unit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.mrsash.kalahapi.dto.BoardDto;
import org.mrsash.kalahapi.dto.GameDto;
import org.mrsash.kalahapi.exception.GameIncorrectPositionException;
import org.mrsash.kalahapi.mapper.impl.BoardEntityMapper;
import org.mrsash.kalahapi.model.TurnType;
import org.mrsash.kalahapi.service.impl.BoardService;
import org.springframework.boot.test.mock.mockito.SpyBean;

public class BoardServiceUnitTest extends AUnitTest {

    @SpyBean
    private BoardEntityMapper boardEntityMapper;

    @SpyBean
    private BoardService boardService;

    @Test
    public void throwExceptionIfPlayer1ChoosePlayer1BigPitPosition() {
        // When / Then
        assertThrows(
                GameIncorrectPositionException.class,
                () -> boardService.calculate(6, createGame())
        );
    }

    @Test
    public void throwExceptionIfPlayer1ChoosePlayer2BigPitPosition() {
        // When / Then
        assertThrows(
                GameIncorrectPositionException.class,
                () -> boardService.calculate(13, createGame())
        );
    }

    @Test
    public void throwExceptionIfPlayer2ChoosePlayer1BigPitPosition() {
        // Given
        var game = createGame();
        game.setTurn(TurnType.PLAYER_2);

        // When / Then
        assertThrows(
                GameIncorrectPositionException.class,
                () -> boardService.calculate(6, game)
        );
    }

    @Test
    public void throwExceptionIfPlayer2ChoosePlayer2BigPitPosition() {
        // Given
        var game = createGame();
        game.setTurn(TurnType.PLAYER_2);

        // When / Then
        assertThrows(
                GameIncorrectPositionException.class,
                () -> boardService.calculate(13, game)
        );
    }

    @Test
    public void checkPlayer1Positions() {
        // Given
        var game = createGame();

        // When / Then
        IntStream.range(0, 6).forEach(it -> assertDoesNotThrow(() -> boardService.calculate(it, game)));
        assertThrows(GameIncorrectPositionException.class, () -> boardService.calculate(-1, game));
        IntStream.range(6, 15).forEach(it -> assertThrows(
                GameIncorrectPositionException.class,
                () -> boardService.calculate(it, game)
        ));
    }

    @Test
    public void checkPlayer2Positions() {
        // Given
        var game = createGame();
        game.setTurn(TurnType.PLAYER_2);

        // When / Then
        IntStream.range(-1, 7).forEach(it -> assertThrows(
                GameIncorrectPositionException.class,
                () -> boardService.calculate(it, game)
        ));
        IntStream.range(7, 13).forEach(it -> assertDoesNotThrow(() -> boardService.calculate(it, game)));
        IntStream.range(13, 14).forEach(it -> assertThrows(
                GameIncorrectPositionException.class,
                () -> boardService.calculate(it, game)
        ));
    }

    @Test
    public void throwExceptionIfPlayerChoosePitWithZero() {
        // Given
        var game = createGame();
        game.getBoard().getPits()[0] = 0;

        // When / Then
        assertThrows(
                GameIncorrectPositionException.class,
                () -> boardService.calculate(0, game)
        );
    }

    @Test
    public void successfullyMove() {
        // Given
        var game = createGame();

        // When
        var result = boardService.calculate(2, game);

        // Then
        assertFalse(result.isFinished());
        assertGameBoard(result.getBoard(), new int[]{6, 6, 0, 7, 7, 7, 1, 7, 7, 6, 6, 6, 6, 0});
    }

    @Test
    public void successfullySkipOppositeBigPitByPlayer1() {
        // Given
        var game = createGame();
        game.getBoard().setPits(new int[]{3, 1, 11, 10, 10, 9, 2, 2, 9, 2, 0, 9, 0, 4});

        // When
        var result = boardService.calculate(4, game);

        // Then
        assertFalse(result.isFinished());
        assertGameBoard(result.getBoard(), new int[]{4, 2, 11, 10, 0, 10, 3, 3, 10, 3, 1, 10, 1, 4});
    }

    @Test
    public void successfullySkipOppositeBigPitByPlayer2() {
        // Given
        var game = createGame();
        game.setTurn(TurnType.PLAYER_2);
        game.getBoard().setPits(new int[]{4, 2, 11, 10, 0, 10, 3, 3, 10, 3, 1, 10, 1, 4});

        // When
        var result = boardService.calculate(11, game);

        // Then
        assertFalse(result.isFinished());
        assertGameBoard(result.getBoard(), new int[]{5, 3, 12, 11, 1, 11, 3, 4, 11, 3, 1, 0, 2, 5});
    }

    @Test
    public void successfullyMoveWithCaptureByPlayer1() {
        // Given
        var game = createGame();
        game.getBoard().setPits(new int[]{4, 11, 3, 1, 0, 2, 5, 5, 3, 12, 11, 0, 12, 3});

        // When
        var result = boardService.calculate(3, game);

        // Then
        assertFalse(result.isFinished());
        assertGameBoard(result.getBoard(), new int[]{4, 11, 3, 0, 0, 2, 9, 5, 0, 12, 11, 0, 12, 3});
    }

    @Test
    public void successfullyMoveWithCaptureByPlayer2() {
        // Given
        var game = createGame();
        game.setTurn(TurnType.PLAYER_2);
        game.getBoard().setPits(new int[]{5, 3, 12, 11, 0, 12, 3, 4, 11, 3, 1, 0, 2, 5});

        // When
        var result = boardService.calculate(10, game);

        // Then
        assertFalse(result.isFinished());
        assertGameBoard(result.getBoard(), new int[]{5, 0, 12, 11, 0, 12, 3, 4, 11, 3, 0, 0, 2, 9});
    }

    @Test
    public void successfullyMoveInOwnBigPitAndHaveOneMoreTurn() {
        // Given
        var game = createGame();
        game.getBoard().setPits(new int[]{4, 5, 5, 0, 0, 0, 19, 1, 0, 6, 6, 5, 7, 14});
        assertGameBoard(
                boardService.calculate(1, game).getBoard(),
                new int[]{4, 0, 6, 1, 1, 1, 20, 1, 0, 6, 6, 5, 7, 14}
        );

        // When / Then
        assertDoesNotThrow(() -> boardService.calculate(0, game));
    }

    @Test
    public void successfullyMoveAndFinish() {
        // Given
        var game = createGame();
        game.getBoard().setPits(new int[]{0, 0, 0, 0, 0, 1, 31, 0, 0, 0, 0, 0, 3, 37});

        // When
        var result = boardService.calculate(5, game);

        // Then
        assertTrue(result.isFinished());
        assertGameBoard(result.getBoard(), new int[]{0, 0, 0, 0, 0, 0, 32, 0, 0, 0, 0, 0, 0, 40});
    }

    private void assertGameBoard(BoardDto board, int[] expectedPits) {
        assertThat(board)
                .matches(it -> Arrays.equals(board.getPits(), expectedPits))
                .matches(it -> it.getPlayer1BigPit().equals(new BoardDto.BigPitDto(6, expectedPits[6])))
                .matches(it -> it.getPlayer2BigPit().equals(new BoardDto.BigPitDto(13, expectedPits[13])));
        assertPlayerPits(board.getPlayer1Pits(), IntStream.range(0, 6), expectedPits);
        assertPlayerPits(board.getPlayer2Pits(), IntStream.range(7, 13), expectedPits);
    }

    private void assertPlayerPits(Map<Integer, Integer> pits, IntStream range, int[] expectedPits) {
        assertThat(pits.size()).isEqualTo(6);
        range.forEach(it -> assertThat(pits.get(it)).isEqualTo(expectedPits[it]));
    }

    private GameDto createGame() {
        return new GameDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                TurnType.PLAYER_1,
                boardEntityMapper.toDto(getDefaultPits())
        );
    }

    private int[] getDefaultPits() {
        return new int[]{6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0};
    }
}
