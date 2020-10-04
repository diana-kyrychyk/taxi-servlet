package ua.com.taxi.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.taxi.dao.AddressDao;
import ua.com.taxi.dao.CarDao;
import ua.com.taxi.dao.ConnectionFactory;
import ua.com.taxi.dao.OrderDao;
import ua.com.taxi.dao.UserDao;
import ua.com.taxi.dao.impl.AddressDaoImpl;
import ua.com.taxi.dao.impl.CarDaoImpl;
import ua.com.taxi.dao.impl.OrderDaoImpl;
import ua.com.taxi.dao.impl.UserDaoImpl;
import ua.com.taxi.exception.DaoException;
import ua.com.taxi.exception.EntityNotFoundException;
import ua.com.taxi.exception.RollbackException;
import ua.com.taxi.model.Address;
import ua.com.taxi.model.Car;
import ua.com.taxi.model.CarStatus;
import ua.com.taxi.model.Category;
import ua.com.taxi.model.Order;
import ua.com.taxi.model.OrderStatus;
import ua.com.taxi.model.User;
import ua.com.taxi.model.dto.OrderConfirmDto;
import ua.com.taxi.model.dto.SearchParameters;
import ua.com.taxi.model.dto.OrderListDto;
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

    private static final String FAILED_OPEN_CONNECTION_MESSAGE = "Database connection opening failed";
    private OrderDao orderDao = new OrderDaoImpl();
    private UserDao userDao = new UserDaoImpl();
    private AddressDao addressDao = new AddressDaoImpl();
    private CarDao carDao = new CarDaoImpl();

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public int create(int userId, Category category, int passengerCount, int departureId, int arrivalId) {
        Order order = null;
        try (Connection connection = ConnectionFactory.getConnection()) {
            try {
                connection.setAutoCommit(false);
                Optional<User> userOptional = userDao.findById(userId, connection);
                Optional<Address> departureAddressOpt = addressDao.findById(departureId, connection);
                Optional<Address> arrivalAddressOpt = addressDao.findById(arrivalId, connection);

                User user = userOptional.orElseThrow(EntityNotFoundException::new);
                Address addressDeparture = departureAddressOpt.orElseThrow(EntityNotFoundException::new);
                Address addressArrival = arrivalAddressOpt.orElseThrow(EntityNotFoundException::new);

                order = new Order();
                order.setDepartureAddress(addressDeparture);
                order.setArrivalAddress(addressArrival);
                order.setPassengersCount(passengerCount);
                order.setCategory(category);


                long distance = MapService.calculateDistance(addressDeparture.getPoint(), addressArrival.getPoint());
                long fare = FareCalculator.calculate(category, distance);

                order.setFare(fare);

                long discountCoins = fare * user.getDiscount() / 100;
                long finalFare = fare - discountCoins;
                order.setDiscount(user.getDiscount());
                order.setFinalFare(finalFare);
                order.setCreationDate(LocalDateTime.now());
                order.setUser(user);
                order.setStatus(OrderStatus.NEW);

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
                if (!orderOptional.isPresent()) {
                    String message = String.format("New order '%s' was not found", id);
                    LOGGER.error(message);
                    throw new EntityNotFoundException(message);
                }
                order = orderOptional.get();

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
                if (carIsFree) {
                    int driverId = carDao.findDriverIdByCar(carId, connection);
                    orderDao.startOrder(orderId, carId, driverId, OrderStatus.ON_ROAD, connection);
                    carDao.changeCarStatus(carId, CarStatus.ON_ORDER, connection);
                } else {
                    LOGGER.error("The car was not found");
                    throw new EntityNotFoundException();
                }
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
