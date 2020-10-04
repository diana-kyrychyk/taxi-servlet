package ua.com.taxi.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.taxi.dao.AddressDao;
import ua.com.taxi.dao.CarDao;
import ua.com.taxi.dao.ConnectionFactory;
import ua.com.taxi.dao.OrderDao;
import ua.com.taxi.dao.UserDao;
import ua.com.taxi.exception.DaoException;
import ua.com.taxi.exception.EntityNotFoundException;
import ua.com.taxi.exception.RollbackException;
import ua.com.taxi.model.Address;
import ua.com.taxi.model.Car;
import ua.com.taxi.model.CarStatus;
import ua.com.taxi.model.Order;
import ua.com.taxi.model.OrderStatus;
import ua.com.taxi.model.User;
import ua.com.taxi.model.dto.OrderConfirmDto;
import ua.com.taxi.model.dto.OrderCreateDto;
import ua.com.taxi.model.dto.OrderListDto;
import ua.com.taxi.model.dto.SearchParameters;
import ua.com.taxi.service.OrderService;
import ua.com.taxi.util.FareCalculator;
import ua.com.taxi.util.MapService;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    private static final String FAILED_OPEN_CONNECTION_MESSAGE = "Database connection opening failed";
    private OrderDao orderDao;
    private UserDao userDao;
    private AddressDao addressDao;
    private CarDao carDao;

    public OrderServiceImpl(OrderDao orderDao, UserDao userDao, AddressDao addressDao, CarDao carDao) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.addressDao = addressDao;
        this.carDao = carDao;
    }

    @Override
    public int create(OrderCreateDto orderCreateDto) {
        Order order = null;
        try (Connection connection = ConnectionFactory.getConnection()) {
            try {
                connection.setAutoCommit(false);
                order = buildOrder(orderCreateDto, connection);
                orderDao.create(order, connection);
                connection.commit();
            } catch (Exception e) {
                LOGGER.error("Creating order failed");
                connection.rollback();
                throw new RollbackException("Creating order failed", e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            LOGGER.error(FAILED_OPEN_CONNECTION_MESSAGE);
            throw new DaoException(FAILED_OPEN_CONNECTION_MESSAGE, e);
        }
        return order.getId();
    }

    private Order buildOrder(OrderCreateDto orderCreateDto, Connection connection) throws SQLException {
        Optional<User> userOptional = userDao.findById(orderCreateDto.getUserId(), connection);
        Optional<Address> departureAddressOpt = addressDao.findById(orderCreateDto.getDepartureId(), connection);
        Optional<Address> arrivalAddressOpt = addressDao.findById(orderCreateDto.getArrivalId(), connection);

        User user = userOptional.orElseThrow(EntityNotFoundException::new);
        Address addressDeparture = departureAddressOpt.orElseThrow(EntityNotFoundException::new);
        Address addressArrival = arrivalAddressOpt.orElseThrow(EntityNotFoundException::new);

        Order order = new Order();
        order.setUser(user);
        order.setDiscount(user.getDiscount());
        order.setDepartureAddress(addressDeparture);
        order.setArrivalAddress(addressArrival);
        order.setPassengersCount(orderCreateDto.getPassengerCount());
        order.setCategory(orderCreateDto.getCategory());

        order.setFare(calculateFare(order));

        order.setFinalFare(calculateFinalFare(order));
        order.setCreationDate(LocalDateTime.now());
        order.setStatus(OrderStatus.NEW);

        return order;
    }

    private long calculateFare(Order order) {
        long distance = MapService.calculateDistance(order.getDepartureAddress().getPoint(), order.getArrivalAddress().getPoint());
        return FareCalculator.calculate(order.getCategory(), distance);
    }

    private long calculateFinalFare(Order order) {
        long discountCoins = order.getFare() * order.getDiscount() / 100;
        return order.getFare() - discountCoins;
    }

    @Override
    public List<OrderListDto> findAllListDto() {
        List<OrderListDto> orders = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection()) {
            orders = orderDao.findAllListDto(connection);
        } catch (SQLException e) {
            LOGGER.error("Finding orders failed");
            throw new DaoException("Finding orders failed", e);
        }
        return orders;
    }

    @Override
    public int findCount(SearchParameters searchParameters) {
        LOGGER.debug("findCount() [{}] ", searchParameters);
        try (Connection connection = ConnectionFactory.getConnection()) {
            return orderDao.findCount(searchParameters, connection);
        } catch (SQLException e) {
            LOGGER.error("Finding filtered orders count failed");
            throw new DaoException("Finding filtered orders count failed", e);
        }
    }

    @Override
    public List<OrderListDto> findAllListDto(SearchParameters searchParameters) {
        LOGGER.debug("findAllListDto() [{}] ", searchParameters);
        List<OrderListDto> orders = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection()) {
            orders = orderDao.findAllListDto(searchParameters, connection);
        } catch (SQLException e) {
            LOGGER.error("Finding filtered orders failed");
            throw new DaoException("Finding filtered orders failed", e);
        }
        return orders;
    }

    @Override
    public OrderConfirmDto prepareConfirm(Integer id) {
        OrderConfirmDto order = null;
        try (Connection connection = ConnectionFactory.getConnection()) {
            try {
                connection.setAutoCommit(false);

                Optional<OrderConfirmDto> orderOptional = orderDao.findOrderConfirmDto(connection, id);
                order = orderOptional.orElseThrow(EntityNotFoundException::new);

                Optional<Car> foundCar = carDao.find(order.getCategory(), order.getPassengersCount(), CarStatus.FREE, connection);
                order.setSuggestedCar(foundCar.orElse(null));
                connection.commit();
            } catch (Exception e) {
                String message = String.format("Preparing order confirm '%s' failed", id);
                LOGGER.error(message);
                connection.rollback();
                throw new RollbackException(message, e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            LOGGER.error(FAILED_OPEN_CONNECTION_MESSAGE);
            throw new DaoException(FAILED_OPEN_CONNECTION_MESSAGE, e);
        }
        return order;
    }

    @Override
    public void confirmOrder(int orderId, int carId) {

        try (Connection connection = ConnectionFactory.getConnection()) {
            try {
                connection.setAutoCommit(false);
                boolean carIsFree = carDao.isCarInStatus(carId, CarStatus.FREE, connection);
                if (!carIsFree) {
                    LOGGER.error("The car was not found");
                    throw new EntityNotFoundException();
                }
                int driverId = carDao.findDriverIdByCar(carId, connection);
                orderDao.startOrder(orderId, carId, driverId, OrderStatus.ON_ROAD, connection);
                carDao.changeCarStatus(carId, CarStatus.ON_ORDER, connection);

                connection.commit();
            } catch (Exception e) {
                String message = String.format("Order confirmation '%s' failed", orderId);
                LOGGER.error(message);
                connection.rollback();
                throw new RollbackException(message, e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            LOGGER.error(FAILED_OPEN_CONNECTION_MESSAGE);
            throw new DaoException(FAILED_OPEN_CONNECTION_MESSAGE, e);
        }
    }

    @Override
    public void complete(int orderId) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            try {
                connection.setAutoCommit(false);
                orderDao.changeOrderStatus(orderId, OrderStatus.COMPLETED, connection);
                Optional<Integer> orderCarOpt = orderDao.findCarIdByOrder(orderId, connection);
                if (orderCarOpt.isPresent()) {
                    carDao.changeCarStatus(orderCarOpt.get(), CarStatus.FREE, connection);
                }
                connection.commit();
            } catch (Exception e) {
                String message = String.format("Fail completing order '%s'", orderId);
                LOGGER.error(message);
                connection.rollback();
                throw new RollbackException(message, e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            LOGGER.error(FAILED_OPEN_CONNECTION_MESSAGE);
            throw new DaoException(FAILED_OPEN_CONNECTION_MESSAGE, e);
        }
    }


    @Override
    public void cancel(int orderId) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            try {
                connection.setAutoCommit(false);
                orderDao.changeOrderStatus(orderId, OrderStatus.CANCELLED, connection);
                Optional<Integer> orderCarOpt = orderDao.findCarIdByOrder(orderId, connection);
                if (orderCarOpt.isPresent()) {
                    carDao.changeCarStatus(orderCarOpt.get(), CarStatus.FREE, connection);
                }
                connection.commit();
            } catch (Exception e) {
                String message = String.format("Fail cancelling order '%s'", orderId);
                LOGGER.error(message);
                connection.rollback();
                throw new RollbackException(message, e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            LOGGER.error(FAILED_OPEN_CONNECTION_MESSAGE);
            throw new DaoException(FAILED_OPEN_CONNECTION_MESSAGE, e);
        }
    }

    @Override
    public Optional<Integer> findByUserAndStatus(int userId, OrderStatus orderStatus) {
        Optional<Integer> result = Optional.empty();
        try (Connection connection = ConnectionFactory.getConnection()) {
            result = orderDao.findByUserAndStatus(userId, orderStatus, connection);
        } catch (SQLException e) {
            LOGGER.error(FAILED_OPEN_CONNECTION_MESSAGE);
            throw new DaoException(FAILED_OPEN_CONNECTION_MESSAGE, e);
        }
        return result;
    }
}
