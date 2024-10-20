package com.threeping.mudium.inquiry.repository;

import com.threeping.mudium.inquiry.aggregate.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InquiryRepository extends JpaRepository<Inquiry,Long> {

    Page<Inquiry> findByTitleContainingAndUser_UserId(String title, Long userId, Pageable pageable);

    Page<Inquiry> findByContentContainingAndUser_UserId(String content, Long userId, Pageable pageable);

    Page<Inquiry> findByUser_userId(Long userId, Pageable inquiryPageable);

    Inquiry findByInquiryIdAndUser_userId(Long inquiryId, Long userId);
}
