package com.threeping.mudium.musical.repository;

import com.threeping.mudium.musical.aggregate.Musical;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MusicalRepository extends JpaRepository<Musical, Long> {
    @Query(value = "SELECT * FROM TBL_MUSICAL_INFO m WHERE m.title = :title", nativeQuery = true)
    Optional<Musical> findMusicalByExactTitle(@Param("title") String title);
    Optional<Musical> findMusicalByMusicalId(Long musicalId);
    Page<Musical> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
