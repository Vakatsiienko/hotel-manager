package com.vaka.repository;

import com.vaka.domain.Reservation;
import com.vaka.domain.Role;
import com.vaka.domain.RoomClass;
import com.vaka.domain.User;
import com.vaka.util.ApplicationContext;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by iaroslav on 02.12.16.
 */
public class ReservationRepositoryTest extends CrudRepositoryTest<Reservation> {
    private ReservationRepository reservationRepository = ApplicationContext.getBean(ReservationRepository.class);
    private CustomerRepository customerRepository = ApplicationContext.getBean(CustomerRepository.class);

    @Test
    public void findByRoomIdTest(Integer roomId){} //List<Reservation>

    @Test
    public void findConfirmedTest() {

    }

    @Test
    public void findRequestedTest() {
    }

    ;

    @Override
    protected CrudRepository<Reservation> getRepository() {
        return reservationRepository;
    }

    @Override
    protected Reservation createEntity() {
        Reservation reservation = new Reservation();
        reservation.setArrivalDate(LocalDate.of(2002, 10, 20));
        reservation.setDepartureDate(LocalDate.of(2002, 11, 20));
        reservation.setRequestedRoomClass(RoomClass.FIRST_CLASS);
        reservation.setGuests(20);
        reservation.setUser(customerRepository.create(new User("email@mail.m", "password", "name", Role.CUSTOMER, "+30987654321")));
        return reservation;
    }

}
