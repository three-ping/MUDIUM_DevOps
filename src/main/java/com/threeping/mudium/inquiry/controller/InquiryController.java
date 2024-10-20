package com.threeping.mudium.inquiry.controller;

import com.threeping.mudium.inquiry.aggregate.enumerate.SearchType;
import com.threeping.mudium.common.ResponseDTO;
import com.threeping.mudium.inquiry.dto.CreateInquiryDTO;
import com.threeping.mudium.inquiry.dto.InquiryDetailDTO;
import com.threeping.mudium.inquiry.dto.InquiryListDTO;
import com.threeping.mudium.inquiry.dto.UpdateInquiryDTO;
import com.threeping.mudium.inquiry.service.InquiryService;
import com.threeping.mudium.user.aggregate.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;

    @Autowired
    InquiryController(InquiryService inquiryService){
        this.inquiryService = inquiryService;
    }

    @GetMapping("{userId}")
    private ResponseDTO<?> viewInquiryPage(
            @PathVariable Long userId,
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchQuery,
            Pageable pageable){

        Page<InquiryListDTO> inquiryPage;

        if(searchType != null && searchQuery != null && !searchQuery.trim().isEmpty()) {
            inquiryPage = inquiryService.viewSearchedInquiryList(pageable,searchType,searchQuery,userId);
        } else {
            inquiryPage = inquiryService.viewInquiryList(pageable,userId);
        }

        return ResponseDTO.ok(inquiryPage);
    }

    @GetMapping("{userId}/{inquiryId}")
    private ResponseDTO<?> viewDetailInquiry(@PathVariable Long userId,
                                          @PathVariable Long inquiryId){
        InquiryDetailDTO inquiryDetail = inquiryService.viewInquiry(userId,inquiryId);
        return ResponseDTO.ok(inquiryDetail);
    }

    @PostMapping("{userId}")
    private ResponseDTO<?> createInquiry(@PathVariable Long userId,
                                         @RequestBody CreateInquiryDTO createInquiryDTO){
        UserEntity user = new UserEntity();
        user.setUserId(userId);
        createInquiryDTO.setUser(user);
        inquiryService.createInquiry(createInquiryDTO);

        return ResponseDTO.ok(null);
    }

    @PutMapping("{userId}/{inquiryId}")
    private ResponseDTO<?> updateInquiry(@PathVariable Long userId,
                                         @PathVariable Long inquiryId,
                                         @RequestBody UpdateInquiryDTO updateInquiryDTO){
        updateInquiryDTO.setUserId(userId);
        updateInquiryDTO.setInquiryId(inquiryId);
        inquiryService.updateInquiry(updateInquiryDTO);

        return ResponseDTO.ok(null);
    }

    @DeleteMapping("{userId}/{inquiryId}")
    private ResponseDTO<?> deleteInquiry(@PathVariable Long userId,
                                         @PathVariable Long inquiryId){
        inquiryService.deleteInquiry(userId,inquiryId);

        return ResponseDTO.ok(null);
    }


}
