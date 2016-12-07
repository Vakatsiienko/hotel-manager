package com.vaka.domain;


import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by Iaroslav on 11/23/2016.
 */

@Getter
@Setter
@ToString
//@EqualsAndHashCode(exclude = "createdDatetime") //mysql timestamp problem
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {
    private Integer id;

    private LocalDateTime createdDatetime;

    public boolean isNew() {
        return id == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id);
//                && Objects.equals(createdDatetime, that.createdDatetime); TODO resolve persisting datetime bug
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDatetime);
    }
}
