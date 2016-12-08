package com.vaka.hotel_manager.repository;

import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.ReservationStatus;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Iaroslav on 11/27/2016.
 */
public interface ReservationRepository extends CrudRepository<Reservation> {

    /**
     * @return all reservations by given room with given status
     */
    List<Reservation> findByRoomIdAndStatus(Integer roomId, ReservationStatus status);

    /**
     * @return all reservations by status
     */
    List<Reservation> findByStatus(ReservationStatus status);

    /**
     * @return all reservations created by user with given status
     */
    List<Reservation> findByUserIdAndStatus(Integer userId, ReservationStatus status);

    /**
     * @return all non REJECTED reservations by given user that have not ended
     */
    List<Reservation> findActiveByUserId(Integer userId);

    /**
     * @return true if given room cannot be reserve in given date period
     */
    boolean existOverlapReservation(Integer roomId, LocalDate arrivalDate, LocalDate departureDate);
}
