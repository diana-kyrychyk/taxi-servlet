package ua.com.taxi.controller.impl;

import ua.com.taxi.controller.Controller;
import ua.com.taxi.controller.MainServlet;
import ua.com.taxi.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OrderConfirmationPostController implements Controller {

    private OrderService orderService;

    public OrderConfirmationPostController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String processRequest(HttpServletRequest request, HttpServletResponse response) {
        String orderId = request.getParameter("orderId");
        String carId = request.getParameter("carId");
        String pathToRedirect = "";
        try {
            orderService.confirmOrder(Integer.valueOf(orderId), Integer.valueOf(carId));
            pathToRedirect = "/";
        } catch (Exception e) {
            pathToRedirect = String.format("/user/order-confirmation?id=%s", orderId);
        }
        return MainServlet.REDIRECT_PREFIX.concat(pathToRedirect);
    }
}
