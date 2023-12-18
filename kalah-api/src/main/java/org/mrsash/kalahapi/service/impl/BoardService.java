package org.mrsash.kalahapi.service.impl;

import static org.mrsash.kalahapi.model.TurnType.PLAYER_1;
import static org.mrsash.kalahapi.model.TurnType.PLAYER_2;

import java.util.Arrays;
import java.util.UUID;
import org.mrsash.kalahapi.dto.BoardDto;
import org.mrsash.kalahapi.dto.BoardResultDto;
import org.mrsash.kalahapi.dto.GameDto;
import org.mrsash.kalahapi.exception.GameIncorrectPositionException;
import org.mrsash.kalahapi.mapper.IBoardEntityMapper;
import org.mrsash.kalahapi.model.TurnType;
import org.mrsash.kalahapi.service.IBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService implements IBoardService {

    private final IBoardEntityMapper boardEntityMapper;

    @Autowired
    public BoardService(IBoardEntityMapper boardEntityMapper) {
        this.boardEntityMapper = boardEntityMapper;
    }

    @Override
    public int[] create() {
        return new int[]{6, 6, 6, 6, 6, 6, 0, 6, 6, 6, 6, 6, 6, 0};
    }

    @Override
    public BoardResultDto calculate(int position, GameDto game) {
        var board = game.getBoard();
        var pits = Arrays.copyOf(board.getPits(), board.getPits().length);
        var turn = game.getTurn();
        checkMove(game.getId(), turn, position, pits, board);
        var pitValue = pits[position];
        pits[position] = 0;
        while (pitValue > 0) {
            pitValue--;
            position++;
            if (isOppositeBigPit(turn, position, board)) {
                position++;
            }
            if (position >= pits.length) {
                position = 0;
            }
            pits[position]++;
        }
        checkCapture(turn, position, pits, board);
        return new BoardResultDto(
                isFinished(pits, board),
                getNextTurn(turn, position, board),
                boardEntityMapper.toDto(pits)
        );
    }

    /**
     * Make sure the game is finished. If one of the players has all zero pits then return
     * a success result and add all opponent pits in his big pit.
     * Also, we reset all pits (except big pits) for sending state to the client
     */
    private boolean isFinished(int[] pits, BoardDto board) {
        var player1Sum = board.getPlayer1Pits().keySet().stream().mapToInt(it -> pits[it]).sum();
        var player2Sum = board.getPlayer2Pits().keySet().stream().mapToInt(it -> pits[it]).sum();
        if (player1Sum == 0 || player2Sum == 0) {
            if (player1Sum == 0) {
                pits[board.getPlayer2BigPit().getPosition()] += player2Sum;
            }
            if (player2Sum == 0) {
                pits[board.getPlayer1BigPit().getPosition()] += player1Sum;
            }
            board.getPlayer1Pits().forEach((key, value) -> pits[key] = 0);
            board.getPlayer2Pits().forEach((key, value) -> pits[key] = 0);
            return true;
        }
        return false;
    }

    /**
     * If a player makes a move on his position and its position is empty
     * then he can steal points from the opponent pit in the opposite position.
     */
    private void checkCapture(TurnType turn, int position, int[] pits, BoardDto board) {
        if (isPlayerPosition(turn, position, board) && pits[position] == 1) {
            pits[position] = 0;
            int bigPitPosition = turn == PLAYER_1
                    ? board.getPlayer1BigPit().getPosition()
                    : board.getPlayer2BigPit().getPosition();
            int oppositePosition = Math.abs(position - board.getPits().length) - 2;
            pits[bigPitPosition] += pits[oppositePosition] + 1;
            pits[oppositePosition] = 0;
        }
    }

    /**
     * Check that position is not a big pit position, opponent position and not empty position
     */
    private void checkMove(UUID gameId, TurnType turn, int position, int[] pits, BoardDto board) {
        if (position == board.getPlayer1BigPit().getPosition() || position == board.getPlayer2BigPit().getPosition()) {
            throw new GameIncorrectPositionException(gameId, turn, position);
        }
        if (!isPlayerPosition(turn, position, board)) {
            throw new GameIncorrectPositionException(gameId, turn, position);
        }
        if (pits[position] == 0) {
            throw new GameIncorrectPositionException(gameId, turn, position);
        }
    }

    private boolean isOppositeBigPit(TurnType turn, int position, BoardDto board) {
        return (turn == PLAYER_1 && position == board.getPlayer2BigPit().getPosition())
                || (turn == PLAYER_2 && position == board.getPlayer1BigPit().getPosition());
    }

    private boolean isPlayerPosition(TurnType turn, int position, BoardDto board) {
        return turn == PLAYER_1
                ? board.getPlayer1Pits().containsKey(position)
                : board.getPlayer2Pits().containsKey(position);
    }

    private TurnType getNextTurn(TurnType turn, int position, BoardDto board) {
        if ((turn == PLAYER_1 && position == board.getPlayer1BigPit().getPosition())
                || (turn == PLAYER_2 && position == board.getPlayer2BigPit().getPosition())) {
            return turn;
        }
        return switch (turn) {
            case PLAYER_1 -> PLAYER_2;
            case PLAYER_2 -> PLAYER_1;
        };
    }
}
