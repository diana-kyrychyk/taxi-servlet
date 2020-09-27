package ua.com.taxi.dao;

import ua.com.taxi.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserDao {

    void create(User user, Connection connection) throws SQLException;
    void update(User user, Connection connection) throws SQLException;
    void updateRolesForUser(int userId, List<Integer> roles, Connection connection) throws SQLException;
    Optional<User> findByPhone(String phone, Connection connection) throws SQLException;
    List<User> findAll(Connection connection) throws SQLException;

    Optional<User> findById(Integer id, Connection connection) throws SQLException;
}
