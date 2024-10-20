package com.threeping.mudium.musical.service;


import com.threeping.mudium.common.exception.CommonException;
import com.threeping.mudium.common.exception.ErrorCode;
import com.threeping.mudium.musical.dto.MusicalItem;
import com.threeping.mudium.musical.dto.MusicalListResponse;
import com.threeping.mudium.performance.dto.PerformanceItem;
import com.threeping.mudium.performance.dto.PerformanceResponse;
import jakarta.xml.bind.JAXBException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MusicalAPIClient {

    @Value("${kopis.api.key}")
    private String apiKey;

    @Value("${kopis.api.url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public MusicalAPIClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<MusicalItem> fetchMusicalList() {
        log.info("Fetching musical list");
        // test를 위해서 줄입니다..
        String startDate = "20240101";
        String endDate = "20241231";
        log.info("endDate: {}", endDate);
        int cPage = 1;
        List<MusicalItem> allMusicals = new ArrayList<>();

        while(true) {
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("service", apiKey)
                    .queryParam("stdate", startDate)
                    .queryParam("eddate", endDate)
                    .queryParam("cpage", cPage + "")
                    .queryParam("rows", "200")
                    .queryParam("shcate", "GGGA")
                    .toUriString();

            log.info("요청 url 확인: {}", url);
            String response = restTemplate.getForObject(url, String.class);
            log.info("정보 확인: " + response);
            MusicalListResponse parsedResponse = parseXmlResponse(response);
            List<MusicalItem> musicalItems = parsedResponse.getMusicalItems();

            if (musicalItems == null || musicalItems.isEmpty()) {
                break; // 결과가 없으면 루프 종료
            }
            allMusicals.addAll(musicalItems);
            cPage++;
        }
        return allMusicals;
    }

    public PerformanceItem fetchPerformanceDetail(String mt20id) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .path("/{mt20id}")
                    .queryParam("service", apiKey)
                    .buildAndExpand(mt20id)
                    .toUriString();
            String response = restTemplate.getForObject(url, String.class);
            return parsePerformanceXmlResponse(response).getPerformanceItem();
        } catch (RestClientException e) {
            throw new CommonException(ErrorCode.API_DETAIL_BAD_REQUEST);
        }
    }

    public PerformanceResponse parsePerformanceXmlResponse(String response) {
        try {
            PerformanceResponse performanceResponse =
                    JAXBManager.getInstance().unmarshalPerformance(response, PerformanceResponse.class);
            return performanceResponse;
        } catch (JAXBException e) {
            throw new CommonException(ErrorCode.JAXB_CONTEXT_ERROR);
        }
    }

    private MusicalListResponse parseXmlResponse(String response) {
        try {
            return JAXBManager.getInstance().unmarshalMusical(response, MusicalListResponse.class);
        } catch (JAXBException e) {
            throw new CommonException(ErrorCode.JAXB_CONTEXT_ERROR);
        }
    }

    private String timeConverter(LocalDateTime now) {
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyyMMdd");
        String format = now.format(sdf);
        return format;
    }
}
