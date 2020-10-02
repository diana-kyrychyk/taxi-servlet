package ua.com.taxi.controller;

import ua.com.taxi.model.dto.UserRegistrationDto;
import ua.com.taxi.service.UserService;
import ua.com.taxi.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/user-registration")
public class UserRegistrationServlet extends HttpServlet {

    private UserService userService = new UserServiceImpl();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/user/user-registration.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        String password = req.getParameter("password");

        UserRegistrationDto user = new UserRegistrationDto(name, phone, password);
        userService.registrate(user);

        resp.sendRedirect("/user-login");
    }
}
