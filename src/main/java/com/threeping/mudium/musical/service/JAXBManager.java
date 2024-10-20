package com.threeping.mudium.musical.service;

import com.threeping.mudium.common.exception.CommonException;
import com.threeping.mudium.common.exception.ErrorCode;
import com.threeping.mudium.musical.dto.MusicalItem;
import com.threeping.mudium.musical.dto.MusicalListResponse;
import com.threeping.mudium.performance.dto.PerformanceItem;
import com.threeping.mudium.performance.dto.PerformanceResponse;
import com.threeping.mudium.performance.dto.RankItem;
import com.threeping.mudium.performance.dto.RankResponse;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;

import java.io.StringReader;

import static jakarta.xml.bind.JAXB.unmarshal;

@Slf4j
public class JAXBManager {
    private static final JAXBManager INSTANCE = new JAXBManager();
    private final JAXBContext musicalContext;
    private final JAXBContext performanceContext;
    private final JAXBContext rankContext;

    private JAXBManager() {
        try {
            this.musicalContext = JAXBContext.newInstance(
                    MusicalListResponse.class,
                    MusicalItem.class);
            this.performanceContext = JAXBContext.newInstance(
                    PerformanceResponse.class,
                    PerformanceItem.class);
            this.rankContext = JAXBContext.newInstance(
                    RankResponse.class,
                    RankItem.class);
            log.info("JAXBManager 싱글톤 객체 생성 성공");
        } catch (JAXBException e) {
            throw new CommonException(ErrorCode.JAXB_MANAGER_ERROR);
        }
    }

    public static JAXBManager getInstance() {
        return INSTANCE;
    }

    // 흐름: 공연 리스트 매핑 객체와 공연 상세 정보 매핑 객체에 대한 context가 다르므로 unmarshal method에
    // unmarshaller 객체를 생성할 context도 넘겨줘서 각 각 고유 unmarshaller가 생기도록 함
    public <T> T unmarshalMusical(String xml, Class<T> clazz) throws JAXBException {
        return unmarshal(xml, clazz, musicalContext);
    }

    public <T> T unmarshalPerformance(String xml, Class<T> clazz) throws JAXBException {
        return unmarshal(xml, clazz, performanceContext);
    }

    public <T> T unmarshalRank(String xml, Class<T> clazz) throws JAXBException {
        return unmarshal(xml, clazz, rankContext);
    }

    private <T> T unmarshal(String xml, Class<T> clazz, JAXBContext context) throws JAXBException {
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return clazz.cast(unmarshaller.unmarshal(new StringReader(xml)));
    }
}
