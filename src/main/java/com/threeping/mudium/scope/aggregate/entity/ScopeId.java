package com.threeping.mudium.scope.aggregate.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ScopeId implements Serializable {

    private Long musicalId;
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScopeId scopeId = (ScopeId) o;
        return Objects.equals(musicalId, scopeId.musicalId) &&
                Objects.equals(userId, scopeId.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(musicalId, userId);
    }
}
