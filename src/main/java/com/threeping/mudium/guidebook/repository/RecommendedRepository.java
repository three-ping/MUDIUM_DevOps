package com.threeping.mudium.guidebook.repository;

import com.threeping.mudium.guidebook.entity.RecommendedMusical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendedRepository extends JpaRepository<RecommendedMusical,Long> {
}
