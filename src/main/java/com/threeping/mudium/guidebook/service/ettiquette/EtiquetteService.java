package com.threeping.mudium.guidebook.service.ettiquette;

import com.threeping.mudium.guidebook.dto.EtiquetteRequestDTO;
import com.threeping.mudium.guidebook.entity.Etiquette;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface EtiquetteService {
    Etiquette createEtiquette( EtiquetteRequestDTO etiquetteRequestDTO);

    void updateEtiquette(EtiquetteRequestDTO etiquetteRequestDTO, Long etiquetteId);

    List<EtiquetteRequestDTO> findEtiquettesList();

    EtiquetteRequestDTO findByEtiquetteId(Long etiquetteId);
}
