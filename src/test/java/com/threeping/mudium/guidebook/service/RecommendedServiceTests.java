package com.threeping.mudium.guidebook.service;

import com.threeping.mudium.guidebook.dto.RecommendedRequestDTO;
import com.threeping.mudium.guidebook.entity.RecommendedMusical;
import com.threeping.mudium.guidebook.service.recommendedMusical.RecommendedService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RecommendedServiceTests {
    @Autowired
    RecommendedService recommendedService;

    @Test
    void testCreateRecommended(){

        RecommendedMusical recommendedMusical = recommendedService.createRecommended(new RecommendedRequestDTO("테스트 뮤지컬 1","테스트뮤지컬내용",1L));

        assertNotNull(recommendedMusical);


    }

    @Test
    void testGetRecommendedId(){

    }


}