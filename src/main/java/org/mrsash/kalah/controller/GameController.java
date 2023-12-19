package org.mrsash.kalah.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import org.mrsash.kalah.dto.GameCreateDto;
import org.mrsash.kalah.dto.GameDto;
import org.mrsash.kalah.dto.GameMoveDto;
import org.mrsash.kalah.dto.GameMoveResultDto;
import org.mrsash.kalah.service.IGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/games")
public class GameController {

    private final IGameService gameService;

    @Autowired
    public GameController(IGameService gameService) {
        this.gameService = gameService;
    }

    @Operation(summary = "Get game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got game"),
            @ApiResponse(responseCode = "404", description = "Game not found", content = @Content)
    })
    @GetMapping("/{gameId}")
    public GameDto getGame(@PathVariable("gameId") String gameId) {
        return gameService.get(gameId);
    }

    @Operation(summary = "Get all player games")
    @ApiResponse(responseCode = "200", description = "Successfully got all player games")
    @GetMapping("/players/{playerId}")
    public List<GameDto> getAllPlayerGames(@PathVariable("playerId") String playerId) {
        return gameService.getAllByPlayer(playerId);
    }

    @Operation(summary = "Create new game")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully created new game"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Incorrect body request. Check validation boundaries",
                    content = @Content
            )
    })
    @PostMapping
    public GameDto createGame(@Valid @RequestBody GameCreateDto gameCreateDto) {
        return gameService.create(gameCreateDto);
    }

    @Operation(summary = "Make a move")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully made a move"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Incorrect body request. Check validation boundaries",
                    content = @Content
            )
    })
    @PostMapping("/{gameId}/move")
    public GameMoveResultDto move(
            @PathVariable("gameId")
            String gameId,
            @Valid
            @RequestBody
            GameMoveDto gameMoveDto
    ) {
        return gameService.move(gameId, gameMoveDto);
    }
}
