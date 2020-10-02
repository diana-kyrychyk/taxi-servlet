package ua.com.taxi.dao;

import ua.com.taxi.model.Address;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AddressDao {

    Optional<Address> findById(int id, Connection connection) throws SQLException;
    List<Address> findAll(Connection connection) throws SQLException;
}
