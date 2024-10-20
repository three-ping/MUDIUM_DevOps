package com.threeping.mudium.performance.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PerformanceRankDTO {

    private Long musicalId;

    private String title;

    private String poster;

    private String region;

    private Integer rank;
}
