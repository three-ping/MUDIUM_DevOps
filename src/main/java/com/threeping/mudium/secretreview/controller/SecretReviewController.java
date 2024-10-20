package com.threeping.mudium.secretreview.controller;

import com.threeping.mudium.common.ResponseDTO;
import com.threeping.mudium.secretreview.dto.SecretReviewRequestDTO;
import com.threeping.mudium.secretreview.dto.SecretReviewResponseDTO;
import com.threeping.mudium.secretreview.service.SecretReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/secretreview")
public class SecretReviewController {

    private final SecretReviewService secretReviewService;

    @Autowired
    public SecretReviewController(SecretReviewService secretReviewService) {
        this.secretReviewService = secretReviewService;
    }

    // 비밀리뷰 전체 조회
    @GetMapping("")
    private ResponseDTO<?> findSecretReviewByUserId(@RequestParam Long userId) {
        List<SecretReviewResponseDTO> secretReviewResponseDTO = secretReviewService.findSecretReviewByUserId(userId);

        return ResponseDTO.ok(secretReviewResponseDTO);
    }

    // 비밀리뷰 상세 조회
    @GetMapping("/{secretReviewId}")
    private ResponseDTO<?> findSecretReviewByUserIdAndSecretReviewId(@PathVariable Long secretReviewId,
                                                                     @RequestParam Long userId) {
        List<SecretReviewResponseDTO> secretReviewResponseDTO =
                secretReviewService.findSecretReviewByUserIdAndSecretReviewId(userId, secretReviewId);

        return ResponseDTO.ok(secretReviewResponseDTO);
    }

    // 비밀리뷰 작성
    @PostMapping("/{musicalId}")
    private ResponseDTO<?> createSecretReview(@PathVariable Long musicalId,
                                              @RequestBody SecretReviewRequestDTO secretReviewRequestDTO) {
        secretReviewService.createSecretReview(musicalId, secretReviewRequestDTO);

        return ResponseDTO.ok("비밀리뷰 등록 성공!!!");
    }

    // 비밀리뷰 수정
    @PutMapping("/{secretReviewId}")
    private ResponseDTO<?> updateSecretReview(@PathVariable Long secretReviewId,
                                              @RequestBody SecretReviewRequestDTO secretReviewRequestDTO) {
        secretReviewService.updateSecretReview(secretReviewId, secretReviewRequestDTO);

        return ResponseDTO.ok("비밀리뷰 수정 성공!!!");
    }

    // 비밀리뷰 삭제
    @DeleteMapping("/{secretReviewId}")
    private ResponseDTO<?> deleteSecretReview(@PathVariable Long secretReviewId,
                                              @RequestParam Long userId) {
        secretReviewService.deleteSecretReview(secretReviewId, userId);

        return ResponseDTO.ok("비밀리뷰 삭제 성공!!!");
    }
}
