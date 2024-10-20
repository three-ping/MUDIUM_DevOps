package com.threeping.mudium.guidebook.service.ettiquette;

import com.threeping.mudium.guidebook.dto.EtiquetteRequestDTO;
import com.threeping.mudium.guidebook.entity.Etiquette;
import com.threeping.mudium.guidebook.repository.EtiquetteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class EtiquetteServiceImpl implements EtiquetteService {
    private final EtiquetteRepository etiquetteRepository;

    @Autowired
    public EtiquetteServiceImpl ( EtiquetteRepository etiquetteRepository ) {
        this.etiquetteRepository = etiquetteRepository;
    }

    @Override
    public Etiquette createEtiquette ( EtiquetteRequestDTO etiquetteRequestDTO ) {
        Etiquette etiquette = Etiquette.builder ()
                .etiquette ( etiquetteRequestDTO.getEtiquette () )
                .etiquetteDescription ( etiquetteRequestDTO.getEtiquetteDescription () )
                .userId ( etiquetteRequestDTO.getUserId () )
                .build ();
        return etiquetteRepository.save ( etiquette );
    }

    @Override
    public void updateEtiquette ( EtiquetteRequestDTO etiquetteRequestDTO, Long etiquetteId ) {
        Optional<Etiquette> optionalEtiquette = etiquetteRepository.findById ( etiquetteId );

        if (optionalEtiquette.isPresent ()) {
            Etiquette etiquette = optionalEtiquette.get ();
            etiquette.setEtiquette ( etiquetteRequestDTO.getEtiquette () );
            etiquette.setEtiquetteDescription ( etiquetteRequestDTO.getEtiquetteDescription () );

            etiquetteRepository.save ( etiquette );
        } else {
            throw new EntityNotFoundException ("수정할 관람 매너가 없습니다!");
        }
    }

    @Override
    public List<EtiquetteRequestDTO> findEtiquettesList () {

        List<Etiquette> allEtiquette = etiquetteRepository.findAll ();
        if (allEtiquette.isEmpty ()) {
            System.out.println ("관람 매너 안내가 없습니다.");
            return Collections.emptyList();
        }

        List<EtiquetteRequestDTO> etiquetteList = new ArrayList<>();
        for (Etiquette etiquette : allEtiquette) {
            EtiquetteRequestDTO etiquetteRequestDTO = EtiquetteRequestDTO.builder ()
                    .etiquette ( etiquette.getEtiquette() )
                    .etiquetteDescription ( etiquette.getEtiquetteDescription () )
                    .userId ( etiquette.getUserId () )
                    .build ();

            etiquetteList.add ( etiquetteRequestDTO );
        }
        return etiquetteList;
    }

    @Override
    public EtiquetteRequestDTO findByEtiquetteId ( Long etiquetteId ) {
        Optional<Etiquette> etiquette = etiquetteRepository.findById ( etiquetteId );
        Etiquette etiquettes = etiquette.get ();

        return EtiquetteRequestDTO.builder ()
                .etiquette ( etiquettes.getEtiquette () )
                .etiquetteDescription ( etiquettes.getEtiquetteDescription () )
                .build ();
    }
}

