package com.threeping.mudium.performance.aggregate;

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
@Entity(name = "rankEntity")
@Table(name = "TBL_PERFORMANCE_RANK")
public class PerformanceRank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "performance_rank_id")
    private Long rankId;

    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "end_date")
    private Timestamp endDate;

    @Column(name = "rank_type")
    @Enumerated(EnumType.STRING)
    private RankType rankType;

    @Column(name = "rank")
    private Integer rank;

    @Column(name = "musical_id")
    private Long musicalId;

    @Column(name = "performance_id")
    private Long performanceId;

}
