package com.threeping.mudium.calendar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "CalendarThemeEntity")
@Table(name = "TBL_CALENDAR_THEME")
public class CalendarTheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calendar_theme_id")
    private Long calendarThemeId;

    @Column(name = "path", nullable = false, length = 1023)
    private String path;

    @Column(name = "user_id")
    private Long userId;

//    @Column(name = "musical_info_id")
//    private Long musicalInfoId;



}
