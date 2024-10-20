package com.threeping.mudium.secretreview.service;

import com.threeping.mudium.common.exception.CommonException;
import com.threeping.mudium.common.exception.ErrorCode;
import com.threeping.mudium.musical.aggregate.Musical;
import com.threeping.mudium.musical.repository.MusicalRepository;
import com.threeping.mudium.review.repository.ReviewRepository;
import com.threeping.mudium.secretreview.aggregate.entity.ActiveStatus;
import com.threeping.mudium.secretreview.aggregate.entity.SecretReview;
import com.threeping.mudium.secretreview.dto.SecretReviewRequestDTO;
import com.threeping.mudium.secretreview.dto.SecretReviewResponseDTO;
import com.threeping.mudium.secretreview.repository.SecretReviewRepository;
import com.threeping.mudium.user.aggregate.entity.UserEntity;
import com.threeping.mudium.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SecretReviewServiceImpl implements SecretReviewService{

    private final SecretReviewRepository secretReviewRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final MusicalRepository musicalRepository;

    @Autowired
    public SecretReviewServiceImpl(SecretReviewRepository secretReviewRepository, ModelMapper modelMapper, ReviewRepository reviewRepository, UserRepository userRepository, MusicalRepository musicalRepository) {
        this.secretReviewRepository = secretReviewRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.musicalRepository = musicalRepository;
    }

    // 비밀리뷰 전체 조회
    @Override
    public List<SecretReviewResponseDTO> findSecretReviewByUserId(Long userId) {

        List<SecretReview> secretReviews = secretReviewRepository
                .findAllByUser_UserIdAndActiveStatus(userId, ActiveStatus.ACTIVE);

        List<SecretReviewResponseDTO> secretReviewResponseDTO = secretReviews.stream()
                .map(secretReview -> modelMapper.map(secretReview, SecretReviewResponseDTO.class))
                .collect(Collectors.toList());

        return secretReviewResponseDTO;
    }

    // 비밀리뷰 상세 조회
    @Override
    public List<SecretReviewResponseDTO> findSecretReviewByUserIdAndSecretReviewId(Long userId, Long secretReviewId) {

        List<SecretReview> secretReviews = secretReviewRepository
                .findByUser_UserIdAndSecretReviewIdAndActiveStatus(userId, secretReviewId, ActiveStatus.ACTIVE);

        List<SecretReviewResponseDTO> secretReviewResponseDTO = secretReviews.stream()
                .map(secretReview -> modelMapper.map(secretReview, SecretReviewResponseDTO.class))
                .collect(Collectors.toList());

        return secretReviewResponseDTO;
    }

    // 비밀리뷰 작성
    @Override
    public void createSecretReview(Long musicalId, SecretReviewRequestDTO secretReviewRequestDTO) {

        List<SecretReview> secretReviews = secretReviewRepository
                .findAllByUser_UserIdAndActiveStatus(secretReviewRequestDTO.getUserId(), ActiveStatus.ACTIVE);

        boolean hasActiveReview = secretReviews.stream()
                .anyMatch(secretReview -> secretReview.getMusical().getMusicalId().equals(musicalId));

        if (hasActiveReview) {
            throw new CommonException(ErrorCode.REVIEW_ALREADY_EXISTS);
        }

        UserEntity user = userRepository.findById(secretReviewRequestDTO.getUserId())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER_ID));
        Musical musical = musicalRepository.findById(musicalId)
                .orElseThrow(() -> new CommonException(ErrorCode.MUSICAL_NOT_FOUND));

        SecretReview newSecretReview = new SecretReview();
        newSecretReview.setContent(secretReviewRequestDTO.getContent());
        newSecretReview.setUser(user);
        newSecretReview.setMusical(musical);
        newSecretReview.setCreatedAt(Timestamp.from(Instant.now()));

        secretReviewRepository.save(newSecretReview);
    }

    // 비밀리뷰 수정
    @Override
    public void updateSecretReview(Long secretReviewId, SecretReviewRequestDTO secretReviewRequestDTO) {

        List<SecretReview> secretReviews = secretReviewRepository
                .findByUser_UserIdAndSecretReviewIdAndActiveStatus(
                        secretReviewRequestDTO.getUserId(), secretReviewId, ActiveStatus.ACTIVE);

        // 리뷰가 존재하지 않으면 처리
        if (secretReviews.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_REVIEW);
        }

        boolean checkUser = secretReviews.stream()
                .anyMatch(m -> m.getUser().getUserId().equals(secretReviewRequestDTO.getUserId()));

        if (!checkUser) {
            throw new CommonException(ErrorCode.WRONG_ENTRY_POINT);
        }

        SecretReview newSecretReview = secretReviews.get(0);
        newSecretReview.setContent(secretReviewRequestDTO.getContent());
        newSecretReview.setUpdatedAt(Timestamp.from(Instant.now()));

        secretReviewRepository.save(newSecretReview);
    }

    // 비밀리뷰 삭제
    @Override
    public void deleteSecretReview(Long secretReviewId, Long userId) {

        List<SecretReview> secretReviews = secretReviewRepository
                .findByUser_UserIdAndSecretReviewIdAndActiveStatus(
                        userId, secretReviewId, ActiveStatus.ACTIVE);

        if (secretReviews.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_REVIEW);
        }

        boolean checkUser = secretReviews.stream()
                .anyMatch(m -> m.getUser().getUserId().equals(userId));

        if (!checkUser) {
            throw new CommonException(ErrorCode.WRONG_ENTRY_POINT);
        }

        SecretReview newSecretReview = secretReviews.get(0);
        newSecretReview.deactivateReview();

        secretReviewRepository.save(newSecretReview);
    }
}
