package com.threeping.mudium.boardlike.aggregate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Entity
@Table(name = "TBL_BOARD_LIKE")
@IdClass(BoardLikePK.class)
public class BoardLike {
    @Id
    @Column(name="board_id")
    private Long boardId;

    @Id
    @Column(name="user_id")
    private Long userId;
}
