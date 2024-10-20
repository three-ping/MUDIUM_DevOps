package com.threeping.mudium.performance.service;

import com.threeping.mudium.common.exception.CommonException;
import com.threeping.mudium.common.exception.ErrorCode;
import com.threeping.mudium.musical.aggregate.Musical;
import com.threeping.mudium.performance.aggregate.Performance;
import com.threeping.mudium.performance.aggregate.PerformanceRank;
import com.threeping.mudium.performance.aggregate.RankType;
import com.threeping.mudium.performance.dto.PerformanceDTO;
import com.threeping.mudium.performance.dto.PerformanceRankDTO;
import com.threeping.mudium.performance.repository.PerformanceRankRepository;
import com.threeping.mudium.performance.repository.PerformanceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class PerformanceServiceImpl implements PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final PerformanceRankRepository performanceRankRepository;

    @Autowired
    public PerformanceServiceImpl(PerformanceRepository performanceRepository, PerformanceRankRepository performanceRankRepository) {
        this.performanceRepository = performanceRepository;
        this.performanceRankRepository = performanceRankRepository;
    }


    @Override
    public List<PerformanceDTO> findPerformances(Long musicalId) {
        List<Performance> performanceList = performanceRepository.findAllByMusicalId(musicalId);
        if (performanceList.isEmpty()) {
            throw new CommonException(ErrorCode.INVALID_MUSICAL_ID);
        }
        List<PerformanceDTO> dtoList = performanceList.stream()
                .map(performance -> {
                    PerformanceDTO dto = new PerformanceDTO();
                    dto.setPerformanceId(performance.getPerformanceId());
                    dto.setActors(performance.getActorList());
                    dto.setPoster(performance.getPoster());
                    dto.setRegion(performance.getRegion());
                    dto.setTheater(performance.getTheater());
                    dto.setEndDate(performance.getEndDate());
                    dto.setStartDate(performance.getStartDate());
                    dto.setMusicalId(performance.getMusicalId());
                    return dto;
                }).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public PerformanceDTO findPerformanceByMusicalIdAndRegion(Long musicalId, String region) {
        Performance performance = performanceRepository.findPerformanceByMusicalIdAndRegion(musicalId, region)
                .orElseThrow(() -> new CommonException(ErrorCode.INVALID_MUSICAL_ID));

        PerformanceDTO dto = new PerformanceDTO();
        dto.setPerformanceId(performance.getPerformanceId());
        dto.setPoster(performance.getPoster());
        dto.setActors(performance.getActorList());
        dto.setMusicalId(performance.getMusicalId());
        dto.setRegion(region);
        dto.setTheater(performance.getTheater());
        dto.setEndDate(performance.getEndDate());
        dto.setStartDate(performance.getStartDate());

        return dto;
    }

    @Override
    public List<PerformanceRankDTO> findDayRank() {
        Timestamp today = getTodayTimestamp();
        log.info("Today is {}", today);
        List<PerformanceRankDTO> dayList = performanceRankRepository.findRankDTOs(today, RankType.DAILY);

        return dayList;
    }

    @Override
    public List<PerformanceRankDTO> findMonthRank() {
        Timestamp today = getTodayTimestamp();
        List<PerformanceRankDTO> monthList = performanceRankRepository.findRankDTOs(today, RankType.MONTHLY);

        return monthList;
    }

    private Timestamp getTodayTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        now = now.minusDays(1);
        LocalDate today = now.toLocalDate();
        return Timestamp.valueOf(today.atStartOfDay());
    }
}
