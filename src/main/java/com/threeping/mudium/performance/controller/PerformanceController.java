package com.threeping.mudium.performance.controller;

import com.threeping.mudium.common.ResponseDTO;
import com.threeping.mudium.performance.dto.PerformanceDTO;
import com.threeping.mudium.performance.dto.PerformanceRankDTO;
import com.threeping.mudium.performance.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/performance")
public class PerformanceController {

    private final PerformanceService performanceService;

    @Autowired
    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    // 뮤지컬 상세조회를 했을 때 모든 공연을 주기 위한 핸들러 메서드
    @GetMapping("/{musicalId}")
    public ResponseDTO<?> findPerformances(@PathVariable Long musicalId) {
        List<PerformanceDTO> dtoList = performanceService.findPerformances(musicalId);

        ResponseDTO<List<PerformanceDTO>> responseDTO = new ResponseDTO<>();
        responseDTO.setData(dtoList);
        responseDTO.setHttpStatus(HttpStatus.OK);
        responseDTO.setSuccess(true);

        return responseDTO;
    }

//    @GetMapping("/{musicalId}/{performanceId}")
//    public ResponseDTO<?> findPerformance(@PathVariable Long musicalId, @PathVariable Long performanceId) {
//        PerformanceDTO dto =
//    }

    // 메인 페이지에서 월간 랭킹을 주기 위한 핸들러 메서드
    @GetMapping("/rank/month")
    public ResponseDTO<?> findMonthPerformancesRank() {
        List<PerformanceRankDTO> monthRankList = performanceService.findMonthRank();

        ResponseDTO<List<PerformanceRankDTO>> responseDTO = new ResponseDTO<>();
        responseDTO.setData(monthRankList);
        responseDTO.setHttpStatus(HttpStatus.OK);
        responseDTO.setSuccess(true);

        return responseDTO;
    }

    // 메인 페이지에서 일간 랭킹을 주기 위한 핸들러 메서드
    @GetMapping("/rank/day")
    public ResponseDTO<?> findDayPerformancesRank() {
        List<PerformanceRankDTO> dayRankList = performanceService.findDayRank();

        ResponseDTO<List<PerformanceRankDTO>> responseDTO = new ResponseDTO<>();
        responseDTO.setData(dayRankList);
        responseDTO.setHttpStatus(HttpStatus.OK);
        responseDTO.setSuccess(true);

        return responseDTO;
    }
}
