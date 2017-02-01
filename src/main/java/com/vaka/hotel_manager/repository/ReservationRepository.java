package com.vaka.hotel_manager.repository;

import com.vaka.hotel_manager.domain.DTO.ReservationDTO;
import com.vaka.hotel_manager.domain.Page;
import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.ReservationStatus;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Iaroslav on 11/27/2016.
 */
public interface ReservationRepository extends CrudRepository<Reservation> {

    /**
     * @return all reservations by status
     */
    Page<ReservationDTO> findPageByStatusFromDate(ReservationStatus status, LocalDate fromDate, Integer page, Integer size);

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
    boolean existOverlapReservations(Integer roomId, LocalDate arrivalDate, LocalDate departureDate);
}
