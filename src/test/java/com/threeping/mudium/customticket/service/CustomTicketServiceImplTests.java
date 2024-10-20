package com.threeping.mudium.customticket.service;

import com.threeping.mudium.customticket.aggregate.dto.CustomTicketDTO;
import com.threeping.mudium.customticket.aggregate.entity.CustomTicketEntity;
import com.threeping.mudium.customticket.repository.CustomTicketRepository;
import com.threeping.mudium.user.aggregate.entity.UserEntity;
import com.threeping.mudium.user.aggregate.entity.UserRole;
import com.threeping.mudium.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
@Transactional
class CustomTicketServiceImplTests {

    @Autowired
    private CustomTicketService customTicketService;

    @Autowired
    private CustomTicketRepository customTicketRepository;

    @Autowired
    private UserRepository userRepository;

    private CustomTicketDTO customTicketDTO;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        // 사용자 데이터 생성
        userEntity = new UserEntity();
        userEntity.setUserName("Test User");
        userEntity.setEncryptedPwd("password123");
        userEntity.setNickname("test_nickname");
        userEntity.setUserRole(UserRole.ROLE_MEMBER);
        userRepository.save(userEntity); // 사용자 저장

        // 커스텀 티켓 DTO 설정
        customTicketDTO = new CustomTicketDTO();
        customTicketDTO.setTicketImage("data:image/jpeg;base64,test-image");
        customTicketDTO.setHologramColor1("#ffdb70");
        customTicketDTO.setHologramColor2("#8432ff");
        customTicketDTO.setComment("Test comment");
        customTicketDTO.setUserId(userEntity.getUserId());
    }

    @Test
    void testCreateCustomTicket() {
        // 커스텀 티켓 생성
        CustomTicketDTO createdTicket = customTicketService.createCustomTicket(customTicketDTO, userEntity.getUserId());

        assertNotNull(createdTicket);
        assertEquals(customTicketDTO.getTicketImage(), createdTicket.getTicketImage());
        assertEquals(customTicketDTO.getHologramColor1(), createdTicket.getHologramColor1());
        assertEquals(customTicketDTO.getHologramColor2(), createdTicket.getHologramColor2());
        assertEquals(customTicketDTO.getComment(), createdTicket.getComment());

        // DB에 생성된 티켓 확인
        CustomTicketEntity savedTicket = customTicketRepository.findById(createdTicket.getCustomTicketId()).orElse(null);
        assertNotNull(savedTicket);
        assertEquals(userEntity, savedTicket.getUser());
    }

    @Test
    void testUpdateCustomTicket() {
        // 커스텀 티켓 생성 후 업데이트
        CustomTicketDTO createdTicket = customTicketService.createCustomTicket(customTicketDTO, userEntity.getUserId());

        CustomTicketDTO updateDTO = new CustomTicketDTO();
        updateDTO.setTicketImage("data:image/jpeg;base64,updated-image");
        updateDTO.setHologramColor1("#ffffff");
        updateDTO.setHologramColor2("#000000");
        updateDTO.setComment("Updated comment");

        // 커스텀 티켓 업데이트
        CustomTicketDTO updatedTicket = customTicketService.updateCustomTicket(createdTicket.getCustomTicketId(), updateDTO, userEntity.getUserId());

        assertNotNull(updatedTicket);
        assertEquals("data:image/jpeg;base64,updated-image", updatedTicket.getTicketImage());
        assertEquals("#ffffff", updatedTicket.getHologramColor1());
        assertEquals("#000000", updatedTicket.getHologramColor2());
        assertEquals("Updated comment", updatedTicket.getComment());
    }

    @Test
    void testDeleteCustomTicket() {
        // 커스텀 티켓 생성 후 삭제
        CustomTicketDTO createdTicket = customTicketService.createCustomTicket(customTicketDTO, userEntity.getUserId());

        customTicketService.deleteCustomTicket(createdTicket.getCustomTicketId(), userEntity.getUserId());

        // DB에서 티켓 삭제 확인
        CustomTicketEntity deletedTicket = customTicketRepository.findById(createdTicket.getCustomTicketId()).orElse(null);
        assertNull(deletedTicket); // 삭제 후 null 확인
    }

    @Test
    void testGetAllCustomTickets() {
        // 여러 개의 커스텀 티켓 생성
        customTicketService.createCustomTicket(customTicketDTO, userEntity.getUserId());
        CustomTicketDTO secondTicketDTO = new CustomTicketDTO();
        secondTicketDTO.setTicketImage("data:image/jpeg;base64,second-image");
        secondTicketDTO.setHologramColor1("#abc123");
        secondTicketDTO.setHologramColor2("#def456");
        secondTicketDTO.setComment("Second comment");
        secondTicketDTO.setUserId(userEntity.getUserId());

        customTicketService.createCustomTicket(secondTicketDTO, userEntity.getUserId());

        // 해당 유저의 모든 티켓 조회
        List<CustomTicketDTO> tickets = customTicketService.getAllCustomTickets(userEntity.getUserId());

        assertNotNull(tickets);
        assertEquals(2, tickets.size());
    }
}
