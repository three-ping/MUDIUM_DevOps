package com.threeping.mudium.musicalreply.controller;

import com.threeping.mudium.common.ResponseDTO;
import com.threeping.mudium.musicalreply.dto.MusicalReplyDTO;
import com.threeping.mudium.musicalreply.service.MusicalReplyService;
import com.threeping.mudium.musicalreply.vo.MusicalReplyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/musical-reply")
public class MusicalReplyController {

    private final MusicalReplyService musicalReplyService;

    @Autowired
    public MusicalReplyController(MusicalReplyService musicalReplyService) {
        this.musicalReplyService = musicalReplyService;
    }

    @GetMapping("/{commentId}")
    public ResponseDTO<?> findMusicalReply(@PathVariable Long commentId) {
        List<MusicalReplyDTO> list = musicalReplyService.findAllReply(commentId);

        ResponseDTO<List<MusicalReplyDTO>> responseDTO = new ResponseDTO<>();
        responseDTO.setData(list);
        responseDTO.setSuccess(true);
        responseDTO.setHttpStatus(HttpStatus.OK);
        return responseDTO;
    }

    @PostMapping("/{commentId}")
    public ResponseDTO<?> createMusicalReply(@PathVariable Long commentId, @RequestBody MusicalReplyVO replyVO) {
        MusicalReplyDTO replyDTO = new MusicalReplyDTO();
        replyDTO.setCommentId(commentId);
        replyDTO.setContent(replyVO.getContent());

        musicalReplyService.createReply(replyVO.getUserId(), replyDTO);

        return ResponseDTO.ok(replyDTO);
    }

    @PutMapping("/{replyId}")
    public ResponseDTO<?> updateMusicalReply(@PathVariable Long replyId, @RequestBody MusicalReplyVO replyVO) {
        MusicalReplyDTO replyDTO = new MusicalReplyDTO();
        replyDTO.setMusicalReplyId(replyId);
        replyDTO.setContent(replyVO.getContent());

        musicalReplyService.updateReply(replyVO.getUserId(), replyDTO);

        return ResponseDTO.ok(replyDTO);
    }

    @DeleteMapping("/{replyId}")
    public ResponseDTO<?> deleteMusicalReply(@PathVariable Long replyId, @RequestBody MusicalReplyVO replyVO) {
        MusicalReplyDTO replyDTO = new MusicalReplyDTO();
        replyDTO.setMusicalReplyId(replyId);

        musicalReplyService.deleteReply(replyVO.getUserId(), replyDTO);

        return ResponseDTO.ok(replyDTO);
    }
}
