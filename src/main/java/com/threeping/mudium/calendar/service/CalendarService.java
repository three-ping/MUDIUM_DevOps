package com.threeping.mudium.calendar.service;

import java.io.IOException;
import java.util.Map;

public interface CalendarService {
    public Map<String, String> getAllImagesAsBase64();
    public String getImageAsBase64(String imagePath);

}
