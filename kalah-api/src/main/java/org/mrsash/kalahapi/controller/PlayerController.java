package org.mrsash.kalahapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.mrsash.kalahapi.dto.LoginDto;
import org.mrsash.kalahapi.dto.PlayerCreateDto;
import org.mrsash.kalahapi.dto.PlayerDto;
import org.mrsash.kalahapi.service.IPlayerService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/players")
public class PlayerController {

    private final IPlayerService playerService;

    @Autowired
    public PlayerController(IPlayerService playerService) {
        this.playerService = playerService;
    }

    /**
     * This method is only a login stub because at the time the server does not have an authorization system,
     * but we need to identify a player.
     *
     * @param name player name
     * @return player state
     */
    @Operation(summary = "Player login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully login"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Incorrect login request. Check validation boundaries",
                    content = @Content
            ),
            @ApiResponse(responseCode = "404", description = "Player not found", content = @Content)
    })
    @GetMapping("/login")
    public LoginDto login(
            @Valid
            @NotBlank
            @Size(min = 5, max = 30)
            @Parameter(description = "Player name. Validation: not null/empty, size between 5 and 30")
            @RequestParam("name")
            String name
    ) {
        return playerService.login(name);
    }

    @Operation(summary = "Get player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully get player"),
            @ApiResponse(responseCode = "404", description = "Player not found", content = @Content)
    })
    @GetMapping("/{playerId}")
    public PlayerDto getPlayer(@PathVariable("playerId") String playerId) {
        return playerService.get(playerId);
    }

    @Operation(summary = "Create new player")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully create new player"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Incorrect create body request. Check validation boundaries",
                    content = @Content
            )
    })
    @PostMapping
    public void createPlayer(@ParameterObject @Valid @RequestBody PlayerCreateDto playerCreateDto) {
        playerService.create(playerCreateDto);
    }
}
