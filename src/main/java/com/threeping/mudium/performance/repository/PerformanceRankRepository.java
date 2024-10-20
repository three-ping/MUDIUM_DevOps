package com.threeping.mudium.performance.repository;

import com.threeping.mudium.performance.aggregate.PerformanceRank;
import com.threeping.mudium.performance.aggregate.RankType;
import com.threeping.mudium.performance.dto.PerformanceRankDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PerformanceRankRepository extends JpaRepository<PerformanceRank, Long> {
    @Query("SELECT new com.threeping.mudium.performance.dto.PerformanceRankDTO(" +
            "pr.musicalId, m.title, p.poster, p.region, pr.rank) " +
            "FROM rankEntity pr " +
            "JOIN MusicalEntity m ON pr.musicalId = m.musicalId " +
            "JOIN TBL_PERFORMANCE p ON pr.performanceId = p.performanceId " +
            "WHERE pr.endDate = :endDate AND pr.rankType = :rankType " +
            "ORDER BY pr.rank")
    List<PerformanceRankDTO> findRankDTOs(
            @Param("endDate") Timestamp endDate,
            @Param("rankType") RankType rankType
    );
}
