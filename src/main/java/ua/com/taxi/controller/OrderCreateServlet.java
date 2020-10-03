package ua.com.taxi.controller;

import ua.com.taxi.model.Category;
import ua.com.taxi.model.OrderStatus;
import ua.com.taxi.service.AddressService;
import ua.com.taxi.service.OrderService;
import ua.com.taxi.service.impl.AddressServiceImpl;
import ua.com.taxi.service.impl.OrderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static ua.com.taxi.filter.AuthenticationFilter.LOGGINED_USER_ID;

@WebServlet("/user/order-create")
public class OrderCreateServlet extends HttpServlet {

    private static final List<Integer> AVAILABLE_PASSENGER_COUNT_SETS = Arrays.asList(2, 4, 8);

    private OrderService orderService = new OrderServiceImpl();
    private AddressService addressService = new AddressServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int userId = (Integer) req.getSession().getAttribute(LOGGINED_USER_ID);
        Optional<Integer> newOrder = orderService.findByUserAndStatus(userId, OrderStatus.NEW);

        if (newOrder.isPresent()) {
            String url = String.format("/user/order-confirmation?id=%s", newOrder.get());
            resp.sendRedirect(url);
        } else {

            req.setAttribute("availableAddresses", addressService.findAll());
            req.setAttribute("availableCategory", Category.values());
            req.setAttribute("availablePassengerCountSets", AVAILABLE_PASSENGER_COUNT_SETS);
            req.getRequestDispatcher("/WEB-INF/order/order-creation.jsp").forward(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = (Integer) req.getSession().getAttribute(LOGGINED_USER_ID);
        String categoryStr = req.getParameter("selectedCategory");
        Category category = nonNull(categoryStr) && !categoryStr.isEmpty() ? Category.valueOf(categoryStr) : null;

        String selectedPassengersCountStr = req.getParameter("selectedPassengersCount");
        int selectedPassengersCount = nonNull(selectedPassengersCountStr) && !selectedPassengersCountStr.isEmpty() ? Integer.valueOf(selectedPassengersCountStr) : 0;

        String selectedDepartureAddressStr = req.getParameter("selectedDepartureAddress");
        int selectedDepartureAddress = nonNull(selectedDepartureAddressStr) && !selectedDepartureAddressStr.isEmpty() ? Integer.valueOf(selectedDepartureAddressStr) : 0;

        String selectedArrivalAddressStr = req.getParameter("selectedArrivalAddress");
        int selectedArrivalAddress = nonNull(selectedArrivalAddressStr) && !selectedArrivalAddressStr.isEmpty() ? Integer.valueOf(selectedArrivalAddressStr) : 0;

        int newOrderId = orderService.create(userId, category, selectedPassengersCount, selectedDepartureAddress, selectedArrivalAddress);

        String url = String.format("/user/order-confirmation?id=%s", newOrderId);
        resp.sendRedirect(url);

    }
}
