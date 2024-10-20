package com.threeping.mudium.boardlike.repository;

import com.threeping.mudium.boardlike.aggregate.entity.BoardLike;
import com.threeping.mudium.boardlike.aggregate.entity.BoardLikePK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLike, BoardLikePK> {
}
