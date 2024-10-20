package com.threeping.mudium.musicalreply.service;

import com.threeping.mudium.musicalreply.dto.MusicalReplyDTO;

import java.util.List;

public interface MusicalReplyService {
    List<MusicalReplyDTO> findAllReply(Long commentId);

    void createReply(Long userId, MusicalReplyDTO replyDTO);

    void updateReply(Long userId, MusicalReplyDTO replyDTO);

    void deleteReply(Long userId, MusicalReplyDTO replyDTO);
}
