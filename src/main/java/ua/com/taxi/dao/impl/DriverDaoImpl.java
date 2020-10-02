package ua.com.taxi.dao.impl;

import ua.com.taxi.dao.DriverDao;
import ua.com.taxi.model.Driver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DriverDaoImpl implements DriverDao {
    @Override
    public List<Driver> findAll(Connection connection) throws SQLException {
        String query = "select id, name " +
                "FROM drivers ;";

        List<Driver> drivers = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Driver driver = extractDriver(resultSet);
                drivers.add(driver);
            }
        }
        return drivers;
    }

    private Driver extractDriver(ResultSet resultSet) throws SQLException {
        Driver driver = new Driver();
        driver.setId(resultSet.getInt("id"));
        driver.setName(resultSet.getString("name"));

        return driver;
    }
}
