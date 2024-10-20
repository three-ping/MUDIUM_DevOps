package com.threeping.mudium.board.repository;

import com.threeping.mudium.board.aggregate.enumerate.ActiveStatus;
import com.threeping.mudium.board.aggregate.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {
    Page<Board> findByActiveStatus(ActiveStatus activeStatus, Pageable pageable);
    Optional<Board> findByActiveStatusAndBoardId(ActiveStatus activeStatus, Long boardId);
    Optional<Board> findByActiveStatusAndBoardIdAndUser_UserId(ActiveStatus activeStatus, Long boardId, Long userId);
    Page<Board> findByTitleContaining(String title, Pageable pageable);
    Page<Board> findByContentContaining(String content, Pageable pageable);
    Page<Board> findByUser_NicknameContaining(String nickname, Pageable pageable);
}
