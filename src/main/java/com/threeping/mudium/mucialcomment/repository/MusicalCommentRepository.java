package com.threeping.mudium.mucialcomment.repository;

import com.threeping.mudium.mucialcomment.aggregate.ActiveStatus;
import com.threeping.mudium.mucialcomment.aggregate.MusicalComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MusicalCommentRepository extends JpaRepository<MusicalComment, Long> {
    @Query("SELECT mc, (SELECT u.nickname FROM UserEntity u WHERE u.userId = mc.userId) as nickName " +
            "FROM MusicalCommentEntity mc WHERE mc.musicalPostId = :musicalPostId")
    List<Object[]> findAllByMusicalPostId(Long musicalPostId);

    Optional<MusicalComment> findMusicalCommentByMusicalBoardCommentIdAndUserIdAndActiveStatus(
            Long musicalBoardCommentId, Long userId, ActiveStatus activeStatus);

    Optional<MusicalComment> findMusicalCommentByMusicalBoardCommentId(Long musicalBoardCommentId);
}
