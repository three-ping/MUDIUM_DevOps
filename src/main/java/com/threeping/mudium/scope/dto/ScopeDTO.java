package com.threeping.mudium.scope.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ScopeDTO {

    private Long userId;

    private Long musicalId;

    private Double scope;

    private String nickName;
}
