    package com.threeping.mudium.scope.service;

    import com.threeping.mudium.scope.aggregate.entity.ScopeEntity;
    import com.threeping.mudium.scope.aggregate.entity.ScopeId;
    import com.threeping.mudium.scope.dto.AverageScopeDTO;
    import com.threeping.mudium.scope.dto.ScopeDTO;
    import com.threeping.mudium.scope.repository.ScopeRepository;
    import com.threeping.mudium.scope.vo.ScopeVO;
    import com.threeping.mudium.user.aggregate.dto.UserDTO;
    import com.threeping.mudium.user.aggregate.entity.UserEntity;
    import com.threeping.mudium.user.service.UserService;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.sql.Timestamp;
    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.stream.Collectors;

    @Service
    public class ScopeServiceImpl implements ScopeService {

        private final ScopeRepository scopeRepository;
        private final UserService userService;

        public ScopeServiceImpl(ScopeRepository scopeRepository, UserService userService) {
            this.scopeRepository = scopeRepository;
            this.userService = userService;
        }

        @Override
        public ScopeDTO findMyScope(Long musicalId, Long userId) {
            ScopeEntity entity = scopeRepository.findScopeByMusicalIdAndUserId(musicalId, userId)
                    .orElse(null);
            ScopeDTO scopeDTO = new ScopeDTO();
            scopeDTO.setUserId(userId);
            scopeDTO.setMusicalId(musicalId);

            if (entity != null) {
                scopeDTO.setScope(entity.getScope());
            } else {
                scopeDTO.setScope(0D);
            }

            return scopeDTO;
        }

        @Override
        @Transactional
        public List<ScopeDTO> findAllScopesByMusicalId(Long musicalId) {
            List<ScopeEntity> entityList = scopeRepository.findAllScopeByMusicalId(musicalId);

            // 특정 뮤지컬에 대한 모든 별점을 보며울 때 줄 거 = 뮤지컬 id, user 정보(닉네임), 별점
            List<ScopeDTO> dtoList = entityList.stream()
                    .map(scopeDTO -> {
                        ScopeDTO dto = new ScopeDTO();
                        dto.setMusicalId(scopeDTO.getMusicalId());
                        dto.setScope(scopeDTO.getScope());
                        dto.setNickName(scopeDTO.getUserNickname());
                        return dto;
                    }).collect(Collectors.toList());

            return dtoList;
        }

        // 평균 별점 계산 서비스 메서드
        // 뮤지컬 번호로 별점를 다 가져와서 계산(평균 별점은 소수점 한 자리까지 제공하고, 추가 평가한 사람 수를 만명 단위로 자르고 '명'을 붙여줍니다.
        @Override
        public AverageScopeDTO calculateAverageScope(Long musicalId) {
            List<ScopeEntity> entityList = scopeRepository.findAllScopeByMusicalId(musicalId);
            Double sum = 0D;
            Long peopleCount = Long.valueOf(entityList.size());
            for (ScopeEntity e : entityList) {
                sum += Double.valueOf(e.getScope());
            }
            String people = formatPeopleCount(peopleCount);
            Double average = peopleCount > 0 ? sum / peopleCount : 0;
            average = Math.round(average * 10.0) / 10.0;
            AverageScopeDTO dto = new AverageScopeDTO();
            dto.setMusicalId(musicalId);
            dto.setScope(average);
            dto.setPeople(people);

            return dto;
        }

        private String formatPeopleCount(long count) {
            if (count < 10000) {
                return count + "명";
            } else {
                double manCount = count / 10000.0;
                // 소수점 한 자리까지 반올림
                manCount = Math.round(manCount * 10.0) / 10.0;
                return manCount + "만명";
            }
        }

        //        // 별점 추가
//        @Override
//        public ScopeEntity createScope(ScopeEntity scopeEntity) {
//            scopeEntity.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
//            return scopeRepository.save(scopeEntity);
//        }
//
//        // 별점 수정
//        @Override
//        public ScopeEntity updateScope(ScopeEntity scopeEntity) {
//            scopeEntity.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
//            return scopeRepository.save(scopeEntity);
//        }

        // 별점 추가/수정 로직
        @Override
        public ScopeEntity createOrUpdateScope(Long musicalId, Long userId, ScopeVO scopeVO) {
            ScopeId scopeId = new ScopeId(musicalId, userId);
//            UserEntity user = userService.getUserByUserId(userId);
            // 기존 별점 존재 여부 확인
            ScopeEntity existingScope = scopeRepository.findById(scopeId).orElse(null);
            UserDTO user = userService.findByUserId(userId);

            if (existingScope != null) {
                // 별점이 이미 존재하는 경우 -> 수정
                existingScope.setScope(scopeVO.getScope());
                existingScope.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
                return scopeRepository.save(existingScope);
            } else {
                // 별점이 없는 경우 -> 새로 추가
                ScopeEntity scopeEntity = new ScopeEntity();
                scopeEntity.setMusicalId(musicalId);
                scopeEntity.setScope(scopeVO.getScope());
                scopeEntity.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                scopeEntity.setUserId(userId);
                scopeEntity.setUserNickname(user.getUserName());
                return scopeRepository.save(scopeEntity);
            }

        // 별점 삭제(일단 뮤지컬 id랑 회원 id로 삭제)
//        @Override
//        @Transactional
//        public void deleteScope(Long musicalx   Id, Long userId) {
//            scopeRepository.deleteScopeByMusicalIdAndUserId(musicalId, userId);
//        }
        }
        @Override
        @Transactional public void deleteScope(Long musicalId, Long userId) {
                scopeRepository.deleteScopeByMusicalIdAndUserId(musicalId, userId);
            }

        @Override
        public List<ScopeEntity> findScopesByUserId(Long userId) {
            return scopeRepository.findByUserId(userId);
        }
    }
