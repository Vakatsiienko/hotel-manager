package com.vaka.repository.inMemoryImpl;

import com.vaka.domain.ReservationRequest;
import com.vaka.domain.Room;
import com.vaka.repository.RoomRepository;

import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Iaroslav on 11/26/2016.
 */
public class RoomRepositoryImpl implements RoomRepository {
    private Map<Integer, Room> roomById = new HashMap<>();
    private Map<Room, Period> reserved;

    @Override
    public List<Room> find(ReservationRequest reservationRequest) {
        return null;
    }

    @Override
    public boolean reserve(Room room) {
        return false;
    }

    @Override
    public Room create(Room entity) {
        return null;
    }

    @Override
    public Room getById(Integer id) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public Room update(Integer id, Room entity) {
        return null;
    }
}
