package ua.com.taxi.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.taxi.dao.ConnectionFactory;
import ua.com.taxi.dao.RoleDao;
import ua.com.taxi.dao.UserDao;
import ua.com.taxi.exception.DaoException;
import ua.com.taxi.exception.RollbackException;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final String FAILED_OPEN_CONNECTION_MESSAGE = "Database connection opening failed";

    private UserDao userDao;
    private RoleDao roleDao;

    public UserServiceImpl(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

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
            LOGGER.error("Finding user by ID failed");
            throw new DaoException("Finding user by ID failed", e);
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
            LOGGER.error("Finding user by phone failed");
            throw new DaoException("Finding user by phone failed", e);
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
            LOGGER.error("Finding all users failed");
            throw new DaoException("Finding all users failed", e);
        }
        return users;
    }

    @Override
    public void registrate(UserRegistrationDto userDto) {
        User user = buildUser(userDto);

        try (Connection connection = ConnectionFactory.getConnection()) {
            try {
                connection.setAutoCommit(false);
                userDao.create(user, connection);
                // TODO get from DB USER role id
                userDao.updateRolesForUser(user.getId(), Collections.singletonList(1), connection);
                connection.commit();
            } catch (Exception e) {
                String message = String.format("Registration user failed '%s'", userDto);
                LOGGER.error(message);
                connection.rollback();
                throw new RollbackException(message, e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            LOGGER.error(FAILED_OPEN_CONNECTION_MESSAGE);
            throw new DaoException(FAILED_OPEN_CONNECTION_MESSAGE, e);
        }
    }

    private User buildUser(UserRegistrationDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        String hashPassword = Hasher.hash(userDto.getPassword());
        user.setPassword(hashPassword);
        user.setPhone(userDto.getPhone());
        return user;
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
                String message = String.format("Updating user '%s' failed", id);
                LOGGER.error(message);
                connection.rollback();
                throw new RollbackException(message, e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            LOGGER.error(FAILED_OPEN_CONNECTION_MESSAGE);
            throw new DaoException(FAILED_OPEN_CONNECTION_MESSAGE, e);
        }
    }
}
