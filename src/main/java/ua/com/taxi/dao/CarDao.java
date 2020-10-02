package ua.com.taxi.dao;

import ua.com.taxi.model.Car;
import ua.com.taxi.model.CarStatus;
import ua.com.taxi.model.Category;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CarDao {

    List<Car> findAll(Connection connection) throws SQLException;
    Optional<Car> find(Category category, int passengersCount, CarStatus status, Connection connection) throws SQLException;
    boolean isCarInStatus(int id, CarStatus carStatus, Connection connection) throws SQLException;

    int findDriverIdByCar(int carId, Connection connection) throws SQLException;

    boolean changeCarStatus(int carId, CarStatus carStatus, Connection connection) throws SQLException;
}
