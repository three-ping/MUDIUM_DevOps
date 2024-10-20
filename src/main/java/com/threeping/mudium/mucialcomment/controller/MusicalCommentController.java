package com.threeping.mudium.mucialcomment.controller;

import com.threeping.mudium.common.ResponseDTO;
import com.threeping.mudium.mucialcomment.dto.MusicalCommentDTO;
import com.threeping.mudium.mucialcomment.service.MusicalCommentService;
import com.threeping.mudium.mucialcomment.vo.MusicalCommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/musical-comment")
public class MusicalCommentController {

    private final MusicalCommentService musicalCommentService;

    @Autowired
    public MusicalCommentController(MusicalCommentService musicalCommentService) {
        this.musicalCommentService = musicalCommentService;
    }

    @GetMapping("/{postId}")
    public ResponseDTO<?> findMusicalComments(@PathVariable Long postId) {
        List<MusicalCommentDTO> commentList = musicalCommentService.findComment(postId);

        ResponseDTO<List<MusicalCommentDTO>> responseDTO = new ResponseDTO<>();
        responseDTO.setData(commentList);
        responseDTO.setHttpStatus(HttpStatus.OK);
        responseDTO.setSuccess(true);

        return responseDTO;
    }

    @PostMapping("/{postId}")
    public ResponseDTO<?> createMusicalComment(@PathVariable Long postId, @RequestBody MusicalCommentVO commentVO) {
        MusicalCommentDTO commentDTO = new MusicalCommentDTO();
        commentDTO.setPostId(postId);
        commentDTO.setContent(commentVO.getContent());

        musicalCommentService.createComment(commentVO.getUserId(), commentDTO);

        return ResponseDTO.ok("생성 성공");
    }

    @PutMapping("/{commentId}")
    public ResponseDTO<?> updateComment(@PathVariable Long commentId, @RequestBody MusicalCommentVO commentVO) {
        MusicalCommentDTO commentDTO = new MusicalCommentDTO();
        commentDTO.setContent(commentVO.getContent());
        commentDTO.setPostId(commentId);

        musicalCommentService.updateComment(commentVO.getUserId(), commentDTO);

        return ResponseDTO.ok("수정 성공");
    }

    @DeleteMapping("/{commentId}")
    public ResponseDTO<?> deleteComment(@PathVariable Long commentId, @RequestBody MusicalCommentVO commentVO) {
        MusicalCommentDTO commentDTO = new MusicalCommentDTO();
        commentDTO.setCommentId(commentId);

        musicalCommentService.deleteComment(commentVO.getUserId(), commentDTO);

        return ResponseDTO.ok("삭제 성공");
    }
}
