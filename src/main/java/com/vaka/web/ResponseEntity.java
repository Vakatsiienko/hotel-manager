package com.vaka.web;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by Iaroslav on 11/25/2016.
 */
@NoArgsConstructor
@Getter
public class ResponseEntity<T> {
    T rows;
    boolean success;
    long total;

    public ResponseEntity(T rows) {
        this.rows = rows;
        success = true;
    }

    public ResponseEntity(T rows, long total) {
        this.rows = rows;
        this.total = total;
        success = true;
    }
}
