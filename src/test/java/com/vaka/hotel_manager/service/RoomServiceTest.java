package com.vaka.hotel_manager.service;

import com.vaka.hotel_manager.EntityProviderUtil;
import com.vaka.hotel_manager.context.ApplicationContext;
import com.vaka.hotel_manager.domain.Room;
import com.vaka.hotel_manager.repository.CrudRepository;
import com.vaka.hotel_manager.repository.RoomRepository;

/**
 * Created by Iaroslav on 12/16/2016.
 */
public class RoomServiceTest extends CrudServiceTest<Room> {

    private RoomRepository roomRepositoryMock = ApplicationContext.getInstance().getBean(RoomRepository.class);
    private RoomService roomService = ApplicationContext.getInstance().getBean(RoomService.class);

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
        return EntityProviderUtil.createRoom();
    }
}
