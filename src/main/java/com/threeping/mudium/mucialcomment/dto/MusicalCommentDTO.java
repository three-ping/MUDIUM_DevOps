package com.threeping.mudium.mucialcomment.dto;

import com.threeping.mudium.mucialcomment.aggregate.ActiveStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MusicalCommentDTO {

    private Long commentId;

    private Long postId;

    private String content;

    private String createdAt;

    private String updatedAt;

    private String nickName;

}
