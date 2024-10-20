package com.threeping.mudium.customticket.controller;

import com.threeping.mudium.common.ResponseDTO;
import com.threeping.mudium.customticket.aggregate.dto.CustomTicketDTO;
import com.threeping.mudium.customticket.service.CustomTicketService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customticket")
@Transactional
public class CustomTicketController {

    private final CustomTicketService customTicketService;

    public CustomTicketController(CustomTicketService customTicketService) {
        this.customTicketService = customTicketService;
    }

    // 티켓 생성
    @PostMapping("/create")
    public ResponseDTO<?> createCustomTicket(@RequestBody CustomTicketDTO customTicketDTO
    ) { // 로그인된 사용자 ID를 전달
        System.out.println("userId: " + customTicketDTO.getUserId());
        return ResponseDTO.ok(customTicketService.createCustomTicket(customTicketDTO, customTicketDTO.getUserId()));
    }

    // 수정
    @PutMapping("/update/{ticketId}")
    public ResponseDTO<?> updateCustomTicket(@PathVariable Long ticketId,
                                             @RequestBody CustomTicketDTO customTicketDTO) {
        System.out.println("userId: " + customTicketDTO.getUserId());
        return ResponseDTO.ok(customTicketService.updateCustomTicket(ticketId, customTicketDTO, customTicketDTO.getUserId()));
    }

    @DeleteMapping("/delete/{ticketId}/{userId}")
    public ResponseDTO<?> deleteCustomTicket(@PathVariable Long ticketId,
                                             @PathVariable Long userId) {
        System.out.println("userId: " + userId);
        customTicketService.deleteCustomTicket(ticketId, userId);
        return ResponseDTO.ok(null);
    }

    @GetMapping("/{userId}")
    public ResponseDTO<?> getAllCustomTickets(@PathVariable Long userId) {
        System.out.println("userId: " + userId);
        return ResponseDTO.ok(customTicketService.getAllCustomTickets(userId));
    }
}