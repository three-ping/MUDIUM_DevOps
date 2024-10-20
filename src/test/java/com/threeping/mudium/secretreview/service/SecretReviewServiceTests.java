package com.threeping.mudium.secretreview.service;

import com.threeping.mudium.secretreview.dto.SecretReviewRequestDTO;
import com.threeping.mudium.secretreview.dto.SecretReviewResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SecretReviewServiceTests {

    @Autowired
    SecretReviewService secretReviewService;

    @DisplayName("비밀리뷰 전체 조회한다.")
    @Test
    void findSecretReviewByUserIdAndActiveStatusTest() {
        // Given
        Long userId = 1L;

        // When
        List<SecretReviewResponseDTO> secretReviewResponseDTO =
                secretReviewService.findSecretReviewByUserId(userId);

        // Then
        assertNotNull(secretReviewResponseDTO);
        assertFalse(secretReviewResponseDTO.isEmpty());

        SecretReviewResponseDTO secretReview = secretReviewResponseDTO.get(0);
        assertEquals(userId, secretReview.getUserId());
    }

    @DisplayName("비밀리뷰 상세 조회한다.")
    @Test
    void findSecretReviewByUserIdAndSecretReviewIdAndActiveStatusTest() {
        // Given
        Long userId = 1L;
        Long secretReviewId = 1L;

        // When
        List<SecretReviewResponseDTO> secretReviewResponseDTO =
                secretReviewService.findSecretReviewByUserIdAndSecretReviewId(userId, secretReviewId);

        // Then
        assertNotNull(secretReviewResponseDTO);
        assertFalse(secretReviewResponseDTO.isEmpty());

        SecretReviewResponseDTO secretReview = secretReviewResponseDTO.get(0);
        assertEquals(userId, secretReview.getUserId());
        assertEquals(secretReviewId, secretReview.getSecretReviewId());
    }

    @DisplayName("비밀리뷰 작성을 한다.")
    @Test
    void createSecretReviewTest() {
        // Given
        Long musicalId = 4L;
        Long userId = 1L;
        String content = "작성 테스트 해봅니다~!";

        SecretReviewRequestDTO secretReviewRequestDTO = new SecretReviewRequestDTO(content, userId);

        // When
        assertDoesNotThrow(() -> secretReviewService.createSecretReview(musicalId, secretReviewRequestDTO));
    }

    @DisplayName("비밀리뷰 수정을 한다.")
    @Test
    void updateSecretReviewTest() {
        // Given
        Long secretReviewId = 19L;
        Long userId = 1L;
        String content = "수정 테스트 해볼까요~?";

        SecretReviewRequestDTO secretReviewRequestDTO = new SecretReviewRequestDTO(content, userId);

        // When
        assertDoesNotThrow(() -> secretReviewService.updateSecretReview(secretReviewId, secretReviewRequestDTO));
    }

    @DisplayName("비밀리뷰 삭제를 한다.")
    @Test
    void deleteSecretReviewTest() {
        // Given
        Long secretReviewId = 19L;
        Long userId = 1L;

        // When
        assertDoesNotThrow(() -> secretReviewService.deleteSecretReview(secretReviewId, userId));
    }

}