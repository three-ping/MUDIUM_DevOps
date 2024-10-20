package com.threeping.mudium.guidebook.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class EtiquetteRequestDTO {

    private String etiquette;
    private String etiquetteDescription;
    private Long userId;

}
