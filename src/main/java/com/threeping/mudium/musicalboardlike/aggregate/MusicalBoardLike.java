package com.threeping.mudium.musicalboardlike.aggregate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "musicalLikeEntity")
@Table(name = "TBL_MUSICAL_BOARD_LIKE")
@IdClass(MusicalBoardLikePK.class)
public class MusicalBoardLike {

    @Id
    @Column(name = "musical_board_id")
    private Long boardId;

    @Id
    @Column(name = "user_id")
    private Long userId;
}
