package com.threeping.mudium.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkAndMusicalVO {
    private Long userId;
    private Long musicalId;
    private String title;
    private String rating;
    private String reviewVideo;
    private String poster;
    private Long viewCount;
    private String production;
    private String synopsys;
}