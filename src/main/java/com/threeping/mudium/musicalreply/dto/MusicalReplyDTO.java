package com.threeping.mudium.musicalreply.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MusicalReplyDTO {

    private Long musicalReplyId;

    private String content;

    private String createdAt;

    private String  updatedAt;

    private Long CommentId;

    private String nickName;

}
