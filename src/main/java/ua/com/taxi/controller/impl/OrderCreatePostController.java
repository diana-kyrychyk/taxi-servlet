package ua.com.taxi.controller.impl;

import ua.com.taxi.controller.Controller;
import ua.com.taxi.controller.MainServlet;
import ua.com.taxi.model.Category;
import ua.com.taxi.model.dto.OrderCreateDto;
import ua.com.taxi.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.util.Objects.nonNull;
import static ua.com.taxi.filter.AuthenticationFilter.LOGGINED_USER_ID;

public class OrderCreatePostController implements Controller {

    private OrderService orderService;

    public OrderCreatePostController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String processRequest(HttpServletRequest request, HttpServletResponse response) {
        OrderCreateDto order = extractParameters(request);
        int newOrderId = orderService.create(order);

        String url = String.format("/user/order-confirmation?id=%s", newOrderId);
        return MainServlet.REDIRECT_PREFIX.concat(url);
    }


    private OrderCreateDto extractParameters(HttpServletRequest request){
        OrderCreateDto order = new OrderCreateDto();
        int userId = (Integer) request.getSession().getAttribute(LOGGINED_USER_ID);
        order.setUserId(userId);

        String categoryStr = request.getParameter("selectedCategory");
        Category category = nonNull(categoryStr) && !categoryStr.isEmpty() ? Category.valueOf(categoryStr) : null;
        order.setCategory(category);

        String selectedPassengersCountStr = request.getParameter("selectedPassengersCount");
        int selectedPassengersCount = nonNull(selectedPassengersCountStr) && !selectedPassengersCountStr.isEmpty() ? Integer.valueOf(selectedPassengersCountStr) : 0;
        order.setPassengerCount(selectedPassengersCount);

        String selectedDepartureAddressStr = request.getParameter("selectedDepartureAddress");
        int selectedDepartureAddress = nonNull(selectedDepartureAddressStr) && !selectedDepartureAddressStr.isEmpty() ? Integer.valueOf(selectedDepartureAddressStr) : 0;
        order.setDepartureId(selectedDepartureAddress);

        String selectedArrivalAddressStr = request.getParameter("selectedArrivalAddress");
        int selectedArrivalAddress = nonNull(selectedArrivalAddressStr) && !selectedArrivalAddressStr.isEmpty() ? Integer.valueOf(selectedArrivalAddressStr) : 0;
        order.setArrivalId(selectedArrivalAddress);

        return order;
    }
}
