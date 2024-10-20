package com.threeping.mudium.bookmark.entity;

import jakarta.persistence.*;
import lombok.*;
import com.threeping.mudium.musical.aggregate.Musical;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TBL_BOOKMARK")
@IdClass(BookmarkPK.class)
public class Bookmark implements Serializable {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "musical_info_id", nullable = false)
    private Long musicalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "musical_info_id", insertable = false, updatable = false)
    private Musical musical;

    public Bookmark(Long userId, Long musicalId) {
        this.userId = userId;
        this.musicalId = musicalId;
    }
}