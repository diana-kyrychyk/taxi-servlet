package ua.com.taxi.dao;

import ua.com.taxi.model.Order;
import ua.com.taxi.model.OrderStatus;
import ua.com.taxi.model.dto.OrderConfirmDto;
import ua.com.taxi.model.dto.SearchParameters;
import ua.com.taxi.model.dto.OrderListDto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface OrderDao {

    void create(Order order, Connection connection) throws SQLException;

    List<OrderListDto> findAllListDto(Connection connection) throws SQLException;

    int findCount(SearchParameters searchParameters, Connection connection) throws SQLException;

    List<OrderListDto> findAllListDto(SearchParameters searchParameters, Connection connection) throws SQLException;

    Optional<OrderConfirmDto> findOrderConfirmDto(Connection connection, Integer id) throws SQLException;

    boolean startOrder(int orderId, int carId, int driverId, OrderStatus orderStatus, Connection connection) throws SQLException;

    Optional<Integer> findCarIdByOrder(int orderId, Connection connection) throws SQLException;

    boolean changeOrderStatus(int orderId, OrderStatus orderStatus, Connection connection) throws SQLException;

    Optional<Integer> findByUserAndStatus(int userId, OrderStatus orderStatus, Connection connection) throws SQLException;
}


