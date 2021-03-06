package com.vaka.hotel_manager.service;

import com.vaka.hotel_manager.EntityProviderUtil;
import com.vaka.hotel_manager.core.context.ApplicationContextHolder;
import com.vaka.hotel_manager.domain.entity.Room;
import com.vaka.hotel_manager.repository.CrudRepository;
import com.vaka.hotel_manager.repository.RoomClassRepository;
import com.vaka.hotel_manager.repository.RoomRepository;

import java.util.Optional;

import static org.mockito.Mockito.when;
/**
 * Created by Iaroslav on 12/16/2016.
 */
public class RoomServiceTest extends CrudServiceTest<Room> {

    private RoomRepository roomRepositoryMock = ApplicationContextHolder.getContext().getBean(RoomRepository.class);
    private RoomService roomService = ApplicationContextHolder.getContext().getBean(RoomService.class);
    private RoomClassRepository roomClassRepositoryMock = ApplicationContextHolder.getContext().getBean(RoomClassRepository.class);

    @Override
    protected CrudService<Room> getService() {
        return roomService;
    }

    @Override
    protected void beforeCreate() {
        when(roomClassRepositoryMock.getByName("Standard")).thenReturn(Optional.of(EntityProviderUtil.createRoomClass("Standard")));
    }

    @Override
    protected void beforeUpdate() {
        when(roomClassRepositoryMock.getByName("Standard")).thenReturn(Optional.of(EntityProviderUtil.createRoomClass("Standard")));
    }

    @Override
    protected CrudRepository<Room> getMockedRepository() {
        return roomRepositoryMock;
    }

    @Override
    protected Room createEntity() {
        return EntityProviderUtil.createRoom(EntityProviderUtil.createRoomClass("Standard"));
    }
}
