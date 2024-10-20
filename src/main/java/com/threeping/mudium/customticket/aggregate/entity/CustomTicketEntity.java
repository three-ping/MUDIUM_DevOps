package com.threeping.mudium.customticket.aggregate.entity;

import com.threeping.mudium.musical.aggregate.Musical;
import com.threeping.mudium.user.aggregate.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="tbl_custom_ticket")
public class CustomTicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="custom_ticket_id")
    private Long customTicketId;

    @Column(name="ticket_image", columnDefinition="LONGTEXT") // 이미지 Base64를 저장하는 필드
    private String ticketImage;

    @Column(name="hologram_color1")
    private String hologramColor1;

    @Column(name="hologram_color2")
    private String hologramColor2;

    @Column(name="comment", length=500)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)  // 사용자와 다대일 관계 설정
    @JoinColumn(name="user_id", nullable=false)
    private UserEntity user; // 티켓의 소유자를 나타냄
}