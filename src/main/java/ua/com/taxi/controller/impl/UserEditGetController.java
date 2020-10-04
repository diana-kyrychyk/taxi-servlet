package ua.com.taxi.controller.impl;

import ua.com.taxi.controller.Controller;
import ua.com.taxi.exception.EntityNotFoundException;
import ua.com.taxi.model.Role;
import ua.com.taxi.model.User;
import ua.com.taxi.service.RoleService;
import ua.com.taxi.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

public class UserEditGetController implements Controller {

    private UserService userService;
    private RoleService roleService;

    public UserEditGetController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public String processRequest(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");

        Optional<User> userOptional = userService.findById(Integer.valueOf(id));
        List<Role> selectedRoles = roleService.findByUser(Integer.valueOf(id));
        List<Role> availableRoles = roleService.findAll();

        request.setAttribute("availableRoles", availableRoles);
        request.setAttribute("selectedRoles", selectedRoles);
        request.setAttribute("userId", userOptional.orElseThrow(EntityNotFoundException::new).getId());
        request.setAttribute("userName", userOptional.orElseThrow(EntityNotFoundException::new).getName());
        return "/WEB-INF/user/user-edit.jsp";
    }
}
