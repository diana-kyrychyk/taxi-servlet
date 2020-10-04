package ua.com.taxi.controller.impl;

import ua.com.taxi.controller.Controller;
import ua.com.taxi.controller.MainServlet;
import ua.com.taxi.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;

public class UserEditPostController implements Controller {

    private UserService userService;

    public UserEditPostController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String processRequest(HttpServletRequest request, HttpServletResponse response) {
        String idStr = request.getParameter("id");
        Integer id = nonNull(idStr) && !idStr.isEmpty() ? Integer.valueOf(idStr) : null;
        String name = request.getParameter("name");
        String[] selectedRoleIdsStr = request.getParameterValues("selectedRoles");
        List<Integer> selectedRoles = new ArrayList<>();
        if (selectedRoleIdsStr != null) {
            selectedRoles = Stream.of(selectedRoleIdsStr).map(Integer::valueOf).collect(Collectors.toList());
        }
        userService.update(id, name, selectedRoles);
        request.setAttribute("message", "Role was added succesfully");
        return MainServlet.REDIRECT_PREFIX.concat("/admin/user-list");
    }
}
