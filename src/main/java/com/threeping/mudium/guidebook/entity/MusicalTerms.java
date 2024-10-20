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
@Table(name = "TBL_MUSICAL_TERMS")
public class MusicalTerms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "musical_terms_id")
    private Long termId;

    @Column(name = "musical_terms", nullable = false, length = 1023)
    private String terms;

    @Column(name = "musical_terms_definition", nullable = false, columnDefinition = "TEXT")
    private String termsDefinition;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;

}
