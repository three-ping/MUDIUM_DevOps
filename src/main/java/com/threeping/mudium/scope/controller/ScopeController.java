package com.threeping.mudium.scope.controller;

import com.threeping.mudium.common.ResponseDTO;
import com.threeping.mudium.scope.aggregate.entity.ScopeEntity;
import com.threeping.mudium.scope.dto.AverageScopeDTO;
import com.threeping.mudium.scope.dto.ScopeDTO;
import com.threeping.mudium.scope.service.ScopeService;
import com.threeping.mudium.scope.vo.ScopeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/scope")
public class ScopeController {

    private final ScopeService scopeService;

    @Autowired
    public ScopeController(ScopeService scopeService) {
        this.scopeService = scopeService;
    }

    @GetMapping("/{userId}/{musicalId}")
    public ResponseDTO<?> findMyScope (@PathVariable Long userId, @PathVariable Long musicalId) {
        ScopeDTO selectedScope = scopeService.findMyScope(musicalId, userId);
        
        return ResponseDTO.ok(selectedScope);
    }

    @PostMapping("/create/{userId}/{musicalId}")
    public ResponseEntity<ScopeEntity> createScope(@PathVariable("musicalId") Long musicalId,
                                                   @PathVariable("userId") Long userId,
                                                   @RequestBody ScopeVO scopeVO) {
        ScopeEntity createdRating = scopeService.createOrUpdateScope(musicalId, userId, scopeVO);
        return new ResponseEntity<>(createdRating, HttpStatus.CREATED);
    }

    // 별점 수정 (PathVariable 사용)
    @PutMapping("/update/{userId}/{musicalId}")
    public ResponseEntity<ScopeEntity> updateScope(@PathVariable("musicalId") Long musicalId,
                                                   @PathVariable("userId") Long userId,
                                                   @RequestBody ScopeVO scopeVO) {
        ScopeEntity updatedRating = scopeService.createOrUpdateScope(musicalId, userId, scopeVO);
        return new ResponseEntity<>(updatedRating, HttpStatus.OK);
    }


    // 별점 삭제 (PathVariable 사용)
    @DeleteMapping("/delete/{userId}/{musicalId}")
    public ResponseEntity<Void> deleteScope(@PathVariable("userId") Long userId,
                                            @PathVariable("musicalId") Long musicalId) {
        scopeService.deleteScope(musicalId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{musicalId}")
    public ResponseDTO<?> getAverageScope(@PathVariable("musicalId") Long musicalId) {
        AverageScopeDTO dto = scopeService.calculateAverageScope(musicalId);

        ResponseDTO<AverageScopeDTO> responseDTO = new ResponseDTO<>();
        responseDTO.setData(dto);
        responseDTO.setSuccess(true);
        responseDTO.setHttpStatus(HttpStatus.OK);
        return responseDTO;
    }
}
