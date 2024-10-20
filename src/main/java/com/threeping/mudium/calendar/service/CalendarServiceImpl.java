package com.threeping.mudium.calendar.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.threeping.mudium.calendar.repository.CalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.apache.commons.io.IOUtils;


import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional
public class CalendarServiceImpl implements CalendarService{

        private final CalendarRepository calendarRepository;

        @Autowired
        public CalendarServiceImpl(CalendarRepository calendarRepository) {
            this.calendarRepository = calendarRepository;
        }

        @Override
        public Map<String, String> getAllImagesAsBase64() {
            Map<String, String> images = new HashMap<>();
            String[] imageNames = {
                    "aladdin1.jpg", "aladdin2.jpg", "aladdin3.jpg",
                    "calendar_image_1.png", "calendar_image_2.png",
                    "calendar_image_3.png", "calendar_image_4.png",
                    "lalaland.jpg", "mammamia.jpg"
            };

            Stream.of(imageNames).forEach( imageName -> {
                String base64Image = getImageAsBase64("calendar/asset/" + imageName);
                if (base64Image != null) {
                    images.put(imageName, base64Image);
                }
            });

            return images;
        }

    public String getImageAsBase64(String imagePath) {
        try {
            ClassPathResource resource = new ClassPathResource(imagePath);
            byte[] imageBytes = IOUtils.toByteArray(resource.getInputStream());
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {  // Catching generic Exception to avoid IOException
            log.error("Error while reading image {}: {}", imagePath, e.getMessage());
            return null;
        }
    }
    }

