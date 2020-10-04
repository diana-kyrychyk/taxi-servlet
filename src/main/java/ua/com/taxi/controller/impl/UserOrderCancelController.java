package ua.com.taxi.controller.impl;

import ua.com.taxi.controller.Controller;
import ua.com.taxi.controller.MainServlet;
import ua.com.taxi.model.Role;
import ua.com.taxi.service.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static ua.com.taxi.filter.AuthenticationFilter.LOGGINED_USER_ROLES;

public class UserOrderCancelController implements Controller {

    private OrderService orderService;

    public UserOrderCancelController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String processRequest(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        orderService.cancel(Integer.valueOf(id));
        String redirectPath = "";

        if (isAdmin(request)) {
            redirectPath = "/admin/order-list";
        } else {
            redirectPath = "/";
        }
        return MainServlet.REDIRECT_PREFIX.concat(redirectPath);
    }

    private boolean isAdmin(HttpServletRequest request) {
        List<String> rolesAttr = (List<String>) request.getSession().getAttribute(LOGGINED_USER_ROLES);
        List<String> roles = rolesAttr != null ? rolesAttr : new ArrayList<>();
        return roles.contains(Role.ADMIN);
    }
}
