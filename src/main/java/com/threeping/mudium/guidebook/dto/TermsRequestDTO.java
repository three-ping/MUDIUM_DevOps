package com.threeping.mudium.guidebook.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class TermsRequestDTO {

    private String terms;
    private String termsDefinition;
    private Long userId;
}
