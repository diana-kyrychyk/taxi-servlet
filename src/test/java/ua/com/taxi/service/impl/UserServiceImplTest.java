package ua.com.taxi.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ua.com.taxi.dao.ConnectionFactory;
import ua.com.taxi.dao.RoleDao;
import ua.com.taxi.dao.UserDao;
import ua.com.taxi.model.Role;
import ua.com.taxi.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ConnectionFactory.class)
public class UserServiceImplTest {

    int id = 1;
    String name = "testName";
    String phone = "+380987770000";
    String password = "passwordTest";
    List<Role> roles = buildRoles();
    Optional<User> user = buildUser();

    UserDao userDaoMock = mock(UserDao.class);
    RoleDao roleDaoMock = mock(RoleDao.class);
    Connection connectionMock = mock(Connection.class);

    UserServiceImpl userService = new UserServiceImpl(userDaoMock, roleDaoMock);

    @Test
    public void shouldFindUserByPhone() throws SQLException {
        PowerMockito.mockStatic(ConnectionFactory.class);
        given(ConnectionFactory.getConnection()).willReturn(connectionMock);
        when(userDaoMock.findByPhone(eq(phone), any(Connection.class))).thenReturn(user);
        when(roleDaoMock.findByUserId(eq(id), any(Connection.class))).thenReturn(roles);

        Optional<User> actualUser = userService.findByPhone(phone);

        assertTrue(actualUser.isPresent());
        assertEquals(roles, actualUser.get().getRoles());
    }

    private Optional<User> buildUser() {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setPhone(phone);
        user.setPassword(password);
        user.setRoles(roles);
        return Optional.of(user);
    }

    private List<Role> buildRoles() {
        Role role = new Role();
        role.setName(Role.ADMIN);
        return Arrays.asList(role);
    }

}
