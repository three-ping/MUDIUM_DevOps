package com.threeping.mudium.musicalreply.service;

import com.threeping.mudium.common.exception.CommonException;
import com.threeping.mudium.common.exception.ErrorCode;
import com.threeping.mudium.mucialcomment.service.MusicalCommentService;
import com.threeping.mudium.musicalreply.aggregate.ActiveStatus;
import com.threeping.mudium.musicalreply.aggregate.MusicalReply;
import com.threeping.mudium.musicalreply.dto.MusicalReplyDTO;
import com.threeping.mudium.musicalreply.repository.MusicalReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MusicalReplyServiceImpl implements MusicalReplyService {

    private final MusicalReplyRepository musicalReplyRepository;
    private final MusicalCommentService musicalCommentService;

    @Autowired
    public MusicalReplyServiceImpl(MusicalReplyRepository musicalReplyRepository, MusicalCommentService musicalCommentService) {
        this.musicalReplyRepository = musicalReplyRepository;
        this.musicalCommentService = musicalCommentService;
    }

    @Override
    public List<MusicalReplyDTO> findAllReply(Long commentId) {
        List<Object[]> results = musicalReplyRepository.findAllByCommentIdOrderByCreatedAtDesc(commentId);

        List<MusicalReplyDTO> repliesDTO = results.stream()
                .map(result -> {
                    MusicalReply reply = (MusicalReply) result[0];
                    String nickname = (String) result[1];

                    MusicalReplyDTO replyDTO = new MusicalReplyDTO();
                    replyDTO.setCommentId(reply.getCommentId());
                    replyDTO.setMusicalReplyId(reply.getMusicalReplyId());
                    if(reply.getActiveStatus().equals(ActiveStatus.INACTIVE)) {
                        replyDTO.setContent("삭제된 댓글입니다.");
                        return replyDTO;
                    }
                    replyDTO.setContent(reply.getContent());
                    replyDTO.setCreatedAt(timeConverter(reply.getCreatedAt()));
                    replyDTO.setUpdatedAt(timeConverter(reply.getUpdatedAt()));
                    replyDTO.setNickName(nickname);
                    return replyDTO;
                }).collect(Collectors.toList());


        return repliesDTO;
    }

    @Override
    public void createReply(Long userId, MusicalReplyDTO replyDTO) {
        if(musicalCommentService.existingCheck(replyDTO.getCommentId()) == false)
            throw new CommonException(ErrorCode.NOT_FOUND_COMMENT);

        MusicalReply newReply = new MusicalReply();

        if(replyDTO.getContent().trim().isEmpty()) {
            throw new CommonException(ErrorCode.MISSING_REQUIRED_CONTENT);
        }

        newReply.setContent(replyDTO.getContent());
        newReply.setCommentId(replyDTO.getCommentId());
        newReply.setUserId(userId);
        newReply.setCreatedAt(Timestamp.from(Instant.now()));
        newReply.setUpdatedAt(Timestamp.from(Instant.now()));

        musicalReplyRepository.save(newReply);
    }

    @Override
    public void updateReply(Long userId, MusicalReplyDTO replyDTO) {
        MusicalReply reply = musicalReplyRepository.findByMusicalReplyIdAndUserIdAndActiveStatus(
                replyDTO.getMusicalReplyId(), userId, ActiveStatus.ACTIVE)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_REPLY));

        if(replyDTO.getContent().trim().isEmpty()) {
            throw new CommonException(ErrorCode.MISSING_REQUIRED_CONTENT);
        }
        reply.setContent(replyDTO.getContent());
        reply.setUpdatedAt(Timestamp.from(Instant.now()));

        musicalReplyRepository.save(reply);
    }

    @Override
    public void deleteReply(Long userId, MusicalReplyDTO replyDTO) {
        MusicalReply reply = musicalReplyRepository.findByMusicalReplyIdAndUserIdAndActiveStatus(
                replyDTO.getMusicalReplyId(), userId, ActiveStatus.ACTIVE)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_REPLY));

        reply.setActiveStatus(ActiveStatus.INACTIVE);
        musicalReplyRepository.save(reply);
    }

    private String timeConverter(Timestamp date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}
