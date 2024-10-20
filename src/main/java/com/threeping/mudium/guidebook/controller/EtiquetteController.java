package com.threeping.mudium.guidebook.controller;

import com.threeping.mudium.common.ResponseDTO;
import com.threeping.mudium.guidebook.dto.EtiquetteRequestDTO;
import com.threeping.mudium.guidebook.entity.Etiquette;
import com.threeping.mudium.guidebook.service.ettiquette.EtiquetteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/etiquette")
public class EtiquetteController {

    private final EtiquetteService etiquetteService;

    @Autowired
    public EtiquetteController ( EtiquetteService etiquetteService ) {
        this.etiquetteService = etiquetteService;
    }

    @PostMapping("/post")
    public ResponseDTO<?> createEtiquette ( @RequestBody EtiquetteRequestDTO etiquetteRequestDTO ){
        Etiquette createEtiquettes = etiquetteService.createEtiquette( etiquetteRequestDTO );
        return ResponseDTO.ok ( createEtiquettes );
    }

    @PutMapping("update/{etiquetteId}")
    public ResponseDTO<?> modifyEtiquette(@PathVariable Long etiquetteId,
                                          @RequestBody EtiquetteRequestDTO etiquetteRequestDTO) {
        etiquetteService.updateEtiquette ( etiquetteRequestDTO, etiquetteId );
        return ResponseDTO.ok ( etiquetteId );
    }

    @GetMapping("")
    public ResponseDTO<?> findEtiquetteList() {
        List<EtiquetteRequestDTO> etiquetteList = etiquetteService.findEtiquettesList ();
        return ResponseDTO.ok( etiquetteList );
    }

    @GetMapping("/detail/{etiquetteId}")
    public ResponseDTO<?> findByEtiquetteId (@PathVariable Long etiquetteId) {
        return ResponseDTO.ok(etiquetteService.findByEtiquetteId ( etiquetteId ));
    }
}
