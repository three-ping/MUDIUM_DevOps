package com.threeping.mudium.musical.service;

import com.threeping.mudium.common.exception.CommonException;
import com.threeping.mudium.common.exception.ErrorCode;
import com.threeping.mudium.musical.aggregate.Musical;
import com.threeping.mudium.musical.dto.MusicalItem;
import com.threeping.mudium.musical.repository.MusicalRepository;
import com.threeping.mudium.performance.aggregate.Performance;
import com.threeping.mudium.performance.dto.PerformanceItem;
import com.threeping.mudium.performance.repository.PerformanceRepository;
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
public class APIServiceImpl implements APIService {

    private final MusicalRepository musicalRepository;
    private final PerformanceRepository performanceRepository;
    private final MusicalAPIClient musicalAPIClient;

    public APIServiceImpl(MusicalRepository musicalRepository, PerformanceRepository performanceRepository,
                          MusicalAPIClient musicalAPIClient) {
        this.musicalRepository = musicalRepository;
        this.performanceRepository = performanceRepository;
        this.musicalAPIClient = musicalAPIClient;
    }

//    @Scheduled(cron = "0 0 1 * * ?") 새벽 1시마다 db 자동 업데이트
    @Scheduled(initialDelay = 5000, fixedDelay = 300000000)
    @Transactional
    @Override
    public void updateMusicalData() {
        try {
            log.info("musical data 업데이트 중...");
            List<MusicalItem> musicalItems = musicalAPIClient.fetchMusicalList();
            for (MusicalItem item : musicalItems) {
                processMusicalItem(item);
            }
        } catch (Exception e) {
            throw new CommonException(ErrorCode.API_LIST_BAD_REQUEST);
        }
    }

    private void processMusicalItem(MusicalItem item) {
        try {
            String OriginTitle = normalizeTitle(item.getTitle());
            String area = normalizeArea(item.getTitle());
            log.info("제목 파싱 확인: {}", OriginTitle);
            log.info("지역 파싱 확인: {}", area);
            Musical musical = getOrCreatedMusical(OriginTitle);
            PerformanceItem performanceItem =
                    musicalAPIClient.fetchPerformanceDetail(item.getExternalId());
            log.info("external Id: " + item.getExternalId());
            updateMusicalInfo(musical, performanceItem);
            log.info("업데이트된 뮤지컬 정보 확인: " + musical);
            Performance performance = getOrCreatedPerformance(musical.getMusicalId(), area);
            updatePerformance(performance, performanceItem);
            log.info("업데이트된 공연 정보 확인: " + performance);

            musicalRepository.save(musical);
            performanceRepository.save(performance);
        } catch (Exception e) {
            log.info("저장 실패한 item의 외부 externalId: " + item.getExternalId());
            log.info("저장에 실패한 item의 타이틀: " + item.getTitle());
            throw new CommonException(ErrorCode.ITEM_PROCESSING_ERROR);
        }
    }

    private void updatePerformance(Performance performance, PerformanceItem performanceItem) {
        if (performance.getActorList() == null || performance.getActorList().isEmpty())
            performance.setActorList(performanceItem.getActorList());

        if (performance.getTheater() == null || performance.getTheater().isEmpty())
            performance.setTheater(performanceItem.getTheater());

        if (performance.getEndDate() == null)
            performance.setEndDate(timeStampConverter(performanceItem.getEndDate()));

        if (performance.getStartDate() == null)
            performance.setStartDate(timeStampConverter(performanceItem.getStartDate()));

        if (performance.getRunTime() == null || performance.getRunTime().isEmpty())
            performance.setRunTime(performanceItem.getRunTime());

        if (performance.getPoster() == null || performance.getPoster().isEmpty())
            performance.setPoster(performanceItem.getPoster());
    }

    private void updateMusicalInfo(Musical musical, PerformanceItem performanceItem) {
        if(musical.getRating() == null || musical.getRating().isEmpty())
        musical.setRating(performanceItem.getAge());
        if(musical.getPoster() == null)
            musical.setPoster(performanceItem.getPoster());
        if(musical.getProduction() == null)
            musical.setProduction(performanceItem.getEntrps());
    }

    private Performance getOrCreatedPerformance(Long musicalId, String area) {
        return performanceRepository.findPerformanceByMusicalIdAndRegion(musicalId, area)
                .orElseGet(() -> {
                    Performance performance = new Performance();
                    performance.setMusicalId(musicalId);
                    performance.setRegion(area);
                    log.info("새로 생성된 공연 정보: " + performance);
                    return performance;
                });
    }

    private Musical getOrCreatedMusical(String originTitle) {
        return musicalRepository.findMusicalByExactTitle(originTitle).orElseGet(() -> {
           Musical musical = new Musical();
           musical.setTitle(originTitle);
           musical.setViewCount(0L);
           musicalRepository.save(musical);
           // 뮤지컬에서 관람등급을 보고 다르다면 또
           return musical;
        });
    }

    private String normalizeTitle(String title) {
        return title.replaceAll("\\[.*?\\]", "")
                .replaceAll("\\(.*?\\)", "")
                .replaceAll("\\s+", "")
                .toLowerCase()
                .trim();
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        LocalDate localDate = LocalDate.parse(Date, formatter);

        LocalDateTime localDateTime = localDate.atStartOfDay();

        return Timestamp.valueOf(localDateTime);
    }
}
