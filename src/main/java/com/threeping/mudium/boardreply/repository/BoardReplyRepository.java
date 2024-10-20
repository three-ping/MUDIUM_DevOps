package com.threeping.mudium.boardreply.repository;

import com.threeping.mudium.board.aggregate.enumerate.ActiveStatus;
import com.threeping.mudium.boardreply.aggregate.entity.BoardReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardReplyRepository extends JpaRepository<BoardReply,Long> {

    List<BoardReply> findByBoardCommentIdAndActiveStatus(Long boardCommentId, ActiveStatus activeStatus);
}
