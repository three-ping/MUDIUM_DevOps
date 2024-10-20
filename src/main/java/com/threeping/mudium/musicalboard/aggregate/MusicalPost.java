package com.threeping.mudium.musicalboard.aggregate;

import com.threeping.mudium.musical.aggregate.Musical;
import com.threeping.mudium.user.aggregate.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "musicalPost")
@Table(name = "TBL_MUSICAL_BOARD")
public class MusicalPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "musical_board_id")
    private Long musicalPostId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "`like`")
    private Long likeCount;

    @Column(name = "comments")
    private Long comments;

//    @Column(name = "replys")
//    private Long replys;

    @Enumerated(EnumType.STRING)
    @Column(name = "active_status", nullable = false)
    private ActiveStatus activeStatus = ActiveStatus.ACTIVE;

    @Column(name = "user_id")
    private Long userId;

    @Column
    private Long musicalId;

    public void softDelete() {
        this.activeStatus = ActiveStatus.INACTIVE;
    }
}
