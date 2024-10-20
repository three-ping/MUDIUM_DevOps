package com.threeping.mudium.mucialcomment.aggregate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "MusicalCommentEntity")
@Table(name = "TBL_MUSICAL_BOARD_COMMENT")
public class MusicalComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "musical_board_comment_id")
    private Long musicalBoardCommentId;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "active_status")
    @Enumerated(EnumType.STRING)
    private ActiveStatus activeStatus = ActiveStatus.ACTIVE;

    @Column(name = "musical_board_id")
    private Long musicalPostId;

    @Column(name = "user_id")
    private Long userId;

    void softDelete() {
        this.activeStatus = ActiveStatus.INACTIVE;
    }
}
