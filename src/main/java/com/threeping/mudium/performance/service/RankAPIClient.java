package com.threeping.mudium.performance.service;


import com.threeping.mudium.common.exception.CommonException;
import com.threeping.mudium.common.exception.ErrorCode;
import com.threeping.mudium.musical.service.JAXBManager;
import com.threeping.mudium.performance.dto.RankResponse;
import jakarta.xml.bind.JAXBException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class RankAPIClient {

    @Value("${kopis.api.key}")
    private String apiKey;

    @Value("${kopis.api.rankurl}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public RankAPIClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public  RankResponse fetchMonthRank() {
        String Date = timeConverter(LocalDateTime.now());
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("service", apiKey)
                .queryParam("ststype", "month")
                .queryParam("date", Date)
                .queryParam("catecode", "GGGA")
                .toUriString();

        RankResponse rankResponse = null;
        try {
            String response = restTemplate.getForObject(url, String.class);
            log.info("정보 확인: " + response);
            rankResponse = parseXmlResponse(response);
            log.info("매핑된 객체 확인: " + rankResponse.getBaseDate() + "<- baseDate");
            log.info("item 하나 확인(제목): " +rankResponse.getRankItems().get(0).getTitle());
        } catch (RestClientException e) {
            throw new CommonException(ErrorCode.JAXB_CONTEXT_ERROR);
        }
        return rankResponse;
    }

    public RankResponse fetchDayRank() {
        String Date = timeConverter(LocalDateTime.now());
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("service", apiKey)
                .queryParam("ststype", "day")
                .queryParam("date", Date)
                .queryParam("catecode", "GGGA")
                .toUriString();
        log.info("일별 순위 url end point: " + url);

        RankResponse rankResponse = null;
        try {
            String response = restTemplate.getForObject(url, String.class);
            log.info("일별 순위 정보 확인: " + response);
            rankResponse = parseXmlResponse(response);
        } catch (RestClientException e) {
            throw new CommonException(ErrorCode.JAXB_CONTEXT_ERROR);
        }
        return rankResponse;
    }

    private RankResponse parseXmlResponse(String response) {
        try {
            RankResponse rankResponse = JAXBManager.getInstance().unmarshalRank(response, RankResponse.class);
            return rankResponse;
        } catch (JAXBException e) {
            throw new CommonException(ErrorCode.JAXB_CONTEXT_ERROR);
        }
    }

    private String timeConverter(LocalDateTime time) {
        LocalDateTime previousDay = time.minusDays(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String date = previousDay.format(formatter);

        log.info("변환된 date 확인 (하루 전): {}", date);
        return date;
    }
}
