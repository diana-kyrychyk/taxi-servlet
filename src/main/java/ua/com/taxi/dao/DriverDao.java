package ua.com.taxi.dao;

import ua.com.taxi.model.Driver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface DriverDao {

    List<Driver> findAll(Connection connection) throws SQLException;
}
