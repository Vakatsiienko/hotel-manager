package com.vaka.hotel_manager.domain;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public enum RoomClass {
    STANDARD("Standard"), HALF_SUITE("Half Suite"), SUITE("Suite"), KING("King"),;

    private String name;


    RoomClass(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }
}
