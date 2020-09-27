package ua.com.taxi.dao;

import ua.com.taxi.model.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface RoleDao {

    List<Role> findByUserId(Integer userId, Connection connection) throws SQLException;
    List<Role> findAll(Connection connection) throws SQLException;
}
