package com.threeping.mudium.musicalboard.repository;

import com.threeping.mudium.musicalboard.aggregate.ActiveStatus;
import com.threeping.mudium.musicalboard.aggregate.MusicalPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MusicalBoardRepository extends JpaRepository<MusicalPost, Long> {
    @Query("SELECT mp, (SELECT u.nickname FROM UserEntity u WHERE u.userId = mp.userId) as writerNickname " +
            "FROM musicalPost mp " +
            "WHERE mp.musicalId = :musicalId AND mp.activeStatus = :activeStatus")
    Page<Object[]> findAllByMusicalIdAndActiveStatus(Long musicalId, ActiveStatus activeStatus, Pageable pageable);

    Optional<MusicalPost> findMusicalPostByMusicalPostIdAndUserIdAndActiveStatus(Long musicalPostId, Long userId, ActiveStatus activeStatus);

    Optional<MusicalPost> findMusicalPostByMusicalPostIdAndActiveStatus(Long musicalPostId, ActiveStatus activeStatus);
}
