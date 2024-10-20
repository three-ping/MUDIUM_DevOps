package com.threeping.mudium.bookmark.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookmarkPK implements Serializable {

    private Long userId;
    private Long musicalId;
}
