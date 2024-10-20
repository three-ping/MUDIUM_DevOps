package com.threeping.mudium.inquiry.service;


import com.threeping.mudium.common.exception.CommonException;
import com.threeping.mudium.common.exception.ErrorCode;
import com.threeping.mudium.inquiry.aggregate.entity.Inquiry;
import com.threeping.mudium.inquiry.aggregate.enumerate.SearchType;
import com.threeping.mudium.inquiry.dto.CreateInquiryDTO;
import com.threeping.mudium.inquiry.dto.InquiryDetailDTO;
import com.threeping.mudium.inquiry.dto.InquiryListDTO;
import com.threeping.mudium.inquiry.dto.UpdateInquiryDTO;
import com.threeping.mudium.inquiry.repository.InquiryRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
// 관리자용 전체 조회 메서드 추가 예정

@Slf4j
@Service
public class InquiryServiceImpl implements InquiryService{

    private final InquiryRepository inquiryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    InquiryServiceImpl(InquiryRepository inquiryRepository,
                       ModelMapper modelMapper){
        this.inquiryRepository = inquiryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<InquiryListDTO> viewSearchedInquiryList(Pageable pageable, SearchType searchType, String searchQuery, Long userId) {
        int pageNumber = pageable.getPageNumber() > 0 ? pageable.getPageNumber() - 1 : 0;
        int pageSize = pageable.getPageSize();
        Sort pageSort = Sort.by("createdAt").descending();
        Pageable inquiryPageable = PageRequest.of(pageNumber,pageSize,pageSort);
        if (searchType.equals(SearchType.TITLE)) {
            return searchInquiryByTitle(searchQuery,userId,inquiryPageable);
        } else if (searchType.equals(SearchType.CONTENT)) {
            return searchInquiryByContent(searchQuery,userId, inquiryPageable);
        }
        return null;
    }

    @Override
    public Page<InquiryListDTO> viewInquiryList(Pageable pageable, Long userId) {
        int pageNumber = pageable.getPageNumber() > 0 ? pageable.getPageNumber() - 1 : 0;
        int pageSize = pageable.getPageSize();
        Sort pageSort = Sort.by("createdAt").descending();
        Pageable inquiryPageable = PageRequest.of(pageNumber,pageSize,pageSort);

        Page<Inquiry> inquiries = inquiryRepository.findByUser_userId(userId,inquiryPageable);

        List<InquiryListDTO> inquiryListDTOList = inquiries.stream()
                .map(inquiry -> {
                    InquiryListDTO inquiryListDTO = modelMapper.map(inquiry,InquiryListDTO.class);
                    return inquiryListDTO;
                })
                .collect(Collectors.toList());
        log.info("inquyrDTOLIst:{}",inquiryListDTOList);
        Page<InquiryListDTO> inquiryListDTOS = new PageImpl<>(inquiryListDTOList,inquiryPageable,inquiries.getTotalElements());
        log.info("inquiryLISTDTO:{}",inquiryListDTOS);
        return inquiryListDTOS;
    }

    @Override
    public InquiryDetailDTO viewInquiry(Long userId, Long inquiryId) {
        Inquiry inquiry = inquiryRepository.findByInquiryIdAndUser_userId(inquiryId,userId);
        InquiryDetailDTO inquiryDetailDTO = null;
        try {
             inquiryDetailDTO = new InquiryDetailDTO(inquiry.getInquiryId(),
                    inquiry.getTitle(),
                    inquiry.getContent(),
                    inquiry.getCreatedAt(),
                    inquiry.getUpdatedAt(),
                    inquiry.getComments(),
                    inquiry.getUser().getUserId());
        } catch (Exception e){
            throw new CommonException(ErrorCode.NOT_FOUND_USER_INQUIRY);
        }
        return inquiryDetailDTO;
    }

    @Override
    public void createInquiry(CreateInquiryDTO createInquiryDTO) {
        Inquiry inquiry = new Inquiry();
        inquiry.setTitle(createInquiryDTO.getTitle());
        inquiry.setContent(createInquiryDTO.getContent());
        inquiry.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        inquiry.setComments(0L);
        inquiry.setUser(createInquiryDTO.getUser());

        inquiryRepository.save(inquiry);
    }

    @Override
    public void updateInquiry(UpdateInquiryDTO updateInquiryDTO) {
        Inquiry inquiry = inquiryRepository
                .findByInquiryIdAndUser_userId(updateInquiryDTO.getInquiryId(),updateInquiryDTO.getUserId());
        inquiry.setTitle(updateInquiryDTO.getTitle());
        inquiry.setContent(updateInquiryDTO.getContent());
        inquiry.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        inquiryRepository.save(inquiry);
    }

    @Override
    public void deleteInquiry(Long userId, Long inquiryId) {
        Inquiry inquiry = inquiryRepository
                .findByInquiryIdAndUser_userId(inquiryId,userId);
        inquiryRepository.delete(inquiry);
    }

    public Page<InquiryListDTO> searchInquiryByTitle(String title,Long userId, Pageable pageable) {
        return inquiryRepository.findByTitleContainingAndUser_UserId(title,userId, pageable).map(this::convertToDTO);
    }

    public Page<InquiryListDTO> searchInquiryByContent(String content, Long userId, Pageable pageable) {
        return inquiryRepository.findByContentContainingAndUser_UserId(content, userId, pageable).map(this::convertToDTO);
    }

    private InquiryListDTO convertToDTO(Inquiry inquiry){
        InquiryListDTO inquiryListDTO = new InquiryListDTO();
        inquiryListDTO.setTitle(inquiry.getTitle());
        inquiryListDTO.setComments(inquiry.getComments());
        inquiryListDTO.setCreatedAt(inquiry.getCreatedAt());
        inquiryListDTO.setInquiryId(inquiry.getInquiryId());
        inquiryListDTO.setUserId(inquiry.getUser().getUserId());
        return inquiryListDTO;
    }

}
