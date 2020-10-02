package ua.com.taxi.dao.impl;

import ua.com.taxi.dao.AddressDao;
import ua.com.taxi.model.Address;
import ua.com.taxi.model.Point;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddressDaoImpl implements AddressDao {


    @Override
    public Optional<Address> findById(int id, Connection connection) throws SQLException {
        String query = "select id, street, building, latitude, longtitude " +
                "FROM addresses " +
                "WHERE id = ? ;";

        Address address = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    address = extractAddress(resultSet);
                }
            }
        }
        return Optional.ofNullable(address);
    }

    @Override
    public List<Address> findAll(Connection connection) throws SQLException {
        String query = "select id, street, building, latitude, longtitude " +
                "FROM addresses ;";

        List<Address> addresses = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Address address = extractAddress(resultSet);
                addresses.add(address);
            }
        }
        return addresses;
    }

    private Address extractAddress(ResultSet resultSet) throws SQLException {
        Address address = new Address();

        address.setId(resultSet.getInt("id"));
        address.setStreet(resultSet.getString("street"));
        address.setBuilding(resultSet.getString("building"));

        Point point = extractPoint(resultSet);
        address.setPoint(point);
        return address;
    }

    private Point extractPoint(ResultSet resultSet) throws SQLException {
        Point point = new Point();
        point.setLatitude(resultSet.getDouble("latitude"));
        point.setLongtitude(resultSet.getDouble("longtitude"));
        return point;
    }
}
