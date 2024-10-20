package com.threeping.mudium.guidebook.repository;

import com.threeping.mudium.guidebook.entity.MusicalTerms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermsRepository extends JpaRepository<MusicalTerms, Long> {
}
