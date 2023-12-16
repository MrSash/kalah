package org.mrsash.kalahapi.dto;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerCreateDto {

    @NotBlank
    @Size(min = 5, max = 30)
    @Parameter(description = "Player name. Validation: not null/empty, size between 5 and 30")
    private String name;
}
