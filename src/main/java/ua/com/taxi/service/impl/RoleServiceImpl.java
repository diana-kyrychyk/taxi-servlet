package ua.com.taxi.service.impl;

import ua.com.taxi.dao.ConnectionFactory;
import ua.com.taxi.dao.RoleDao;
import ua.com.taxi.dao.impl.RoleDaoImpl;
import ua.com.taxi.model.Role;
import ua.com.taxi.service.RoleService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleServiceImpl implements RoleService {

    private RoleDao roleDao = new RoleDaoImpl();

    @Override
    public List<Role> findByUser(Integer userId) {
        List<Role> roles = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection()) {
            roles = roleDao.findByUserId(userId, connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    @Override
    public List<Role> findAll() {
        List<Role> roles = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection()) {
            roles = roleDao.findAll(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }
}
