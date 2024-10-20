package com.threeping.mudium.musical.service;

import com.threeping.mudium.musical.dto.MusicalDTO;
import com.threeping.mudium.musical.dto.MusicalListDTO;
import com.threeping.mudium.performance.dto.PerformanceDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MusicalServiceImplTests {

    private final MusicalService musicalService;

    @Autowired
    public MusicalServiceImplTests(MusicalService musicalService) {
        this.musicalService = musicalService;
    }

    @DisplayName("뮤지컬 번호로 뮤지컬 상세 정보를 조회한다.")
    @Test
    void musicalDetailViewTest() {
        // given
        Long musicId = 3L;

        // when
        MusicalDTO musicalDetail = musicalService.findMusicalDetail(musicId);

        // then
        assertNotNull(musicalDetail, "조회된 뮤지컬 상세 정보는 null이 아니다.");
    }


    @DisplayName("모든 뮤지컬 정보를 페이징 조회한다.")
    @Test
    void viewMusicalList() {
        // given
        Pageable pageable = PageRequest.of(1, 12, Sort.by("viewCount").descending());
        String title = "";

        // when
        Page<MusicalListDTO> dtoPage = musicalService.findByName(title, pageable);

        // then
        assertNotNull(dtoPage, "조회된 페이징 뮤지컬 정보 객체는 null이 아니다.");
        assertTrue(dtoPage.hasContent(), "조회된 페이징 뮤지컬 정보는 내용을 갖고 있다.");
        assertEquals(12, dtoPage.getSize(), "조회된 뮤지컬 정보는 페이지당 12개이다.");
    }

    @DisplayName("뮤지컬 이름으로 조회한다.")
    @Test
    void searchByTitle() {
        // given
        Pageable pageable = PageRequest.of(1, 10, Sort.by("viewCount").descending());
        String title = "빨간";

        // when
        Page<MusicalListDTO> dtoPage = musicalService.findByName(title, pageable);
        List<MusicalListDTO> dtoList = dtoPage.getContent();
        for (MusicalListDTO dto : dtoList) {
            System.out.println(dto.getTitle());
        }

        // then
        assertNotNull(dtoPage, "검색된 이름으로 조회된 뮤지컬은 null이 아니다.");
        assertTrue(dtoPage.hasContent(), "검색된 이름으로 조회된 페이징 객체는 내용을 갖고 있다.");
    }

    @DisplayName("메인 화면에서 뮤지컬을 클릭 시, 뮤지컬의 이름으로 상세조회를 실행한다.")
    @Test
    void searchByExactTitle() {
        // given
        String title = "킹키부츠";

        // when
        MusicalDTO dto = musicalService.findMusicalDetailByName(title);

        // then
        assertNotNull(dto, "조회된 뮤지컬은 null이 아니다.");
        assertEquals(dto.getTitle(), title);
    }
}