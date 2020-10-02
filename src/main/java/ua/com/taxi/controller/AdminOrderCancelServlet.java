package ua.com.taxi.controller;

import ua.com.taxi.service.OrderService;
import ua.com.taxi.service.impl.OrderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/order-cancel")
public class AdminOrderCancelServlet extends HttpServlet {

    private OrderService orderService = new OrderServiceImpl();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        orderService.cancel(Integer.valueOf(id));

        response.sendRedirect("/order-list");
    }
}