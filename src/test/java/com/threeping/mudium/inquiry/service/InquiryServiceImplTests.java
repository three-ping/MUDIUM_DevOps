package com.threeping.mudium.inquiry.service;

import com.threeping.mudium.common.exception.CommonException;
import com.threeping.mudium.inquiry.aggregate.entity.Inquiry;
import com.threeping.mudium.inquiry.dto.CreateInquiryDTO;
import com.threeping.mudium.inquiry.dto.InquiryDetailDTO;
import com.threeping.mudium.inquiry.dto.InquiryListDTO;
import com.threeping.mudium.inquiry.dto.UpdateInquiryDTO;
import com.threeping.mudium.inquiry.repository.InquiryRepository;
import com.threeping.mudium.user.aggregate.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class InquiryServiceImplTests {

    private final InquiryService inquiryService;
    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    InquiryServiceImplTests(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @DisplayName("게시글 리스트를 페이지로 조회한다.")
    @Test
    void inquiryPageViewTest(){
        Pageable pageable = PageRequest.of(1,10, Sort.by("createdAt").descending());
        Long memberId = 1L;
        Page<InquiryListDTO> inquiryDTOPage = inquiryService.viewInquiryList(pageable,memberId);

        assertNotNull(inquiryDTOPage, "조회된 페이지는 null이 아니다.");
        log.info("inquiryDTOPage: {}",inquiryDTOPage);
        assertTrue(inquiryDTOPage.hasContent(), "조회된 페이지는 내용이 있다.");
        assertEquals(10, inquiryDTOPage.getSize(), "페이지 크기는 10이다.");

    }

    @DisplayName("게시글 상세 정보를 조회한다.")
    @Test
    void inquiryDetailViewTest(){
        Long invalidInquiryId = -1L;
        Long userId = 1L;
        String title = "테스트용 문의";
        String content = "테스트용 문의 내용";
        UserEntity user = new UserEntity();
        user.setUserId(userId);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        CreateInquiryDTO createInquiryDTO = new CreateInquiryDTO(
                title,content,new Timestamp(System.currentTimeMillis()),user);

        inquiryService.createInquiry(createInquiryDTO);

        Inquiry createdInquiry = inquiryRepository.findByUser_userId(userId,pageable).getContent().get(0);

        InquiryDetailDTO inquiryDetailDTO = inquiryService.viewInquiry(userId,createdInquiry.getInquiryId());

        assertNotNull(inquiryDetailDTO,"조회된 게시글은 null이 아니다.");
        assertEquals(createdInquiry.getInquiryId(),inquiryDetailDTO.getInquiryId(),"조회된 게시글 ID는 요청된 ID와 일치한다.");
        Exception exception = assertThrows(CommonException.class,
                ()->inquiryService.viewInquiry(userId,invalidInquiryId));
        assertEquals("해당 문의를 찾을 수 없습니다.",exception.getMessage());
    }

    @DisplayName("게시글을 작성한다.")
    @Test
    void inquiryCreateTest(){
        Long userId = 1L;
        String title = "테스트용 문의";
        String content = "테스트용 문의 내용";
        UserEntity user = new UserEntity();
        user.setUserId(userId);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        CreateInquiryDTO createInquiryDTO = new CreateInquiryDTO(
                title,content,new Timestamp(System.currentTimeMillis()),user);

        inquiryService.createInquiry(createInquiryDTO);

        Inquiry createdInquiry = inquiryRepository.findByUser_userId(userId,pageable).getContent().get(0);

        assertEquals(title,createdInquiry.getTitle());
        assertEquals(content,createdInquiry.getContent());

    }

    @DisplayName("게시글을 수정한다.")
    @Test
    void inquiryUpdateTest(){
        Long userId = 1L;
        String title = "테스트용 문의";
        String content = "테스트용 문의 내용";
        UserEntity user = new UserEntity();
        user.setUserId(userId);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        CreateInquiryDTO createInquiryDTO = new CreateInquiryDTO(
                title,content,new Timestamp(System.currentTimeMillis()),user);

        inquiryService.createInquiry(createInquiryDTO);

        Inquiry createdInquiry = inquiryRepository.findByUser_userId(userId,pageable).getContent().get(0);

        String updatedTitle = "수정된 문의";
        String updatedContent = "수정된 내용";
        UpdateInquiryDTO updateInquiryDTO = new UpdateInquiryDTO(
                userId,createdInquiry.getInquiryId(),updatedTitle,updatedContent
                ,new Timestamp(System.currentTimeMillis()));

        inquiryService.updateInquiry(updateInquiryDTO);

        Inquiry updatedInquiry = inquiryRepository.findById(createdInquiry.getInquiryId()).orElseThrow();

        assertEquals(updatedTitle,updatedInquiry.getTitle());
        assertEquals(updatedContent,updatedInquiry.getContent());
    }

    @DisplayName("게시글을 삭제한다.")
    @Test
    void deleteInquiryTest(){
        Long userId = 1L;
        String title = "테스트용 문의";
        String content = "테스트용 문의 내용";
        UserEntity user = new UserEntity();
        user.setUserId(userId);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        CreateInquiryDTO createInquiryDTO = new CreateInquiryDTO(
                title,content,new Timestamp(System.currentTimeMillis()),user);

        inquiryService.createInquiry(createInquiryDTO);

        Inquiry createdInquiry = inquiryRepository.findByUser_userId(userId,pageable).getContent().get(0);

        inquiryService.deleteInquiry(userId,createdInquiry.getInquiryId());

assertThrows(CommonException.class,
                ()->inquiryService.viewInquiry(createdInquiry.getInquiryId(),userId));
    }
}