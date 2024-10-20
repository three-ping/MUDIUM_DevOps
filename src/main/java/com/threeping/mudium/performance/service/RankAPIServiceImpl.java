package com.threeping.mudium.performance.service;

import com.threeping.mudium.common.exception.CommonException;
import com.threeping.mudium.common.exception.ErrorCode;
import com.threeping.mudium.musical.dto.MusicalDTO;
import com.threeping.mudium.musical.service.MusicalService;
import com.threeping.mudium.performance.aggregate.PerformanceRank;
import com.threeping.mudium.performance.aggregate.RankType;
import com.threeping.mudium.performance.dto.PerformanceDTO;
import com.threeping.mudium.performance.dto.RankItem;
import com.threeping.mudium.performance.dto.RankResponse;
import com.threeping.mudium.performance.repository.PerformanceRankRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@Slf4j
public class RankAPIServiceImpl implements RankAPIService {

    private final PerformanceRankRepository performanceRankRepository;
    private final MusicalService musicalService;
    private final RankAPIClient rankAPIClient;
    private final PerformanceService performanceService;

    public RankAPIServiceImpl(PerformanceRankRepository performanceRankRepository,
                              PerformanceService performanceService,
                              MusicalService musicalService,
                              RankAPIClient rankAPIClient) {
        this.performanceRankRepository = performanceRankRepository;
        this.performanceService = performanceService;
        this.musicalService = musicalService;
        this.rankAPIClient = rankAPIClient;
    }

//    @Scheduled(cron = "0 0 1 * * ?") 새벽 1시마다 db 자동 업데이트
    @Scheduled(initialDelay = 5000, fixedDelay = 300000000)
    @Transactional
    @Override
    public void updateMonthData() {

        try {
            RankResponse response = rankAPIClient.fetchMonthRank();
            log.info("end fetchMonthRank");
            saveMonthRankToDB(response);
        } catch (Exception e) {
            throw new CommonException(ErrorCode.API_RANK_BAD_REQUEST);
        }

    }

    @Transactional
    public void saveMonthRankToDB(RankResponse response) {
        log.info("start saveMonthRankToDB");
        List<RankItem> itemList = response.getRankItems();
        log.info("진행상황 확인(item list 꺼냄)");
        String[] dates = response.getBaseDate().split("~");
        log.info("진행상황 확인(날짜 배열 생성): " + dates[0] +" with " + dates[1]);
        Timestamp startDate = timeStampConverter(dates[0]);
        log.info("timeStampConverter startDate: " + startDate);
        Timestamp endDate = timeStampConverter(dates[1]);
        log.info("timeStampConverter endDate: " + endDate);

        for (int i = 0; i < 10; i++) {
            log.info("진행상황 확인(월별 순위 반복분 진입)");
            RankItem MonthItem = itemList.get(i);
            String title = normalizeTitle(MonthItem.getTitle());
            log.info("api가 가져온 제목: " + title);
            String area = normalizeArea(MonthItem.getTitle());
            log.info("api가 가져온 지역: " + area);
            MusicalDTO musicalDTO = musicalService.findMusicalDetailByName(title);
            log.info("찾아온 musicalDTO: " + musicalDTO);
            PerformanceDTO performanceDTO =
                    performanceService.findPerformanceByMusicalIdAndRegion(musicalDTO.getMusicalId(), area);
            log.info("찾아온 performanceDTO: " + performanceDTO);

            PerformanceRank performanceRank = new PerformanceRank();
            performanceRank.setMusicalId(musicalDTO.getMusicalId());
            performanceRank.setPerformanceId(performanceDTO.getPerformanceId());
            performanceRank.setRankType(RankType.MONTHLY);
            performanceRank.setStartDate(startDate);
            performanceRank.setEndDate(endDate);
            performanceRank.setRank(MonthItem.getRank());

            performanceRankRepository.save(performanceRank);
        }
    }

    @Scheduled(initialDelay = 5000, fixedDelay = 300000000)
    @Transactional
    @Override
    public void updateDayData() {
        try {
            RankResponse response = rankAPIClient.fetchDayRank();
            saveDayRankToDB(response);
        } catch (Exception e) {
            throw new CommonException(ErrorCode.API_RANK_BAD_REQUEST);
        }
    }

    @Transactional
    public void saveDayRankToDB(RankResponse response) {
        List<RankItem> itemList = response.getRankItems();
        String date = response.getBaseDate();
        Timestamp startDate = timeStampConverter(date);

        for (int i = 0; i < 10; i++) {
            RankItem DayItem = itemList.get(i);
            String title = normalizeTitle(DayItem.getTitle());
            log.info("파싱된 title: " + title);
            String area = normalizeArea(DayItem.getTitle());
            log.info("api가 가져온 지역: " + area);
            MusicalDTO musicalDTO = musicalService.findMusicalDetailByName(title);
            log.info("찾아온 musicalDTO: " + musicalDTO);

            PerformanceDTO performanceDTO =
                    performanceService.findPerformanceByMusicalIdAndRegion(musicalDTO.getMusicalId(), area);
            log.info("찾아온 performanceDTO: " + performanceDTO);

            PerformanceRank performanceRank = new PerformanceRank();
            performanceRank.setMusicalId(musicalDTO.getMusicalId());
            performanceRank.setPerformanceId(performanceDTO.getPerformanceId());
            performanceRank.setRank(DayItem.getRank());
            performanceRank.setStartDate(startDate);
            performanceRank.setEndDate(startDate);
            performanceRank.setRankType(RankType.DAILY);

            performanceRankRepository.save(performanceRank);
        }
    }

    private String normalizeTitle(String title) {
        return title.replaceAll("\\[.*?\\]", "")  // 대괄호와 그 내용 제거 (지역 정보)
                .replaceAll("\\(.*?\\)", "")  // 소괄호와 그 내용 제거
                .replaceAll("\\s+", "")       // 모든 공백 제거
                .toLowerCase()                // 소문자로 변환
                .trim();               // 소문자로 변환
    }

    private String normalizeArea(String title) {
        // 대괄호 안의 내용을 찾습니다.
        String areaPattern = "\\[(.*?)]";
        Pattern pattern = Pattern.compile(areaPattern);
        Matcher matcher = pattern.matcher(title);

        if (matcher.find()) {
            String area = matcher.group(1);
            // 소괄호와 그 내용을 제거합니다.
            area = area.replaceAll("\\(.*?\\)", "");
            // 모든 공백을 제거합니다.
            area = area.replaceAll("\\s+", "");
            if(area.equals("세종시")) area ="세종";
            return area;
        } else {
            // 대괄호가 없는 경우 "서울"을 반환합니다.
            return "서울";
        }
    }

    private Timestamp timeStampConverter(String Date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate localDate = LocalDate.parse(Date, formatter);

        LocalDateTime localDateTime = localDate.atStartOfDay();

        return Timestamp.valueOf(localDateTime);
    }
}
