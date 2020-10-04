package ua.com.taxi.service;

import ua.com.taxi.model.Category;
import ua.com.taxi.model.OrderStatus;
import ua.com.taxi.model.dto.OrderConfirmDto;
import ua.com.taxi.model.dto.SearchParameters;
import ua.com.taxi.model.dto.OrderListDto;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    int create(int userId, Category category, int passengerCount, int departureId, int arrivalId);

    List<OrderListDto> findAllListDto();

    int findCount(SearchParameters searchParameters);

    List<OrderListDto> findAllListDto(SearchParameters searchParameters);

    OrderConfirmDto prepareConfirm(Integer id);

    void confirmOrder(int orderId, int carId);

    void complete(int orderId);

    void cancel(int orderId);

    Optional<Integer> findByUserAndStatus(int userId, OrderStatus orderStatus);
}
