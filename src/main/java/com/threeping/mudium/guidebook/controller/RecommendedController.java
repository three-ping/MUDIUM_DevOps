package com.threeping.mudium.guidebook.controller;

import com.threeping.mudium.common.ResponseDTO;
import com.threeping.mudium.guidebook.dto.RecommendedRequestDTO;
import com.threeping.mudium.guidebook.entity.RecommendedMusical;
import com.threeping.mudium.guidebook.service.recommendedMusical.RecommendedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommended-musical")
public class RecommendedController {

    private final RecommendedService recommendedService;

    @Autowired
    public RecommendedController ( RecommendedService recommendedService ) {
        this.recommendedService = recommendedService;
    }

    @PostMapping("/post")
    public ResponseDTO<?> createRecommended ( @RequestBody RecommendedRequestDTO recommendedRequestDTO ) {
        RecommendedMusical createRecommend = recommendedService.createRecommended ( recommendedRequestDTO );
        return ResponseDTO.ok ( createRecommend );
    }

    @PutMapping("/update/{recommendedId}")
    public ResponseDTO<?> modifyRecommended ( @PathVariable Long recommendedId,
                                              @RequestBody RecommendedRequestDTO recommendedRequestDTO ) {
        recommendedService.updateRecommended ( recommendedRequestDTO, recommendedId );

        return ResponseDTO.ok ( recommendedId );
    }

    @GetMapping("")
    public ResponseDTO<?> findRecommendedList () {
        List<RecommendedRequestDTO> recommendedList = recommendedService.findRecommendedList ();
        return ResponseDTO.ok ( recommendedList );
    }

    @GetMapping("/detail/{recommendedId}")
    public ResponseDTO<?> findByRecommendedId ( @PathVariable Long recommendedId ) {
        return ResponseDTO.ok ( recommendedService.findByRecommendedId ( recommendedId ) );
    }
}
