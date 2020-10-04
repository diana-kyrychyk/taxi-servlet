package ua.com.taxi.controller.impl;

import ua.com.taxi.controller.Controller;
import ua.com.taxi.model.User;
import ua.com.taxi.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class UserListController implements Controller {

    private UserService userService;

    public UserListController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String processRequest(HttpServletRequest request, HttpServletResponse response) {
        List<User> userList = userService.findAllUsers();

        request.setAttribute("users", userList);
        return "/WEB-INF/user/user-list.jsp";
    }
}
