package ua.com.taxi.dao.impl;

import ua.com.taxi.dao.RoleDao;
import ua.com.taxi.model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao {
    @Override
    public List<Role> findByUserId(Integer userId, Connection connection) throws SQLException {
        String query =
                "SELECT * FROM roles as r " +
                        "LEFT JOIN users_roles as ur on r.id = ur.role_id " +
                        "WHERE ur.user_id = ?;";

        List<Role> roles = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Role role = extractRole(resultSet);
                    roles.add(role);
                }
            }
        }
        return roles;
    }

    @Override
    public List<Role> findAll(Connection connection) throws SQLException {
        String query = "select id, name FROM roles;";

        List<Role> roles = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Role role = extractRole(resultSet);
                roles.add(role);
            }
        }
        return roles;
    }

    private Role extractRole(ResultSet resultSet) throws SQLException {
        Role role = new Role();
        role.setId(resultSet.getInt("id"));
        role.setName(resultSet.getString("name"));
        return role;
    }
}
