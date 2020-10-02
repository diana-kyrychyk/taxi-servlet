package ua.com.taxi.service.impl;

import ua.com.taxi.dao.ConnectionFactory;
import ua.com.taxi.dao.RoleDao;
import ua.com.taxi.dao.UserDao;
import ua.com.taxi.dao.impl.RoleDaoImpl;
import ua.com.taxi.dao.impl.UserDaoImpl;
import ua.com.taxi.model.Role;
import ua.com.taxi.model.User;
import ua.com.taxi.model.dto.UserRegistrationDto;
import ua.com.taxi.service.UserService;
import ua.com.taxi.util.Hasher;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();
    private RoleDao roleDao = new RoleDaoImpl();

    @Override
    public Optional<User> findById(Integer id) {
        User user = null;
        try (Connection connection = ConnectionFactory.getConnection()) {
            Optional<User> userOptional = userDao.findById(id, connection);
            if (userOptional.isPresent()) {
                user = userOptional.get();
                List<Role> roles = roleDao.findByUserId(user.getId(), connection);
                user.setRoles(roles);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        User user = null;
        try (Connection connection = ConnectionFactory.getConnection()) {
            Optional<User> userOptional = userDao.findByPhone(phone, connection);
            if (userOptional.isPresent()) {
                user = userOptional.get();
                List<Role> roles = roleDao.findByUserId(user.getId(), connection);
                user.setRoles(roles);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAllUsers() {
        //TODO use UserListDto
        List<User> users = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection()) {
            users = userDao.findAll(connection);
            for (User user : users) {
                List<Role> roles = roleDao.findByUserId(user.getId(), connection);
                user.setRoles(roles);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void registrate(UserRegistrationDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        String hashPassword = Hasher.hash(userDto.getPassword());
        user.setPassword(hashPassword);
        user.setPhone(userDto.getPhone());

        try (Connection connection = ConnectionFactory.getConnection()) {
            try {
                connection.setAutoCommit(false);
                userDao.create(user, connection);
                // TODO get from DB USER role id
                userDao.updateRolesForUser(user.getId(), Collections.singletonList(1), connection);
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Integer id, String name, List<Integer> selectedRoles) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            try {
                connection.setAutoCommit(false);

                Optional<User> userOptional = userDao.findById(id, connection);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    user.setName(name);
                    userDao.update(user, connection);
                    userDao.updateRolesForUser(user.getId(), selectedRoles, connection);
                }
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
