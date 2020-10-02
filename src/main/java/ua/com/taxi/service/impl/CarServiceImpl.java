package ua.com.taxi.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.taxi.dao.CarDao;
import ua.com.taxi.dao.ConnectionFactory;
import ua.com.taxi.dao.impl.CarDaoImpl;
import ua.com.taxi.exception.DaoException;
import ua.com.taxi.model.Car;
import ua.com.taxi.service.CarService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarServiceImpl implements CarService {

    private CarDao carDao = new CarDaoImpl();
    private static final Logger LOGGER = LoggerFactory.getLogger(CarServiceImpl.class);

    @Override
    public List<Car> findAll() {
        List<Car> cars = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection()) {
            cars = carDao.findAll(connection);
        } catch (SQLException e) {
            LOGGER.error("Finding all cars failed");
            throw new DaoException("Finding all cars failed", e);
        }
        return cars;
    }
}
