package com.threeping.mudium.musicalboard.service;

import com.threeping.mudium.musicalboard.dto.MusicalPostDTO;
import com.threeping.mudium.musicalboard.dto.MusicalPostListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MusicalBoardService {

    Page<MusicalPostListDTO> findAllPost(Long musicalId, Pageable pageable);

    MusicalPostDTO findPost(Long musicalPostId);

    void createPost(Long musicalId, Long userId, MusicalPostDTO postDTO);

    void updatePost(Long musicalPostId, Long userId, MusicalPostDTO postDTO);

    void deletePost(Long musicalPostId, Long userId);

    boolean existingCheck(Long postId);
}
