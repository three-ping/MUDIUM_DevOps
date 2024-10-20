package com.threeping.mudium.boardcomment.repository;

import com.threeping.mudium.board.aggregate.enumerate.ActiveStatus;
import com.threeping.mudium.boardcomment.aggregate.BoardComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCommentRepository extends JpaRepository<BoardComment,Long> {
    Page<BoardComment> findByBoardId(Long boardId,Pageable pageable);
}
