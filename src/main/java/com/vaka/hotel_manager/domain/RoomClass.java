package com.vaka.hotel_manager.domain;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public enum RoomClass {
    STANDARD {
        @Override
        protected String userValue() {
            return "Standard";
        }
    }, HALF_SUITE {
        @Override
        protected String userValue() {
            return "Half Suite";
        }
    }, SUITE {
        @Override
        protected String userValue() {
            return "Suite";
        }
    }, KING {
        @Override
        protected String userValue() {
            return "King";
        }
    };

    protected abstract String userValue();

    @Override
    public String toString() {
        return userValue();
    }
}
