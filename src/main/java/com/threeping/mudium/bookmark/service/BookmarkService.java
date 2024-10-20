package com.threeping.mudium.bookmark.service;

import com.threeping.mudium.bookmark.dto.BookmarkAndMusicalVO;
import com.threeping.mudium.bookmark.dto.BookmarkRequestDTO;
import com.threeping.mudium.bookmark.dto.BookmarkResponseDTO;
import com.threeping.mudium.bookmark.entity.Bookmark;

import java.util.List;

public interface BookmarkService {

    public Bookmark addBookmark ( Long userId, Long musicalId ) ;

    public void deleteBookmark(Long userId, Long musicalId );

    public List<BookmarkResponseDTO> findBookmarkByUserId ( Long userId );


    List<BookmarkAndMusicalVO> findBookmarksWithMusicalInfoByUserId(Long userId);
}
