package ua.com.taxi.controller;

import ua.com.taxi.model.Role;
import ua.com.taxi.model.User;
import ua.com.taxi.service.RoleService;
import ua.com.taxi.service.UserService;
import ua.com.taxi.service.impl.RoleServiceImpl;
import ua.com.taxi.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;

@WebServlet("/admin/user-edit")
public class UserEditServlet extends HttpServlet {

    private UserService userService = new UserServiceImpl();
    private RoleService roleService = new RoleServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");

        Optional<User> userOptional = userService.findById(Integer.valueOf(id));
        List<Role> selectedRoles = roleService.findByUser(Integer.valueOf(id));

        List<Role> availableRoles = roleService.findAll();

        req.setAttribute("availableRoles", availableRoles);
        req.setAttribute("selectedRoles", selectedRoles);
        //TODO is present
        req.setAttribute("userId", userOptional.get().getId());
        req.setAttribute("userName", userOptional.get().getName());
        req.getRequestDispatcher("/WEB-INF/user/user-edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        Integer id = nonNull(idStr) && !idStr.isEmpty() ? Integer.valueOf(idStr) : null;
        String name = req.getParameter("name");
        String[] selectedRoleIdsStr = req.getParameterValues("selectedRoles");
        List<Integer> selectedRoles = new ArrayList<>();
        if (selectedRoleIdsStr != null) {
            selectedRoles = Stream.of(selectedRoleIdsStr).map(Integer::valueOf).collect(Collectors.toList());
        }

        userService.update(id, name, selectedRoles);

        //TODO show at page
        req.setAttribute("message", "Role was added succesfully");

        resp.sendRedirect("/admin/user-list");
    }
}
