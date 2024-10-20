package com.threeping.mudium.guidebook.service.recommendedMusical;

import com.threeping.mudium.guidebook.dto.RecommendedRequestDTO;
import com.threeping.mudium.guidebook.entity.RecommendedMusical;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RecommendedService {
    @Transactional
    RecommendedMusical createRecommended(RecommendedRequestDTO recommendedRequestDTO);

    @Transactional
    void updateRecommended(RecommendedRequestDTO recommendedRequestDTO, Long recommendedId);

    //  추천 작품 조회하기
    @Transactional
    List<RecommendedRequestDTO> findRecommendedList();

    @Transactional
    RecommendedRequestDTO findByRecommendedId(Long recommendedId);
}
