package com.threeping.mudium.performance.dto;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PerformanceDTO {

    private Long performanceId;

    private String theater;

    private String region;

    private String poster;

    private String actors;

    private Timestamp startDate;

    private Timestamp endDate;

    private Long musicalId;

}
