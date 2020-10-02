package ua.com.taxi.dao.impl;

import ua.com.taxi.dao.UserDao;
import ua.com.taxi.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    @Override
    public void create(User user, Connection connection) throws SQLException {
        String insertUser =
                "insert into users (name, phone, password, discount, balance) " +
                        "values (?, ?, ?, ?, ?) ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS);) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPhone());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setLong(4, user.getDiscount());
            preparedStatement.setLong(5, user.getBalance());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                user.setId(id);
            }
        }
    }

    @Override
    public void update(User user, Connection connection) throws SQLException {
        String updateUserQuery =
                "update users " +
                        "set " +
                        "name = ?, " +
                        "phone = ?, " +
                        "password = ?, " +
                        "discount = ?, " +
                        "balance = ? " +
                        "where id = ? ;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateUserQuery)) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPhone());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setLong(4, user.getDiscount());
            preparedStatement.setLong(5, user.getBalance());
            preparedStatement.setInt(6, user.getId());
            preparedStatement.execute();
        }
    }

    @Override
    public Optional<User> findById(Integer id, Connection connection) throws SQLException {
        String query = "select id, name, phone, password, discount, balance FROM users WHERE id = ?;";

        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user = extractUser(resultSet);
                }
            }
        }
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByPhone(String phone, Connection connection) throws SQLException {
        String query = "select id, name, phone, password, discount, balance FROM users WHERE phone = ?;";

        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, phone);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user = extractUser(resultSet);
                }
            }
        }
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll(Connection connection) throws SQLException {
        String query = "select id, name, phone, password, discount, balance FROM users;";

        List<User> users = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                User user = extractUser(resultSet);
                users.add(user);
            }
        }
        return users;
    }

    @Override
    public void updateRolesForUser(int userId, List<Integer> roles, Connection connection) throws SQLException {
        deleteUserRoles(userId, connection);
        insertUserRoles(userId, roles, connection);
    }

    private void deleteUserRoles(int userId, Connection connection) throws SQLException {
        String deleteQuery = "delete from users_roles where user_id = ?";

        try (PreparedStatement deletePreparedStatement = connection.prepareStatement(deleteQuery)) {
            deletePreparedStatement.setInt(1, userId);
            deletePreparedStatement.execute();
        }
    }

    private void insertUserRoles(int userId, List<Integer> selectedRoles, Connection connection) throws SQLException {
        String insertQuery = "insert into users_roles (user_id, role_id) " +
                "values (?, ?);";

        try (PreparedStatement insertPreparedStatement = connection.prepareStatement(insertQuery)) {
            for (int selectedRole : selectedRoles) {
                insertPreparedStatement.setInt(1, userId);
                insertPreparedStatement.setInt(2, selectedRole);
                insertPreparedStatement.execute();
            }
        }
    }

    private User extractUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setPhone(resultSet.getString("phone"));
        user.setPassword(resultSet.getString("password"));
        user.setDiscount(resultSet.getInt("discount"));
        user.setBalance(resultSet.getInt("balance"));
        return user;
    }
}
