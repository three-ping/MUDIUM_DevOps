package com.threeping.mudium.musical.service;


import com.threeping.mudium.musical.dto.MusicalDTO;
import com.threeping.mudium.musical.dto.MusicalListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface MusicalService {
    MusicalDTO findMusicalDetail(Long musicId);

    Page<MusicalListDTO> findByName(String title,Pageable pageable);

    MusicalDTO findMusicalDetailByName(String title);
}
