package com.threeping.mudium.customticket.service;

import com.threeping.mudium.customticket.aggregate.dto.CustomTicketDTO;
import com.threeping.mudium.customticket.aggregate.entity.CustomTicketEntity;
import com.threeping.mudium.customticket.repository.CustomTicketRepository;
import com.threeping.mudium.musical.aggregate.Musical;
import com.threeping.mudium.musical.repository.MusicalRepository;
import com.threeping.mudium.user.aggregate.entity.UserEntity;
import com.threeping.mudium.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomTicketServiceImpl implements CustomTicketService {

    private final CustomTicketRepository customTicketRepository;
    private final UserRepository userRepository;  // UserRepository 주입

    public CustomTicketServiceImpl(CustomTicketRepository customTicketRepository,
                                   UserRepository userRepository) {
        this.customTicketRepository = customTicketRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CustomTicketDTO createCustomTicket(CustomTicketDTO customTicketDTO, Long userId) {
        // 로그인된 사용자를 찾음
        Optional<UserEntity> userOpt = userRepository.findById(userId);
        UserEntity user = userOpt.orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));


        // 티켓 엔티티 생성 및 사용자 정보 설정
        CustomTicketEntity ticket = new CustomTicketEntity();
        ticket.setTicketImage(customTicketDTO.getTicketImage());
        ticket.setHologramColor1(customTicketDTO.getHologramColor1());
        ticket.setHologramColor2(customTicketDTO.getHologramColor2());
        ticket.setComment(customTicketDTO.getComment());
        ticket.setUser(user);  // 사용자 정보 연동

        // 티켓 저장
        CustomTicketEntity savedTicket = customTicketRepository.save(ticket);
        return new CustomTicketDTO(
                savedTicket.getCustomTicketId(),
                savedTicket.getTicketImage(),
                savedTicket.getHologramColor1(),
                savedTicket.getHologramColor2(),
                savedTicket.getComment(),
                user.getUserId()
        );
    }

    @Override
    public CustomTicketDTO updateCustomTicket(Long ticketId, CustomTicketDTO customTicketDTO, Long userId) {
        CustomTicketEntity ticket = customTicketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("티켓을 찾을 수 없습니다."));

        // 로그인된 사용자가 티켓의 소유자인지 확인
        UserEntity user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        if (!ticket.getUser().equals(user)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        ticket.setTicketImage(customTicketDTO.getTicketImage());
        ticket.setHologramColor1(customTicketDTO.getHologramColor1());
        ticket.setHologramColor2(customTicketDTO.getHologramColor2());
        ticket.setComment(customTicketDTO.getComment());

        CustomTicketEntity updatedTicket = customTicketRepository.save(ticket);
        return new CustomTicketDTO(
                updatedTicket.getCustomTicketId(),
                updatedTicket.getTicketImage(),
                updatedTicket.getHologramColor1(),
                updatedTicket.getHologramColor2(),
                updatedTicket.getComment(),
                user.getUserId()
        );
    }

    @Override
    public void deleteCustomTicket(Long ticketId, Long userId) {
        CustomTicketEntity ticket = customTicketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("티켓을 찾을 수 없습니다."));

        // 로그인된 사용자가 티켓의 소유자인지 확인
        UserEntity user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        if (!ticket.getUser().equals(user)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        customTicketRepository.deleteById(ticketId);
    }

    @Override
    public List<CustomTicketDTO> getAllCustomTickets(Long userId) {
        // 로그인된 사용자의 티켓만 가져옴
        UserEntity user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        List<CustomTicketEntity> tickets = customTicketRepository.findAllByUser(user);
        return tickets.stream()
                .map(ticket -> new CustomTicketDTO(
                        ticket.getCustomTicketId(),
                        ticket.getTicketImage(),
                        ticket.getHologramColor1(),
                        ticket.getHologramColor2(),
                        ticket.getComment(),
                        user.getUserId()
                ))
                .collect(Collectors.toList());
    }
}