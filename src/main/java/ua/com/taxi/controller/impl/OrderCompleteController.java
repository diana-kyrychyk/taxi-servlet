package ua.com.taxi.controller.impl;

import ua.com.taxi.controller.Controller;
import ua.com.taxi.controller.MainServlet;
import ua.com.taxi.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OrderCompleteController implements Controller {

    private OrderService orderService;

    public OrderCompleteController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String processRequest(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        orderService.complete(Integer.valueOf(id));
        return MainServlet.REDIRECT_PREFIX.concat("/admin/order-list");
    }
}


