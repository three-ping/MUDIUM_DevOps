package com.threeping.mudium.notice.repository;

import com.threeping.mudium.notice.aggregate.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NoticeRepository extends JpaRepository<Notice,Long> {
    Page<Notice> findByTitleContaining(String title, Pageable pageable);

    Page<Notice> findByContentContaining(String content, Pageable pageable);
}
