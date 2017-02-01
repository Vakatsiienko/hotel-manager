package com.vaka.hotel_manager.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Created by Iaroslav on 1/29/2017.
 */
@AllArgsConstructor
@Getter
public class Page<T> {
    private List<T> content;
    private final long totalLength;
}
