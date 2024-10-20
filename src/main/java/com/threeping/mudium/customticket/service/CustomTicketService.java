package com.threeping.mudium.customticket.service;

import com.threeping.mudium.customticket.aggregate.dto.CustomTicketDTO;

import java.util.List;

public interface CustomTicketService {

    CustomTicketDTO createCustomTicket(CustomTicketDTO customTicketDTO, Long userId); // userId 추가

    CustomTicketDTO updateCustomTicket(Long customTicketId, CustomTicketDTO customTicketDTO, Long userId); // userId 추가

    void deleteCustomTicket(Long ticketId, Long userId); // userId 추가

    List<CustomTicketDTO> getAllCustomTickets(Long userId); // userId 추가
}