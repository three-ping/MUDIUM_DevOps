package com.threeping.mudium.musicalboardlike.repository;

import com.threeping.mudium.musicalboardlike.aggregate.MusicalBoardLike;
import com.threeping.mudium.musicalboardlike.aggregate.MusicalBoardLikePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicalBoardLikeRepository extends JpaRepository<MusicalBoardLike, MusicalBoardLikePK> {
}
