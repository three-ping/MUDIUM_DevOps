package com.threeping.mudium.musical.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MusicalDTO {

    private Long musicalId;

    private String title;

    private String rating;

    private String poster;

    private String production;

    private Long viewCount;

    private String synopsys;

    private String reviewVideos;
}
