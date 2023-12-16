package org.mrsash.kalahapi.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import lombok.SneakyThrows;
import org.mrsash.kalahapi.dto.LoginDto;
import org.mrsash.kalahapi.dto.PlayerCreateDto;
import org.mrsash.kalahapi.dto.PlayerDto;
import org.mrsash.kalahapi.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public abstract class AIntegrationTest {

    @Container
    @ServiceConnection
    @SuppressWarnings("unused")
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.1");

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
        public void createPlayer(String name) {
            createPlayerPerform(name).andExpect(status().isOk());
        }

        @SneakyThrows
        public void createPlayerException(String name, HttpStatus status) {
            createPlayerPerform(name).andExpect(status().is(status.value()));
        }

        @SneakyThrows
        private ResultActions loginPerform(String name) {
            return mockMvc.perform(MockMvcRequestBuilders.get("/v1/players/login")
                            .param("name", name)
                    )
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
        public ResultActions getPlayerPerform(UUID playerId) {
            return mockMvc.perform(MockMvcRequestBuilders.get("/v1/players/" + playerId.toString()))
                    .andDo(print());
        }
    }
}
