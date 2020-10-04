package ua.com.taxi.controller.impl;

import ua.com.taxi.controller.Controller;
import ua.com.taxi.controller.MainServlet;
import ua.com.taxi.model.Category;
import ua.com.taxi.model.OrderStatus;
import ua.com.taxi.service.AddressService;
import ua.com.taxi.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static ua.com.taxi.filter.AuthenticationFilter.LOGGINED_USER_ID;

public class OrderCreateGetController implements Controller {

    private static final List<Integer> AVAILABLE_PASSENGER_COUNT_SETS = Arrays.asList(2, 4, 8);

    private OrderService orderService;
    private AddressService addressService;

    public OrderCreateGetController(OrderService orderService, AddressService addressService) {
        this.orderService = orderService;
        this.addressService = addressService;
    }

    @Override
    public String processRequest(HttpServletRequest request, HttpServletResponse response) {
        int userId = (Integer) request.getSession().getAttribute(LOGGINED_USER_ID);
        Optional<Integer> newOrder = orderService.findByUserAndStatus(userId, OrderStatus.NEW);
        String page = "";

        if (newOrder.isPresent()) {
            String url = String.format("/user/order-confirmation?id=%s", newOrder.get());
            page = MainServlet.REDIRECT_PREFIX.concat(url);
        } else {

            request.setAttribute("availableAddresses", addressService.findAll());
            request.setAttribute("availableCategory", Category.values());
            request.setAttribute("availablePassengerCountSets", AVAILABLE_PASSENGER_COUNT_SETS);
            page = "/WEB-INF/order/order-creation.jsp";
        }
        return page;
    }
}
