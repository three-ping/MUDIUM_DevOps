package com.threeping.mudium.scope.service;

import com.threeping.mudium.scope.aggregate.entity.ScopeEntity;
import com.threeping.mudium.scope.dto.AverageScopeDTO;
import com.threeping.mudium.scope.dto.ScopeDTO;
import com.threeping.mudium.scope.vo.ScopeVO;

import java.util.List;

public interface ScopeService {

//    ScopeEntity createScope(ScopeEntity scopeEntity);
//
//    ScopeEntity updateScope(ScopeEntity scopeEntity);
    ScopeEntity createOrUpdateScope(Long musicalId, Long userId, ScopeVO scopeVO);

    void deleteScope(Long musicalId, Long userId);

    List<ScopeDTO> findAllScopesByMusicalId(Long musicalId);

    AverageScopeDTO calculateAverageScope(Long musicalId);

    List<ScopeEntity> findScopesByUserId(Long userId);

    ScopeDTO findMyScope(Long musicalId, Long userId);
}
