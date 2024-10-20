package com.threeping.mudium.boardlike.aggregate.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BoardLikePK implements Serializable {
    private Long boardId;
    private Long userId;
}
