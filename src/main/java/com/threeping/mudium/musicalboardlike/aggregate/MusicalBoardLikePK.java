package com.threeping.mudium.musicalboardlike.aggregate;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MusicalBoardLikePK implements Serializable {

    private Long boardId;

    private Long userId;

}
