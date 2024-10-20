package com.threeping.mudium.musicalreply.repository;

import com.threeping.mudium.musicalreply.aggregate.ActiveStatus;
import com.threeping.mudium.musicalreply.aggregate.MusicalReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MusicalReplyRepository extends JpaRepository<MusicalReply, Long> {
    @Query("SELECT mr, (SELECT u.nickname FROM UserEntity u WHERE u.userId = mr.userId) as nickName " +
            "FROM MusicalReplyEntity mr WHERE mr.CommentId = :commentId")
    List<Object[]> findAllByCommentIdOrderByCreatedAtDesc(Long commentId);

    Optional<MusicalReply> findByMusicalReplyIdAndUserIdAndActiveStatus(Long replyId, Long userId, ActiveStatus activeStatus);
}
