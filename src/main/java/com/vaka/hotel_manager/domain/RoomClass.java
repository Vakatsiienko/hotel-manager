package com.vaka.hotel_manager.domain;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public enum RoomClass {
    STANDARD, HALF_SUITE, SUITE, KING;

    @Override
    public String toString() {
        switch (this) {
            case STANDARD:
                return "Standard";
            case HALF_SUITE:
                return "Half Suite";
            case SUITE:
                return "Suite";
            case KING:
                return "King";
            default:
                throw new IllegalArgumentException();
        }
    }
}
