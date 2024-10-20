package com.threeping.mudium.performance.aggregate;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "TBL_PERFORMANCE")
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "performance_id")
    private Long performanceId;

    @Column(name = "region")
    private String region;

    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "end_date")
    private Timestamp endDate;

    @Column(name = "runtime")
    private String runTime;

    @Column(name = "theater")
    private String theater;

    @Column(name = "actor_list")
    private String actorList;

    @Column(name = "poster")
    private String poster;

    @Column(name = "musical_info_id")
    private Long musicalId;
}
