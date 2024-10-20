package com.threeping.mudium.scope.service;

import com.threeping.mudium.scope.aggregate.entity.ScopeEntity;
import com.threeping.mudium.scope.aggregate.entity.ScopeId;
import com.threeping.mudium.scope.dto.AverageScopeDTO;
import com.threeping.mudium.scope.repository.ScopeRepository;
import com.threeping.mudium.scope.vo.ScopeVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional  // 테스트가 끝나면 데이터가 롤백되도록 처리
@Slf4j
class ScopeServiceImplTests {

    @Autowired
    private ScopeServiceImpl scopeService;

    @Autowired
    private ScopeRepository scopeRepository;

    private ScopeEntity scopeEntity;

    @Test
    public void testCreateScope() {
        // DB에 이미 존재하는 회원 ID와 뮤지컬 ID 사용
        Long existingUserId = 1L;
        Long existingMusicalId = 1L;
        ScopeVO scopeVO = new ScopeVO(5D);


        // 새로운 별점 추가 테스트
        ScopeEntity savedScope = scopeService.createOrUpdateScope(existingMusicalId, existingUserId, scopeVO);

        // 별점이 정상적으로 추가되었는지 확인
        assertNotNull(savedScope);
        assertEquals(5, savedScope.getScope());
    }

    @Test
    public void testUpdateScope() {
        // 기존에 별점이 있는 경우 수정 테스트
        ScopeEntity existingScope = new ScopeEntity(1L, 1L, 4D, Timestamp.valueOf(LocalDateTime.now()), null, "닉네임");
        scopeRepository.save(existingScope);

        // 기존 별점을 5로 수정
        ScopeEntity result = scopeService.createOrUpdateScope(1L, 1L, new ScopeVO(4D));

        // 별점이 정상적으로 수정되었는지 확인
        assertNotNull(result);
        assertEquals(5, result.getScope());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    public void testDeleteScope() {
        // 추가된 별점을 삭제
        scopeService.deleteScope(1L, 1L);

        // 삭제 후 해당 별점이 더 이상 존재하지 않는지 확인
        boolean isDeleted = scopeRepository.findById(new ScopeId(1L, 1L)).isEmpty();
        assertTrue(isDeleted);  // 별점이 삭제되었는지 확인
    }

    @DisplayName("평균 별점 조회")
    @Test
    void testAverageScope() {
        // Given
        Long musicalId = 1L;

        // When
        AverageScopeDTO dto = scopeService.calculateAverageScope(musicalId);
        log.info(dto.toString());

        // Then
        assertNotNull(dto);
        assertTrue(dto.getMusicalId().equals(musicalId), "조회된 평균 별점의 뮤지컬 번호 확인");
    }
}