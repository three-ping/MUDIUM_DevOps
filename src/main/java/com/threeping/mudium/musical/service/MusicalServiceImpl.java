package com.threeping.mudium.musical.service;

import com.threeping.mudium.common.exception.CommonException;
import com.threeping.mudium.common.exception.ErrorCode;
import com.threeping.mudium.musical.aggregate.Musical;
import com.threeping.mudium.musical.dto.MusicalDTO;
import com.threeping.mudium.musical.dto.MusicalListDTO;
import com.threeping.mudium.musical.repository.MusicalRepository;
import com.threeping.mudium.scope.service.ScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class MusicalServiceImpl implements MusicalService {

    private final MusicalRepository musicalRepository;
    private final ScopeService scopeService;

    @Autowired
    public MusicalServiceImpl(MusicalRepository musicalRepository,
                              ScopeService scopeService) {
        this.musicalRepository = musicalRepository;
        this.scopeService = scopeService;
    }

    @Override
    public Page<MusicalListDTO> findByName(String title, Pageable pageable) {

        // jpa는 페이지 번호를 0부터 시작하기 때문에 0으로 pageNumber 설정
        int pageNumber = pageable.getPageNumber()<= 1 ? 0 : pageable.getPageNumber() - 1;
        int pageSize = pageable.getPageSize();
        Sort sort = Sort.by("viewCount").descending();
        Pageable musicalPageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Musical> musicalPage;

        if(title != null && !title.trim().isEmpty()) {
            // 제목으로 검색
            musicalPage = musicalRepository.findByTitleContainingIgnoreCase(title, musicalPageable);
        } else {
            // 아무것도 입력 안 하면 모두가 가져오기
            musicalPage = musicalRepository.findAll(musicalPageable);
        }

        // JPA가 페이징 쿼리를 실행할 때, 기본적으로 두 가지 쿼리를 실행:
        // a) 요청된 페이지의 데이터를 가져오는 쿼리
        // b) 전체 데이터의 개수를 세는 카운트 쿼리
        if(musicalPage.getTotalPages() < pageSize) {
            Pageable maxPageable = PageRequest.of(musicalPage.getTotalPages() - 1, pageSize, sort);
            if(title != null && !title.trim().isEmpty()) {
                musicalPage = musicalRepository.findByTitleContainingIgnoreCase(title, musicalPageable);
            } else {
                musicalPage = musicalRepository.findAll(maxPageable);
            }
        }

        List<Long> musicalIds = musicalPage.getContent().stream()
                .map(Musical::getMusicalId)
                .collect(Collectors.toList());

        List<MusicalListDTO> dtoList = musicalPage.getContent().stream()
                .map(musical -> {
                    MusicalListDTO dto = new MusicalListDTO();
                    dto.setMusicalId(musical.getMusicalId());
                    dto.setMusicalId(musical.getMusicalId());
                    dto.setPoster(musical.getPoster());
                    dto.setTitle(musical.getTitle());
                    return dto;
                }).collect(Collectors.toList());
        // 전체 데이터셋의 총 항목 수를 나타냄.
        //이 정보를 통해 Page 객체는 다음과 같은 중요한 메타데이터를 계산 가능:
        //a) 총 페이지 수 (getTotalPages())
        //b) 현재 페이지가 첫 페이지인지 (isFirst())
        //c) 현재 페이지가 마지막 페이지인지 (isLast())
        //d) 다음 페이지가 있는지 (hasNext())
        //e) 이전 페이지가 있는지 (hasPrevious())
        // 위의 정보를 활용해 client는 다음 페이지 버튼을 만들지 말지 같은 걸 구성가능
        Page<MusicalListDTO> dtoPage = new PageImpl<>(dtoList, pageable, musicalPage.getTotalElements());

        return dtoPage;
    }

    @Override
    public MusicalDTO findMusicalDetail(Long musicalId) {
        MusicalDTO musicalDTO = new MusicalDTO();
        Musical musical = musicalRepository.findMusicalByMusicalId(musicalId)
                .orElseThrow(() -> new CommonException(ErrorCode.INVALID_MUSICAL_ID));
        musicalDTO.setMusicalId(musical.getMusicalId());
        musicalDTO.setTitle(musical.getTitle());
        musicalDTO.setRating(musical.getRating());
        musicalDTO.setPoster(musical.getPoster());
        musicalDTO.setProduction(musical.getProduction());
        musicalDTO.setSynopsys(musical.getSynopsys());
        musicalDTO.setViewCount(musical.getViewCount() + 1);
        // 조회되면 조회 수 1 증가
        musical.setViewCount(musical.getViewCount() + 1);
        musicalRepository.save(musical);

        return musicalDTO;
    }

    @Override
    public MusicalDTO findMusicalDetailByName(String title) {
        Musical musical = musicalRepository.findMusicalByExactTitle(title)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_MUSICAL));
        MusicalDTO musicalDTO = new MusicalDTO();
        musicalDTO.setMusicalId(musical.getMusicalId());
        musicalDTO.setTitle(musical.getTitle());
        musicalDTO.setReviewVideos(musical.getReviewVideo());
        musicalDTO.setPoster(musical.getPoster());
        musicalDTO.setProduction(musical.getProduction());
        musicalDTO.setSynopsys(musical.getSynopsys());
        musicalDTO.setViewCount(musical.getViewCount());
        musicalDTO.setRating(musical.getRating());
        return musicalDTO;
    }
}
