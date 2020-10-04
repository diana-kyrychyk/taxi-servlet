package ua.com.taxi.dao.impl;

import ua.com.taxi.dao.OrderDao;
import ua.com.taxi.model.Category;
import ua.com.taxi.model.Order;
import ua.com.taxi.model.OrderStatus;
import ua.com.taxi.model.dto.OrderConfirmDto;
import ua.com.taxi.model.dto.SearchParameters;
import ua.com.taxi.model.dto.OrderListDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

public class OrderDaoImpl implements OrderDao {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void create(Order order, Connection connection) throws SQLException {
        String insertUser =
                "insert into orders (departure_address_id, arrival_address_id, passengers_count, category, fare, discount, final_fare, status, creation_date, user_id ) " +
                        "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS);) {

            preparedStatement.setInt(1, order.getDepartureAddress().getId());
            preparedStatement.setInt(2, order.getArrivalAddress().getId());
            preparedStatement.setInt(3, order.getPassengersCount());
            preparedStatement.setString(4, order.getCategory().toString());
            preparedStatement.setLong(5, order.getFare());
            preparedStatement.setLong(6, order.getDiscount());
            preparedStatement.setLong(7, order.getFinalFare());
            preparedStatement.setString(8, order.getStatus().toString());
            preparedStatement.setTimestamp(9, Timestamp.valueOf(order.getCreationDate()));
            preparedStatement.setInt(10, order.getUser().getId());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                order.setId(id);
            }
        }
    }

    @Override
    public List<OrderListDto> findAllListDto(Connection connection) throws SQLException {
        String query = "select " +
                "o.id, " +
                "u.phone, " +
                "o.category, " +
                "o.fare, " +
                "o.status, " +
                "o.creation_date, " +
                "ad.street  as departure_street,  " +
                "ad.building as departure_building, " +
                "aa.street as arrival_street, " +
                "aa.building as arrival_building, " +
                "c.license_plate " +
                "FROM orders as o " +
                "LEFT JOIN cars c on o.car_id = c.id " +
                "LEFT JOIN users u on o.user_id = u.id " +
                "LEFT JOIN addresses ad on o.departure_address_id = ad.id " +
                "LEFT JOIN addresses aa on o.arrival_address_id = aa.id; ";

        List<OrderListDto> orders = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                OrderListDto order = extractOrder(resultSet);
                orders.add(order);
            }
        }
        return orders;
    }

    @Override
    public int findCount(SearchParameters searchParameters, Connection connection) throws SQLException {

        String query = "SELECT " +
                "COUNT(*) as orders_number " +
                "FROM orders as o " +
                "WHERE 1 = 1 " +
                "{FilterByQuery} ; ";

        query = query.replace("{FilterByQuery}", specifyFilterQuery(searchParameters));
        int orderCount = 0;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                orderCount = resultSet.getInt("orders_number");
            }
        }
        return orderCount;
    }

    @Override
    public List<OrderListDto> findAllListDto(SearchParameters searchParameters, Connection connection) throws SQLException {

        String query = "select " +
                "o.id, " +
                "u.phone, " +
                "o.category, " +
                "o.fare, " +
                "o.status, " +
                "o.creation_date, " +
                "ad.street  as departure_street,  " +
                "ad.building as departure_building, " +
                "aa.street as arrival_street, " +
                "aa.building as arrival_building, " +
                "c.license_plate " +
                "FROM orders as o " +
                "LEFT JOIN cars c on o.car_id = c.id " +
                "LEFT JOIN users u on o.user_id = u.id " +
                "LEFT JOIN addresses ad on o.departure_address_id = ad.id " +
                "LEFT JOIN addresses aa on o.arrival_address_id = aa.id " +
                "WHERE 1 = 1 " +
                "{FilterByQuery}" +
                "{OrderByQuery} " +
                "{OffsetQuery} " +
                "{LimitQuery} ; ";

        query = query.replace("{FilterByQuery}", specifyFilterQuery(searchParameters));
        query = query.replace("{OrderByQuery}", specifyOrderByQuery(searchParameters.getSortType()));
        query = query.replace("{OffsetQuery}", specifyOffsetQuery(searchParameters));
        query = query.replace("{LimitQuery}", specifyLimitQuery(searchParameters));

        List<OrderListDto> orders = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                OrderListDto order = extractOrder(resultSet);
                orders.add(order);
            }
        }
        return orders;
    }

    private String specifyOrderByQuery(String sortType) {
        return
                SearchParameters.SORT_BY_DATE.equals(sortType) ? " ORDER BY o.creation_date DESC "
                        : SearchParameters.SORT_BY_FARE.equals(sortType) ? " ORDER BY o.fare DESC "
                        : "";
    }

    private String specifyFilterQuery(SearchParameters searchParameters) {
        String filterQuery = "";
        if (searchParameters.getSelectedPassenger() != null) {
            filterQuery = filterQuery
                    .concat(" AND o.user_id = {user_id} ")
                    .replace("{user_id}", String.valueOf(searchParameters.getSelectedPassenger()));
        }

        if (searchParameters.getStartDate() != null ) {
            filterQuery = filterQuery
                    .concat(" AND o.creation_date > '{date}' ")
                    .replace("{date}", DATE_TIME_FORMATTER.format(searchParameters.getStartDate()));
        }

        if (searchParameters.getEndDate() != null ) {
            filterQuery = filterQuery
                    .concat(" AND o.creation_date < '{date}' ")
                    .replace("{date}", DATE_TIME_FORMATTER.format(searchParameters.getEndDate()));
        }

        return filterQuery;
    }

    private String specifyOffsetQuery(SearchParameters searchParameters) {
        int offset = searchParameters.getPageSize() * (searchParameters.getPageNumber() - 1);
        return " OFFSET {count} "
                .replace("{count}", String.valueOf(offset));
    }

    private String specifyLimitQuery(SearchParameters searchParameters) {
        return " LIMIT {count} "
                .replace("{count}", String.valueOf(searchParameters.getPageSize()));
    }

    private OrderListDto extractOrder(ResultSet resultSet) throws SQLException {
        OrderListDto order = new OrderListDto();
        order.setOrderId(resultSet.getInt("id"));
        order.setUserPhone(resultSet.getString("phone"));

        String departureStreet = resultSet.getString("departure_street");
        String departureBuilding = resultSet.getString("departure_building");
        String departureAddress = departureStreet.concat(", ").concat(departureBuilding);

        order.setDepartureAddress(departureAddress);

        String arrivalStreet = resultSet.getString("arrival_street");
        String arrivalBuilding = resultSet.getString("arrival_building");
        String arrivalAddress = arrivalStreet.concat(", ").concat(arrivalBuilding);

        order.setArrivalAddress(arrivalAddress);

        String categoryStr = resultSet.getString("category");
        Category category = categoryStr != null ? Category.valueOf(categoryStr) : null;
        order.setCategory(category);

        order.setLicensePlate(resultSet.getString("license_plate"));
        order.setFare(resultSet.getLong("fare"));

        Timestamp creationDateTs = resultSet.getTimestamp("creation_date");
        LocalDateTime creationDate = LocalDateTime.ofInstant(creationDateTs.toInstant(), TimeZone.getDefault().toZoneId());
        order.setCreationDate(creationDate);

        String statusStr = resultSet.getString("status");
        OrderStatus status = statusStr != null ? OrderStatus.valueOf(statusStr) : null;
        order.setStatus(status);

        return order;
    }

    @Override
    public Optional<OrderConfirmDto> findOrderConfirmDto(Connection connection, Integer id) throws SQLException {
        String query = "select " +
                "o.id, " +
                "o.category, " +
                "o.fare, " +
                "o.final_fare, " +
                "o.passengers_count, " +
                "ad.street  as departure_street,  " +
                "ad.building as departure_building, " +
                "aa.street as arrival_street, " +
                "aa.building as arrival_building, " +
                "u.phone " +
                "FROM orders as o " +
                "LEFT JOIN users u on o.user_id = u.id " +
                "LEFT JOIN addresses ad on o.departure_address_id = ad.id " +
                "LEFT JOIN addresses aa on o.arrival_address_id = aa.id " +
                "WHERE o.id = ? " +
                "AND o.status = ? ; ";

        OrderConfirmDto orderConfirmDto = null;
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            statement.setString(2, OrderStatus.NEW.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                orderConfirmDto = extractOrderConfirmation(resultSet);
            }
        }
        return Optional.ofNullable(orderConfirmDto);
    }

    private OrderConfirmDto extractOrderConfirmation(ResultSet resultSet) throws SQLException {
        OrderConfirmDto order = new OrderConfirmDto();
        order.setOrderId(resultSet.getInt("id"));

        String departureStreet = resultSet.getString("departure_street");
        String departureBuilding = resultSet.getString("departure_building");
        String departureAddress = departureStreet.concat(", ").concat(departureBuilding);

        order.setDepartureAddress(departureAddress);

        String arrivalStreet = resultSet.getString("arrival_street");
        String arrivalBuilding = resultSet.getString("arrival_building");
        String arrivalAddress = arrivalStreet.concat(", ").concat(arrivalBuilding);

        order.setArrivalAddress(arrivalAddress);

        String categoryStr = resultSet.getString("category");
        Category category = categoryStr != null ? Category.valueOf(categoryStr) : null;
        order.setCategory(category);

        order.setPassengersCount(resultSet.getInt("passengers_count"));

        order.setFare(resultSet.getLong("fare"));
        order.setFinalFare(resultSet.getLong("final_fare"));
        order.setUserPhone(resultSet.getString("phone"));

        return order;
    }

    @Override
    public boolean startOrder(int orderId, int carId, int driverId, OrderStatus orderStatus, Connection connection) throws SQLException {
        String query =
                "update orders " +
                        "set " +
                        "car_id = ?, " +
                        "driver_id = ?, " +
                        "status = ? " +
                        "where id = ? ;";

        boolean result = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, carId);
            preparedStatement.setInt(2, driverId);
            preparedStatement.setString(3, orderStatus.toString());
            preparedStatement.setInt(4, orderId);
            result = preparedStatement.execute();
        }
        return result;
    }

    @Override
    public Optional<Integer> findCarIdByOrder(int orderId, Connection connection) throws SQLException {
        String query = "select o.car_id " +
                "FROM orders as o " +
                "WHERE o.id = ? ;";

        Integer carId = null;
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, orderId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                carId = resultSet.getInt("car_id");
            }
        }
        return Optional.ofNullable(carId);
    }

    @Override
    public boolean changeOrderStatus(int orderId, OrderStatus orderStatus, Connection connection) throws SQLException {
        String query =
                "update orders " +
                        "set " +
                        "status = ? " +
                        "where id = ? ;";

        boolean result = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, orderStatus.toString());
            preparedStatement.setInt(2, orderId);
            result = preparedStatement.execute();
        }
        return result;
    }

    @Override
    public Optional<Integer> findByUserAndStatus(int userId, OrderStatus orderStatus, Connection connection) throws SQLException {
        String query = "select o.id " +
                "FROM orders as o " +
                "WHERE o.user_id = ? " +
                "AND o.status = ? " +
                "LIMIT 1 ;";

        Integer orderId = null;
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setString(2, orderStatus.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                orderId = resultSet.getInt("id");
            }
        }
        return Optional.ofNullable(orderId);
    }
}
