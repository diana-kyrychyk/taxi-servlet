package ua.com.taxi.controller.impl;

import ua.com.taxi.controller.Controller;
import ua.com.taxi.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ua.com.taxi.controller.MainServlet.REDIRECT_PREFIX;

public class AdminOrderCancelController implements Controller {

    private OrderService orderService;

    public AdminOrderCancelController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String processRequest(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        orderService.cancel(Integer.valueOf(id));
        return REDIRECT_PREFIX.concat("/admin/order-list");
    }
}
