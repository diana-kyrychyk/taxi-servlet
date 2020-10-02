package ua.com.taxi.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.taxi.dao.AddressDao;
import ua.com.taxi.dao.ConnectionFactory;
import ua.com.taxi.dao.impl.AddressDaoImpl;
import ua.com.taxi.exception.DaoException;
import ua.com.taxi.model.Address;
import ua.com.taxi.service.AddressService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddressServiceImpl implements AddressService {

    private AddressDao addressDao = new AddressDaoImpl();
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressServiceImpl.class);

    @Override
    public List<Address> findAll() {
        List<Address> address = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection()) {
            address = addressDao.findAll(connection);
        } catch (SQLException e) {
            LOGGER.error("Finding all addresses failed");
            throw new DaoException("Finding all addresses failed", e);
        }
        return address;
    }
}
