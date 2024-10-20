package com.threeping.mudium.guidebook.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "TBL_RECOMMENDED_MUSICAL")
public class RecommendedMusical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommended_musical_id")
    private Long recommendedId;

    @Column(name = "title", nullable = false, length = 1023)
    private String musicalTitle;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String musicalDescription;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

//    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;
}
