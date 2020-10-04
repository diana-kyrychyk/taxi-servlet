package ua.com.taxi.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.taxi.dao.ConnectionFactory;
import ua.com.taxi.dao.DriverDao;
import ua.com.taxi.exception.DaoException;
import ua.com.taxi.model.Driver;
import ua.com.taxi.service.DriverService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverServiceImpl implements DriverService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverServiceImpl.class);
    private DriverDao driverDao;

    public DriverServiceImpl(DriverDao driverDao) {
        this.driverDao = driverDao;
    }

    @Override
    public List<Driver> findAll() {
        List<Driver> drivers = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection()) {
            drivers = driverDao.findAll(connection);
        } catch (SQLException e) {
            LOGGER.error("Finding all drivers failed");
            throw new DaoException("Finding all drivers failed", e);
        }
        return drivers;
    }
}
