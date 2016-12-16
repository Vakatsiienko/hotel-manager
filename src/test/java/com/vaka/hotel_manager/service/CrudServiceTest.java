package com.vaka.hotel_manager.service;

import com.vaka.hotel_manager.domain.BaseEntity;
import com.vaka.hotel_manager.domain.Role;
import com.vaka.hotel_manager.domain.User;
import com.vaka.hotel_manager.repository.CrudRepository;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * @author Iaroslav
 * @since 22.12.2014 20:50
 */
public abstract class CrudServiceTest<Entity extends BaseEntity> {
    @Test
    public void testCreate() {
        Entity e = createEntity();
        CrudRepository<Entity> repository = getMockedRepository();
        getService().create(getLoggedManager(), e);

        verify(repository).create(e);
    }

    @Test
    public void testGeneratingCreatedDatetimeOnCreate() {
        Entity e = createEntity();
        e.setCreatedDatetime(LocalDateTime.MIN);
        Entity created = getService().create(getLoggedManager(), e);

        Assert.assertNotEquals(e.getCreatedDatetime(), created.getCreatedDatetime());
    }

    @Test
    public void testGetById() {
        Entity created = createEntity();
        CrudRepository<Entity> repository = getMockedRepository();
        when(repository.getById(1)).thenReturn(Optional.of(created));

        Optional<Entity> read = getService().getById(getLoggedManager(), 1);
        Assert.assertNotNull(read);
        Assert.assertEquals(read.get(), created);
    }

    @Test
    public void testUpdateTrue() {
        final Entity oldEntity = createEntity();
        final Entity newEntity = createEntity();
        final CrudRepository<Entity> repository = getMockedRepository();
        User loggedUser = getLoggedManager();
        when(repository.update(oldEntity.getId(), newEntity)).thenReturn(true);

        boolean b = getService().update(loggedUser, oldEntity.getId(), newEntity);
        Assert.assertTrue(b);
    }
    @Test
    public void testUpdateFalse() {
        final Entity oldEntity = createEntity();
        final Entity newEntity = createEntity();
        final CrudRepository<Entity> repository = getMockedRepository();
        User loggedUser = getLoggedManager();
        when(repository.update(oldEntity.getId(), newEntity)).thenReturn(false);

        boolean b = getService().update(loggedUser, oldEntity.getId(), newEntity);
        Assert.assertTrue(b);

    }

    @Test
    public void testDeleteTrue() {
        final CrudRepository<Entity> dao = getMockedRepository();
        final Entity e = createEntity();
        final boolean deleted = true;
        when(dao.delete(e.getId())).thenReturn(true);

        boolean b = getService().delete(getLoggedManager(), e.getId());
        Assert.assertEquals(b, deleted);
    }

    @Test
    public void testDeleteFalse() {
        final CrudRepository<Entity> dao = getMockedRepository();
        final Entity e = createEntity();
        final boolean deleted = false;
        when(dao.delete(e.getId())).thenReturn(false);

        boolean b = getService().delete(getLoggedManager(), e.getId());
        Assert.assertEquals(b, deleted);
    }

    protected User getLoggedManager(){
        User user = new User();
        user.setRole(Role.MANAGER);
        return user;
    }
    protected abstract CrudService<Entity> getService();

    protected abstract CrudRepository<Entity> getMockedRepository();

    protected abstract Entity createEntity();
}
