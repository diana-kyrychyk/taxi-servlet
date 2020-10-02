package ua.com.taxi.dao.impl;

import ua.com.taxi.dao.CarDao;
import ua.com.taxi.model.Car;
import ua.com.taxi.model.CarStatus;
import ua.com.taxi.model.Category;
import ua.com.taxi.model.Driver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarDaoImpl implements CarDao {
    @Override
    public List<Car> findAll(Connection connection) throws SQLException {
        String query = "select c.id, c.brand, c.model, c.license_plate, c.capacity, c.category, c.status, d.id as driver_id, d.name as driver_name " +
                "FROM cars as c " +
                "LEFT JOIN drivers d on c.driver_id = d.id ;";

        List<Car> cars = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Car car = extractCar(resultSet);
                cars.add(car);
            }
        }
        return cars;
    }

    @Override
    public Optional<Car> find(Category category, int passengersCount, CarStatus status, Connection connection) throws SQLException {
        String query = "select c.id, c.brand, c.model, c.license_plate, c.capacity, c.category, c.status, d.id as driver_id, d.name as driver_name " +
                "FROM cars as c " +
                "LEFT JOIN drivers d on c.driver_id = d.id " +
                "WHERE c.category = ? " +
                "AND c.capacity >= ? " +
                "AND c.status = ? " +
                "LIMIT 1 ;";

        Car car = null;
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, category.toString());
            statement.setInt(2, passengersCount);
            statement.setString(3, status.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                car = extractCar(resultSet);
            }
        }
        return Optional.ofNullable(car);
    }

    @Override
    public boolean isCarInStatus(int id, CarStatus carStatus, Connection connection) throws SQLException {
        String query = "select c.id " +
                "FROM cars as c " +
                "WHERE c.id = ? " +
                "AND c.status = ? ;";

        boolean result = false;
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            statement.setString(2, carStatus.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                result = true;
            }
        }
        return result;
    }

    private Car extractCar(ResultSet resultSet) throws SQLException {
        Car car = new Car();
        Driver driver = null;
        car.setId(resultSet.getInt("id"));
        car.setBrand(resultSet.getString("brand"));
        car.setModel(resultSet.getString("model"));
        car.setLicensePlate(resultSet.getString("license_plate"));
        car.setCapacity(resultSet.getInt("capacity"));

        String categoryStr = resultSet.getString("category");
        Category category = categoryStr != null ? Category.valueOf(categoryStr) : null;
        car.setCategory(category);

        String carStatusStr = resultSet.getString("status");
        CarStatus status = carStatusStr != null ? CarStatus.valueOf(carStatusStr) : null;
        car.setStatus(status);

        Integer driverId = resultSet.getObject("driver_id", Integer.class);
        if (driverId != null) {
            driver = extractDriver(resultSet);
        }
        car.setDriver(driver);
        return car;
    }

    private Driver extractDriver(ResultSet resultSet) throws SQLException {
        Driver driver = new Driver();
        driver.setId(resultSet.getInt("driver_id"));
        driver.setName(resultSet.getString("driver_name"));
        return driver;
    }

    @Override
    public int findDriverIdByCar(int carId, Connection connection) throws SQLException {
        String query = "select c.driver_id " +
                "FROM cars as c " +
                "WHERE c.id = ? ;";

        int driverId = 0;
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, carId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                driverId = resultSet.getInt("driver_id");
            }
        }
        return driverId;
    }

    @Override
    public boolean changeCarStatus(int carId, CarStatus carStatus, Connection connection) throws SQLException {
        String query =
                "update cars " +
                        "set " +
                        "status = ? " +
                        "where id = ? ;";

        boolean result = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, carStatus.toString());
            preparedStatement.setInt(2, carId);
            result = preparedStatement.execute();
        }
        return result;
    }
}
