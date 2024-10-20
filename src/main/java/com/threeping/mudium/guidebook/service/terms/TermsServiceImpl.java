package com.threeping.mudium.guidebook.service.terms;

import com.threeping.mudium.guidebook.dto.TermsRequestDTO;
import com.threeping.mudium.guidebook.entity.MusicalTerms;
import com.threeping.mudium.guidebook.repository.TermsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class TermsServiceImpl implements TermsService {

    private final TermsRepository termsRepository;

    @Autowired
    public TermsServiceImpl ( TermsRepository termsRepository ) {
        this.termsRepository = termsRepository;
    }

    @Override
    public MusicalTerms createTerms ( TermsRequestDTO termsRequestDTO ) {
        MusicalTerms terms = MusicalTerms.builder ()
                .terms ( termsRequestDTO.getTerms () )
                .termsDefinition ( termsRequestDTO.getTermsDefinition () )
                .userId ( termsRequestDTO.getUserId () )
                .build ();
        return termsRepository.save ( terms );
    }

    @Override
    public void updateTerms ( TermsRequestDTO termsRequestDTO, Long termId) {
        Optional<MusicalTerms> optionalMusicalTerms = termsRepository.findById ( termId );

        if (optionalMusicalTerms.isPresent ()) {
            MusicalTerms terms = optionalMusicalTerms.get ();
            terms.setTerms (termsRequestDTO.getTerms ());
            terms.setTermsDefinition ( termsRequestDTO.getTermsDefinition () );

            termsRepository.save ( terms );
        } else {
            throw new EntityNotFoundException ("수정할 뮤지컬 용어가 없습니다!");
        }
    }

    @Override
    public List<TermsRequestDTO> findTermsList () {
        List<MusicalTerms> allTerms = termsRepository.findAll ();
        if (allTerms.isEmpty ()){
            System.out.println ("뮤지컬 용어가 없습니다.");
            return Collections.emptyList();
        }
        List<TermsRequestDTO> termsList = new ArrayList<> ();
        for (MusicalTerms terms : allTerms){
            TermsRequestDTO termsRequestDTO = TermsRequestDTO.builder ()
                    .terms ( terms.getTerms() )
                    .termsDefinition ( terms.getTermsDefinition () )
                    .userId ( terms.getUserId () )
                    .build ();
            termsList.add ( termsRequestDTO );
        }
        return termsList;
    }

    @Override
    public TermsRequestDTO findByTermId ( Long termId ) {
        Optional<MusicalTerms> terms = termsRepository.findById ( termId );
        MusicalTerms musicalTerms = terms.get ();

        return TermsRequestDTO.builder ()
                .terms ( musicalTerms.getTerms () )
                .termsDefinition ( musicalTerms.getTermsDefinition () )
                .build ();
    }
}
