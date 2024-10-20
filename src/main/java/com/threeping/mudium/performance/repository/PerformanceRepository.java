package com.threeping.mudium.performance.repository;

import com.threeping.mudium.performance.aggregate.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    Optional<Performance> findPerformanceByPerformanceId(Long performanceId);
    List<Performance> findAllByMusicalId(Long musicalId);
    Optional<Performance> findPerformanceByMusicalIdAndRegion(Long musicalId, String region);
}
