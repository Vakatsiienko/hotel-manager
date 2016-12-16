package com.vaka.hotel_manager.repository;

import com.vaka.hotel_manager.domain.DTO.ReservationDTO;
import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.ReservationStatus;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Iaroslav on 11/27/2016.
 */
public interface ReservationRepository extends CrudRepository<Reservation> {

//    /**
//     * @return all reservations by given room with given status
//     */
//    List<ReservationDTO> findByRoomIdAndStatus(Integer roomId, ReservationStatus status);

    /**
     * @return all reservations by status
     */
    List<ReservationDTO> findByStatusFromDate(ReservationStatus status, LocalDate fromDate);

    /**
     * @return all reservations created by user with given status
     */
    List<ReservationDTO> findByUserIdAndStatus(Integer userId, ReservationStatus status);

    /**
     * @return all CONFIRMED reservations by given user that have not ended
     */
    List<ReservationDTO> findActiveByUserId(Integer userId);

    /**
     * @return true if given room cannot be reserve in given date period
     */
    boolean existOverlapReservation(Integer roomId, LocalDate arrivalDate, LocalDate departureDate);
}
