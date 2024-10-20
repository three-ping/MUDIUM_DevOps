package com.threeping.mudium.guidebook.service.terms;

import com.threeping.mudium.guidebook.dto.TermsRequestDTO;
import com.threeping.mudium.guidebook.entity.MusicalTerms;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface TermsService {

    MusicalTerms createTerms( TermsRequestDTO termsRequestDTO );

    void updateTerms(TermsRequestDTO termsRequestDTO, Long termId);

    List<TermsRequestDTO> findTermsList();

    TermsRequestDTO findByTermId(Long termId);
}
