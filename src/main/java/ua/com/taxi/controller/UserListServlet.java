package ua.com.taxi.controller;

import ua.com.taxi.model.User;
import ua.com.taxi.service.UserService;
import ua.com.taxi.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/user-list")
public class UserListServlet extends HttpServlet {

    private UserService userService = new UserServiceImpl();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> userList = userService.findAllUsers();

        request.setAttribute("users", userList);

        request.getRequestDispatcher("/WEB-INF/user/user-list.jsp").forward(request, response);
    }
}
