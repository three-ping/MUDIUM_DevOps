package com.threeping.mudium.scope.aggregate.entity;

import com.threeping.mudium.user.aggregate.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@IdClass(ScopeId.class)
@Table(name="tbl_scope")
public class ScopeEntity {

    @Id
    @Column(name="musical_info_id")
    private Long musicalId;

    @Id
    @Column(name="user_id")
    private Long userId;

    @Column(name="scope", nullable=false)
    private Double scope;

    @Column(name="created_at", nullable=false)
    private Timestamp createdAt;

    @Column(name="updated_at", nullable=true)
    private Timestamp updatedAt;

    @Column(name = "user_nickname", nullable = false)
    private String userNickname;
}
