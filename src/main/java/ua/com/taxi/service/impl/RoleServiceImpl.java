package ua.com.taxi.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.taxi.dao.ConnectionFactory;
import ua.com.taxi.dao.RoleDao;
import ua.com.taxi.exception.DaoException;
import ua.com.taxi.model.Role;
import ua.com.taxi.service.RoleService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleServiceImpl implements RoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);

    private RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public List<Role> findByUser(Integer userId) {
        List<Role> roles = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection()) {
            roles = roleDao.findByUserId(userId, connection);

        } catch (SQLException e) {
            LOGGER.error("Finding roles by user failed");
            throw new DaoException("Finding roles by user failed", e);
        }
        return roles;
    }

    @Override
    public List<Role> findAll() {
        List<Role> roles = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection()) {
            roles = roleDao.findAll(connection);

        } catch (SQLException e) {
            LOGGER.error("Finding all roles failed");
            throw new DaoException("Finding all roles failed", e);
        }
        return roles;
    }
}
