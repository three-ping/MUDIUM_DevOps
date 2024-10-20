package com.threeping.mudium.calendar.aggregate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CalendarDTO {
    private Long calendarThemeId;
    private String path;
    private Long userId;
    private Long musicalInfoId;
}
