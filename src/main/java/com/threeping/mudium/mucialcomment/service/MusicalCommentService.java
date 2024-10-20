package com.threeping.mudium.mucialcomment.service;

import com.threeping.mudium.mucialcomment.dto.MusicalCommentDTO;

import java.util.List;

public interface MusicalCommentService {
    List<MusicalCommentDTO> findComment(Long postId);

    void createComment(Long userId, MusicalCommentDTO commentDTO);

    void updateComment(Long userId, MusicalCommentDTO commentDTO);

    void deleteComment(Long userId, MusicalCommentDTO commentDTO);

    boolean existingCheck(Long commentId);
}
