package com.vaka.hotel_manager.service;

import com.vaka.hotel_manager.EntityProviderUtil;
import com.vaka.hotel_manager.core.context.ApplicationContext;
import com.vaka.hotel_manager.domain.Room;
import com.vaka.hotel_manager.domain.RoomClass;
import com.vaka.hotel_manager.repository.CrudRepository;
import com.vaka.hotel_manager.repository.RoomClassRepository;
import com.vaka.hotel_manager.repository.RoomRepository;

/**
 * Created by Iaroslav on 12/16/2016.
 */
public class RoomServiceTest extends CrudServiceTest<Room> {

    private RoomRepository roomRepositoryMock = ApplicationContext.getInstance().getBean(RoomRepository.class);
    private RoomService roomService = ApplicationContext.getInstance().getBean(RoomService.class);
    private RoomClassRepository roomClassRepository = ApplicationContext.getInstance().getBean(RoomClassRepository.class); //TODO change to mock

    @Override
    protected CrudService<Room> getService() {
        return roomService;
    }

    @Override
    protected CrudRepository<Room> getMockedRepository() {
        return roomRepositoryMock;
    }

    @Override
    protected Room createEntity() {
        return EntityProviderUtil.createRoom(roomClassRepository.create(EntityProviderUtil.createOrGetStoredRoomClass("Standard")));
    }
}
