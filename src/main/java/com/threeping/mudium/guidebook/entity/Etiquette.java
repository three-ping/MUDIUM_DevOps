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
@Table(name = "TBL_ETIQUETTE")
public class Etiquette {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "etiquette_id")
    private Long etiquetteId;

    @Column(name = "etiquette", nullable = false, length = 1023)
    private String etiquette;

    @Column(name = "etiquette_description", nullable = false, columnDefinition = "TEXT")
    private String etiquetteDescription;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;
}
