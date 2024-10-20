package com.threeping.mudium.guidebook.repository;

import com.threeping.mudium.guidebook.entity.Etiquette;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtiquetteRepository extends JpaRepository<Etiquette, Long> {
}
