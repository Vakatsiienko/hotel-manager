package com.vaka.domain;


import lombok.*;

import java.time.LocalDateTime;

/**
 * Created by Iaroslav on 11/23/2016.
 */

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {
    private Integer id;

    private LocalDateTime createdDate;

    public boolean isNew() {
        return id == null;
    }
}
