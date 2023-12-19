package org.mrsash.kalah.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.UUID;
import lombok.SneakyThrows;
import org.mrsash.kalah.dto.GameCreateDto;
import org.mrsash.kalah.dto.GameDto;
import org.mrsash.kalah.dto.GameMoveDto;
import org.mrsash.kalah.dto.GameMoveResultDto;
import org.mrsash.kalah.dto.LoginDto;
import org.mrsash.kalah.dto.PlayerCreateDto;
import org.mrsash.kalah.dto.PlayerDto;
import org.mrsash.kalah.model.TurnType;
import org.mrsash.kalah.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
public abstract class AIntegrationTest {

    @Autowired
    protected RestSender restSender;

    @Component
    protected static class RestSender {

        private final MockMvc mockMvc;

        @Autowired
        protected RestSender(MockMvc mockMvc) {
            this.mockMvc = mockMvc;
        }

        @SneakyThrows
        public LoginDto login(String name) {
            return MapperUtil.fromJson(
                    loginPerform(name)
                            .andExpect(status().isOk())
                            .andReturn()
                            .getResponse()
                            .getContentAsString(),
                    LoginDto.class
            );
        }

        @SneakyThrows
        public void loginException(String name, HttpStatus status) {
            loginPerform(name).andExpect(status().is(status.value()));
        }

        @SneakyThrows
        public PlayerDto getPlayer(UUID playerId) {
            return MapperUtil.fromJson(
                    getPlayerPerform(playerId)
                            .andExpect(status().isOk())
                            .andReturn()
                            .getResponse()
                            .getContentAsString(),
                    PlayerDto.class
            );
        }

        @SneakyThrows
        public void getPlayerException(UUID playerId, HttpStatus status) {
            getPlayerPerform(playerId).andExpect(status().is(status.value()));
        }

        @SneakyThrows
        public PlayerDto createPlayer(String name) {
            return MapperUtil.fromJson(
                    createPlayerPerform(name)
                            .andExpect(status().isOk())
                            .andReturn()
                            .getResponse()
                            .getContentAsString(),
                    PlayerDto.class
            );
        }

        @SneakyThrows
        public void createPlayerException(String name, HttpStatus status) {
            createPlayerPerform(name).andExpect(status().is(status.value()));
        }

        @SneakyThrows
        public GameDto getGame(UUID gameId) {
            return MapperUtil.fromJson(
                    getGamePerform(gameId)
                            .andExpect(status().isOk())
                            .andReturn()
                            .getResponse()
                            .getContentAsString(),
                    GameDto.class
            );
        }

        @SneakyThrows
        public void getGameException(UUID gameId, HttpStatus status) {
            getGamePerform(gameId).andExpect(status().is(status.value()));
        }

        @SneakyThrows
        public List<GameDto> getAllPlayerGames(UUID playerId) {
            return MapperUtil.fromJson(
                    getAllPlayerGamesPerform(playerId)
                            .andExpect(status().isOk())
                            .andReturn()
                            .getResponse()
                            .getContentAsString(),
                    new TypeReference<>() {}
            );
        }

        @SneakyThrows
        public void getAllPlayerGamesException(UUID playerId, HttpStatus status) {
            getAllPlayerGamesPerform(playerId).andExpect(status().is(status.value()));
        }

        @SneakyThrows
        public GameDto createGame(String ownerId) {
            return MapperUtil.fromJson(
                    createGamePerform(ownerId)
                            .andExpect(status().isOk())
                            .andReturn()
                            .getResponse()
                            .getContentAsString(),
                    GameDto.class
            );
        }

        @SneakyThrows
        public void createGameException(String ownerId, HttpStatus status) {
            createGamePerform(ownerId).andExpect(status().is(status.value()));
        }

        @SneakyThrows
        public GameMoveResultDto move(UUID gameId, int position, TurnType turn) {
            return MapperUtil.fromJson(
                    movePerform(gameId.toString(), position, turn)
                            .andExpect(status().isOk())
                            .andReturn()
                            .getResponse()
                            .getContentAsString(),
                    GameMoveResultDto.class
            );
        }

        @SneakyThrows
        public void moveException(UUID gameId, int position, TurnType turn, HttpStatus status) {
            movePerform(gameId.toString(), position, turn).andExpect(status().is(status.value()));
        }

        @SneakyThrows
        private ResultActions loginPerform(String name) {
            return mockMvc.perform(MockMvcRequestBuilders.get("/v1/players/login")
                            .param("name", name)
                    )
                    .andDo(print());
        }

        @SneakyThrows
        public ResultActions getPlayerPerform(UUID playerId) {
            return mockMvc.perform(MockMvcRequestBuilders.get("/v1/players/" + playerId.toString()))
                    .andDo(print());
        }

        @SneakyThrows
        private ResultActions createPlayerPerform(String name) {
            return mockMvc.perform(MockMvcRequestBuilders.post("/v1/players")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(MapperUtil.toJson(new PlayerCreateDto(name)))
                    )
                    .andDo(print());
        }

        @SneakyThrows
        public ResultActions getGamePerform(UUID gameId) {
            return mockMvc.perform(MockMvcRequestBuilders.get("/v1/games/" + gameId.toString()))
                    .andDo(print());
        }

        @SneakyThrows
        public ResultActions getAllPlayerGamesPerform(UUID playerId) {
            return mockMvc.perform(MockMvcRequestBuilders.get("/v1/games/players/" + playerId.toString()))
                    .andDo(print());
        }

        @SneakyThrows
        private ResultActions createGamePerform(String ownedId) {
            return mockMvc.perform(MockMvcRequestBuilders.post("/v1/games")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(MapperUtil.toJson(new GameCreateDto(ownedId)))
                    )
                    .andDo(print());
        }

        @SneakyThrows
        private ResultActions movePerform(String gameId, int position, TurnType turn) {
            return mockMvc.perform(MockMvcRequestBuilders.post(String.format("/v1/games/%s/move", gameId))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(MapperUtil.toJson(new GameMoveDto(position, turn)))
                    )
                    .andDo(print());
        }
    }
}
