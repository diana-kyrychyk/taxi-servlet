package ua.com.taxi.controller;

import ua.com.taxi.model.dto.OrderListDto;
import ua.com.taxi.service.OrderService;
import ua.com.taxi.service.impl.OrderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/order-list")
public class OrderListServlet extends HttpServlet {

    private OrderService orderService = new OrderServiceImpl();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<OrderListDto> orderList = orderService.findAllListDto();

        request.setAttribute("orders", orderList);

        request.getRequestDispatcher("/WEB-INF/order/order-list.jsp").forward(request, response);
    }
}
