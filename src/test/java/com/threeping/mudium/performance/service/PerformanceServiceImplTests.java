package com.threeping.mudium.performance.service;

import com.threeping.mudium.performance.dto.PerformanceDTO;
import com.threeping.mudium.performance.dto.PerformanceRankDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class PerformanceServiceImplTests {

    private final PerformanceService performanceService;

    @Autowired
    PerformanceServiceImplTests(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @DisplayName("특정 뮤지컬의 모든 공연 리스트 조회")
    @Test
    void searchPerformance() {
        // Given
        Long musicalId = 1L;

        // When
        List<PerformanceDTO> dtoList = performanceService.findPerformances(musicalId);

        // Then
        assertNotNull(dtoList, "조회된 공연리스트는 null이 아니다.");
        assertTrue(dtoList.get(0).getMusicalId().equals(musicalId), "조회된 리스트의 뮤지컬 번호 확인");
    }

    @DisplayName("월별 순위 리스트 조회")
    @Test
    void searchMonthRank() {
        // When
        List<PerformanceRankDTO> monthList = performanceService.findMonthRank();

        for (PerformanceRankDTO dto : monthList) {
            log.info("10개의 공연 순위 확인" + dto.toString());
        }

        // Then
        assertNotNull(monthList, "조회된 월별 랭킹은 null이 아니다.");
        assertTrue(monthList.size() == 10, "조회된 리스트의 공연 개수는 10개다.");
    }

    @DisplayName("일별 순위 리스트 조회")
    @Test
    void searchDayRank() {
        // When
        List<PerformanceRankDTO> dayList = performanceService.findDayRank();

        for (PerformanceRankDTO dto : dayList) {
            log.info("10개의 공연 순위 확인" + dto.toString());
        }

        // Then
        assertNotNull(dayList, "조회된 일별 랭킹은 null이 아니다.");
        assertTrue(dayList.size() == 10, "조회된 리스트의 공연 개수는 10개다.");
    }

}