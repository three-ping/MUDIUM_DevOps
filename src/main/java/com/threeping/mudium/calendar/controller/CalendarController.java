package com.threeping.mudium.calendar.controller;

import com.threeping.mudium.calendar.repository.CalendarRepository;
import com.threeping.mudium.calendar.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.Map;

@RestController
@RequestMapping("/calendar-theme")
public class CalendarController {
    private final CalendarRepository calendarRepository;
    private final CalendarService calendarService;

    @Autowired
    public CalendarController(CalendarRepository calendarRepository, CalendarService calendarService) {
        this.calendarRepository = calendarRepository;
        this.calendarService = calendarService;
    }

    @GetMapping("/images")
    public ResponseEntity<Map<String, String>> getImages() {
        Map<String, String> images = calendarService.getAllImagesAsBase64();
        return ResponseEntity.ok(images);
    }
}
