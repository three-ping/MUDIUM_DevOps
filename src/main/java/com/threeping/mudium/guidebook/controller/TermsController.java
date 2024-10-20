package com.threeping.mudium.guidebook.controller;

import com.threeping.mudium.common.ResponseDTO;
import com.threeping.mudium.guidebook.dto.TermsRequestDTO;
import com.threeping.mudium.guidebook.entity.MusicalTerms;
import com.threeping.mudium.guidebook.service.terms.TermsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/terms")
public class TermsController {

    private final TermsService termsService;

    @Autowired
    public TermsController ( TermsService termsService ) {
        this.termsService = termsService;
    }

    @PostMapping("/post")
    public ResponseDTO<?> createTerms ( @RequestBody TermsRequestDTO termsRequestDTO ){
        MusicalTerms createTerms = termsService.createTerms ( termsRequestDTO );
        return ResponseDTO.ok ( createTerms );
    }
    @PutMapping("update/{termId}")
    public ResponseDTO<?> modifyTerm(@PathVariable Long termId,
                                          @RequestBody TermsRequestDTO termsRequestDTO) {
        termsService.updateTerms ( termsRequestDTO, termId );
        return ResponseDTO.ok ( termId );
    }

    @GetMapping("")
    public ResponseDTO<?> findTermsList() {
        List<TermsRequestDTO> termsList = termsService.findTermsList ();
        return ResponseDTO.ok( termsList );
    }

    @GetMapping("/detail/{termId}")
    public ResponseDTO<?> findByTermId (@PathVariable Long termId) {
        return ResponseDTO.ok(termsService.findByTermId ( termId ));
    }
}
