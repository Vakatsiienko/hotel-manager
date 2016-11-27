package com.vaka.service;

import com.vaka.domain.ReservationRequest;
import com.vaka.domain.Room;
import com.vaka.domain.User;

import java.util.List;

/**
 * Created by Iaroslav on 11/24/2016.
 */
public interface RoomService extends CrudService<Room> {

    List<Room> findForRequestId(User loggedUser, Integer id);
    
    List<Room> findForRequest(User loggedUser, ReservationRequest request);
}
