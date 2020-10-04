package ua.com.taxi.controller.impl;

import ua.com.taxi.controller.Controller;
import ua.com.taxi.model.dto.OrderConfirmDto;
import ua.com.taxi.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OrderConfirmationGetController implements Controller {

    private OrderService orderService;

    public OrderConfirmationGetController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String processRequest(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        OrderConfirmDto order = orderService.prepareConfirm(Integer.valueOf(id));

        request.setAttribute("order", order);
        return "/WEB-INF/order/order-confirm.jsp";
    }
}
