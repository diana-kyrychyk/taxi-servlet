package ua.com.taxi.controller;

import ua.com.taxi.model.Role;
import ua.com.taxi.service.OrderService;
import ua.com.taxi.service.impl.OrderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ua.com.taxi.filter.AuthenticationFilter.LOGGINED_USER_ROLES;

@WebServlet("/user/order-cancel")
public class UserOrderCancelServlet extends HttpServlet {

    private OrderService orderService = new OrderServiceImpl();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        orderService.cancel(Integer.valueOf(id));

        if (isAdmin(request)) {
            response.sendRedirect("/order-list");
        } else {
            response.sendRedirect("/");
        }
    }

    private boolean isAdmin(HttpServletRequest request) {
        List<String> rolesAttr = (List<String>) request.getSession().getAttribute(LOGGINED_USER_ROLES);
        List<String> roles = rolesAttr != null ? rolesAttr : new ArrayList<>();
        return roles.contains(Role.ADMIN);
    }
}
