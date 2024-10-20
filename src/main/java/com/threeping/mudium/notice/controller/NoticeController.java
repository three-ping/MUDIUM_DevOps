package com.threeping.mudium.notice.controller;

import com.threeping.mudium.notice.aggregate.enumerate.SearchType;
import com.threeping.mudium.common.ResponseDTO;
import com.threeping.mudium.notice.dto.CreateNoticeDTO;
import com.threeping.mudium.notice.dto.NoticeDetailDTO;
import com.threeping.mudium.notice.dto.NoticeListDTO;
import com.threeping.mudium.notice.dto.UpdateNoticeDTO;
import com.threeping.mudium.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @Autowired
    private NoticeController(NoticeService noticeService){
        this.noticeService = noticeService;
    }

    @GetMapping("")
    private ResponseDTO<?> viewNoticePage(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchQuery,
            Pageable pageable) {

        Page<NoticeListDTO> noticePage;

        if (searchType != null && searchQuery != null && !searchQuery.trim().isEmpty()) {
            noticePage = noticeService.viewSearchedNoticeList(pageable,searchType,searchQuery);
        } else {
            noticePage = noticeService.viewNoticeList(pageable);
        }

        return ResponseDTO.ok(noticePage);
    }

    @GetMapping("{noticeId}")
    private ResponseDTO<?> viewDetailNotice(@PathVariable Long noticeId){
        NoticeDetailDTO noticeDetail = noticeService.viewNotice(noticeId);
        return ResponseDTO.ok(noticeDetail);
    }

    @PostMapping("")
    private ResponseDTO<?> createNotice(@RequestBody CreateNoticeDTO createNoticeDTO){
        noticeService.createNotice(createNoticeDTO);
        return ResponseDTO.ok(null);
    }

    @PutMapping("{noticeId}")
    private ResponseDTO<?> updateNotice(@PathVariable Long noticeId,
                                        @RequestBody UpdateNoticeDTO updateNoticeDTO){
        updateNoticeDTO.setNoticeId(noticeId);
        noticeService.updateNotice(updateNoticeDTO);
        return ResponseDTO.ok(null);
    }

    @DeleteMapping("{noticeId}/{userId}")
    private ResponseDTO<?> deleteNotice(@PathVariable Long noticeId,
                                        @PathVariable Long userId){
        noticeService.deleteNotice(noticeId,userId);
        return ResponseDTO.ok(null);
    }
}
