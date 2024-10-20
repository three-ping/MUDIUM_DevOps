package com.threeping.mudium.guidebook.service.recommendedMusical;

import com.threeping.mudium.guidebook.dto.RecommendedRequestDTO;
import com.threeping.mudium.guidebook.entity.RecommendedMusical;
import com.threeping.mudium.guidebook.repository.RecommendedRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
public class RecommendedServiceImpl implements RecommendedService {

    private final RecommendedRepository recommendedRepository;

    @Autowired
    public RecommendedServiceImpl(RecommendedRepository recommendedRepository) {
        this.recommendedRepository = recommendedRepository;
    }

    @Override
    @Transactional
    public RecommendedMusical createRecommended(RecommendedRequestDTO recommendedRequestDTO) {
        RecommendedMusical recommendedMusical = RecommendedMusical.builder()
                .musicalTitle(recommendedRequestDTO.getMusicalTitle())
                .musicalDescription(recommendedRequestDTO.getMusicalDescription())
                .userId(recommendedRequestDTO.getUserId())
                .build();

       return recommendedRepository.save(recommendedMusical);
    }

    @Override
    @Transactional
    public void updateRecommended(RecommendedRequestDTO recommendedRequestDTO,Long recommendedId) {
        Optional<RecommendedMusical> optionalRecommendedMusical = recommendedRepository.findById(recommendedId);

        if (optionalRecommendedMusical.isPresent()) {
            RecommendedMusical recommendedMusical = optionalRecommendedMusical.get();
            recommendedMusical.setMusicalTitle(recommendedRequestDTO.getMusicalTitle());
            recommendedMusical.setMusicalDescription(recommendedRequestDTO.getMusicalDescription());

             recommendedRepository.save(recommendedMusical);
        } else {
            throw new EntityNotFoundException("수정할 추천작이 없습니다!");
        }
    }

    //    전체 목록 조회
    @Transactional
    @Override
    public List<RecommendedRequestDTO> findRecommendedList() {

        List<RecommendedMusical> allRecommended = recommendedRepository.findAll();
        if (allRecommended.isEmpty()) {
            System.out.println("추천 작품이 없습니다.");
            return Collections.emptyList();
        }

        List<RecommendedRequestDTO> recommendedList = new ArrayList<>();

        for (RecommendedMusical recommendedMusical : allRecommended) {
            RecommendedRequestDTO recommendedRequestDTO = RecommendedRequestDTO.builder()
                    .musicalTitle(recommendedMusical.getMusicalTitle())
                    .musicalDescription(recommendedMusical.getMusicalDescription())
                    .userId ( recommendedMusical.getUserId() )
                    .build();

            recommendedList.add(recommendedRequestDTO);
        }

        return recommendedList;
    }

    //   추천 작품 조회하기
    @Override
    @Transactional
    public RecommendedRequestDTO findByRecommendedId(Long recommendedId) {
        Optional<RecommendedMusical> recommend = recommendedRepository.findById(recommendedId);
        RecommendedMusical recommendedMusical = recommend.get();

        return RecommendedRequestDTO.builder()
                .musicalTitle(recommendedMusical.getMusicalTitle())
                .musicalDescription(recommendedMusical.getMusicalDescription())
                .build();
    }
}
