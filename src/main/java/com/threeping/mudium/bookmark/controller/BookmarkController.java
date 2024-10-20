package com.threeping.mudium.bookmark.controller;

import com.threeping.mudium.bookmark.dto.BookmarkAndMusicalVO;
import com.threeping.mudium.bookmark.dto.BookmarkRequestDTO;
import com.threeping.mudium.bookmark.dto.BookmarkResponseDTO;
import com.threeping.mudium.bookmark.entity.Bookmark;
import com.threeping.mudium.bookmark.service.BookmarkService;
import com.threeping.mudium.common.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmark")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @Autowired
    public BookmarkController ( BookmarkService bookmarkService ) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping("/add")
    public ResponseDTO<BookmarkResponseDTO> addBookmark ( @RequestBody BookmarkRequestDTO bookmarkRequestDTO ) {
        Bookmark bookmark = bookmarkService.addBookmark ( bookmarkRequestDTO.getUserId (), bookmarkRequestDTO.getMusicalId () );
        BookmarkResponseDTO bookmarkResponseDTO = new BookmarkResponseDTO (
                bookmark.getUserId (),
                bookmark.getMusicalId ());
        return ResponseDTO.ok (bookmarkResponseDTO);
//        return ResponseEntity.status ( HttpStatus.CREATED).body(bookmarkResponseDTO);
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deleteBookmark ( @RequestBody BookmarkRequestDTO bookmarkRequestDTO ) {
        bookmarkService.deleteBookmark ( bookmarkRequestDTO.getUserId (), bookmarkRequestDTO.getMusicalId () );
        return ResponseEntity.noContent ().build ();
    }

    @GetMapping("/{userId}")
    public ResponseDTO<List<BookmarkResponseDTO>> findBookmarkByUserId(@PathVariable Long userId) {
        List<BookmarkResponseDTO> bookmarks = bookmarkService.findBookmarkByUserId ( userId );
        return ResponseDTO.ok ( bookmarks);  // 삭제 후 목록 반환
    }

    @GetMapping("/users/{userId}")
    public ResponseDTO<List<BookmarkAndMusicalVO>> findBookmarksWithMusicalInfoByUserId(@PathVariable Long userId) {
        List<BookmarkAndMusicalVO> bookmarksWithMusicalInfo = bookmarkService.findBookmarksWithMusicalInfoByUserId(userId);
        return ResponseDTO.ok(bookmarksWithMusicalInfo);
    }
}
