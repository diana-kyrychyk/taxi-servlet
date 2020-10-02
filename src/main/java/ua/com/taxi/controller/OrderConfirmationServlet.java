package ua.com.taxi.controller;

import ua.com.taxi.model.dto.OrderConfirmDto;
import ua.com.taxi.service.OrderService;
import ua.com.taxi.service.impl.OrderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/order-confirmation")
public class OrderConfirmationServlet extends HttpServlet {

    private OrderService orderService = new OrderServiceImpl();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        OrderConfirmDto order = orderService.prepareConfirm(Integer.valueOf(id));

        request.setAttribute("order", order);

        request.getRequestDispatcher("/WEB-INF/order/order-confirm.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderId = req.getParameter("orderId");
        String carId = req.getParameter("carId");
        try {
            orderService.confirmOrder(Integer.valueOf(orderId), Integer.valueOf(carId));
            resp.sendRedirect("/");
        } catch (Exception e) {
            String url = String.format("/order-confirmation?id=%s", orderId);
            resp.sendRedirect(url);
        }
    }
}
