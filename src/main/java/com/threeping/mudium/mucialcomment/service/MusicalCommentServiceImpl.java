package com.threeping.mudium.mucialcomment.service;

import com.threeping.mudium.common.exception.CommonException;
import com.threeping.mudium.common.exception.ErrorCode;
import com.threeping.mudium.mucialcomment.aggregate.ActiveStatus;
import com.threeping.mudium.mucialcomment.aggregate.MusicalComment;
import com.threeping.mudium.mucialcomment.dto.MusicalCommentDTO;
import com.threeping.mudium.mucialcomment.repository.MusicalCommentRepository;
import com.threeping.mudium.musicalboard.aggregate.MusicalPost;
import com.threeping.mudium.musicalboard.service.MusicalBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MusicalCommentServiceImpl implements MusicalCommentService {

    private final MusicalCommentRepository musicalCommentRepository;
    private final MusicalBoardService musicalBoardService;

    @Autowired
    public MusicalCommentServiceImpl(MusicalCommentRepository musicalCommentRepository, MusicalBoardService musicalBoardService) {
        this.musicalCommentRepository = musicalCommentRepository;
        this.musicalBoardService = musicalBoardService;
    }

    @Override
    public List<MusicalCommentDTO> findComment(Long postId) {
        List<Object[]> results = musicalCommentRepository.findAllByMusicalPostId(postId);

        List<MusicalCommentDTO> dtoList = results.stream()
                .map(result -> {
                    MusicalComment comment = (MusicalComment) result[0];
                    String nickName = (String) result[1];

                    MusicalCommentDTO dto = new MusicalCommentDTO();
                    dto.setCommentId(comment.getMusicalBoardCommentId());
                    dto.setPostId(comment.getMusicalPostId());
                    if(comment.getActiveStatus().equals(ActiveStatus.INACTIVE)) {
                        dto.setContent("삭제된 댓글입니다.");
                        return dto;
                    }
                    dto.setContent(comment.getContent());
                    dto.setNickName(nickName);
                    dto.setCreatedAt(timeConverter(comment.getCreatedAt()));
                    dto.setUpdatedAt(timeConverter(comment.getUpdatedAt()));
                    return dto;
                }).collect(Collectors.toList());

        return dtoList;
    }

    @Override
    public void createComment(Long userId, MusicalCommentDTO commentDTO) {
       if(musicalBoardService.existingCheck(commentDTO.getPostId()) == false)
           throw new CommonException(ErrorCode.NOT_FOUND_MUSICAL_BOARD);

        MusicalComment newComment = new MusicalComment();
        if(commentDTO.getContent().trim().isEmpty()) {
            throw new CommonException(ErrorCode.MISSING_REQUIRED_CONTENT);
        }
        newComment.setMusicalPostId(commentDTO.getPostId());
        newComment.setCreatedAt(Timestamp.from(Instant.now()));
        newComment.setUpdatedAt(Timestamp.from(Instant.now()));
        newComment.setContent(commentDTO.getContent());
        newComment.setUserId(userId);

        musicalCommentRepository.save(newComment);
    }

    @Override
    public void updateComment(Long userId, MusicalCommentDTO commentDTO) {

        String content = commentDTO.getContent();
        if(content.trim().isEmpty()) {
            throw new CommonException(ErrorCode.MISSING_REQUIRED_CONTENT);
        }
        Long commentId = commentDTO.getCommentId();
        MusicalComment beforeComment = musicalCommentRepository.
                findMusicalCommentByMusicalBoardCommentIdAndUserIdAndActiveStatus(
                        commentId, userId, ActiveStatus.ACTIVE)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_COMMENT));

        beforeComment.setContent(content);
        beforeComment.setUpdatedAt(Timestamp.from(Instant.now()));

        musicalCommentRepository.save(beforeComment);
    }

    @Override
    public void deleteComment(Long userId, MusicalCommentDTO commentDTO) {
        Long commentId = commentDTO.getCommentId();
        MusicalComment existingComment = musicalCommentRepository.
                findMusicalCommentByMusicalBoardCommentIdAndUserIdAndActiveStatus(
                        commentId, userId, ActiveStatus.ACTIVE)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_COMMENT));

        existingComment.setActiveStatus(ActiveStatus.INACTIVE);

        musicalCommentRepository.save(existingComment);
    }

    @Override
    public boolean existingCheck(Long commentId) {
        return musicalCommentRepository.findMusicalCommentByMusicalBoardCommentId(commentId).isPresent();
    }

    private String timeConverter(Timestamp date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}
