package com.threeping.mudium.customticket.aggregate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomTicketDTO {

    private Long customTicketId;
    private String ticketImage;
    private String hologramColor1;
    private String hologramColor2;
    private String comment;
    private Long userId;
}
