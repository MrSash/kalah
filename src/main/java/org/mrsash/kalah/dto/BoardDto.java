package org.mrsash.kalah.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {

    private int[] pits;

    private Map<Integer, Integer> player1Pits;

    private Map<Integer, Integer> player2Pits;

    private BigPitDto player1BigPit;

    private BigPitDto player2BigPit;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BigPitDto {

        private int position;

        private int value;
    }
}
