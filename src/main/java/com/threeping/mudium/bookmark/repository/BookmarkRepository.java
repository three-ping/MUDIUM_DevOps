package com.threeping.mudium.bookmark.repository;

import com.threeping.mudium.bookmark.entity.Bookmark;
import com.threeping.mudium.bookmark.entity.BookmarkPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkPK> {
    List<Bookmark> findAllByUserId ( Long userId );

    List<Bookmark> findByUserId(Long userId);
}
