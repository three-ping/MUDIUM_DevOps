package com.threeping.mudium.musical.aggregate;

import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "MusicalEntity")
@Table(name = "TBL_MUSICAL_INFO")
public class Musical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "musical_info_id")
    private Long musicalId;

    @Column(name = "title")
    private String title;

    @Column(name = "rating")
    private String rating;

    @Column(name = "review_video")
    private String reviewVideo;

    @Column(name = "image_url")
    private String poster;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "production")
    private String production;

    @Column(name = "synopsys")
    private String synopsys;
}
