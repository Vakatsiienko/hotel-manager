package com.vaka.hotel_manager.service;

import com.vaka.hotel_manager.domain.DTO.ReservationDTO;
import com.vaka.hotel_manager.domain.Reservation;
import com.vaka.hotel_manager.domain.ReservationStatus;
import com.vaka.hotel_manager.domain.User;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Iaroslav on 11/27/2016.
 */
public interface ReservationService extends CrudService<Reservation> {

    /**
     * @param roomId which room to apply
     * @param reservationId to which reservation apply room
     * @return true if room applied, false if not, throwing NotFoundException if room or reservation not present by given id.
     */
    boolean applyRoom(User loggedUser, Integer roomId, Integer reservationId);

    /**
     * @return all ReservationDTO by given status, that have not end by given date
     */
    List<ReservationDTO> findByStatusFromDate(User loggedUser, ReservationStatus status, LocalDate fromDate);

    /**
     * @return all ReservationDTO by given status and User ID
     */
    List<ReservationDTO> findByStatusAndUserId(User loggedUser, ReservationStatus status, Integer userId);

    /**
     * @param reservationId id of rejecting reservation
     * @return true if reservation have been rejected, false if not
     */
    boolean reject(User loggedUser, Integer reservationId);

    /**
     * @return all reservation by given user that have not ended
     */
    List<ReservationDTO> findActiveByUserId(User loggedUser, Integer userId);
}
