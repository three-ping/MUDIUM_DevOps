package com.threeping.mudium.guidebook.dto;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class RecommendedRequestDTO {
    private String musicalTitle;
    private String musicalDescription;
    private Long userId;

}