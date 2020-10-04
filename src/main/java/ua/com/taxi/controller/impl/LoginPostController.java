package ua.com.taxi.controller.impl;

import ua.com.taxi.controller.Controller;
import ua.com.taxi.model.Role;
import ua.com.taxi.model.User;
import ua.com.taxi.service.UserService;
import ua.com.taxi.util.Hasher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ua.com.taxi.controller.MainServlet.REDIRECT_PREFIX;
import static ua.com.taxi.filter.AuthenticationFilter.LOGGINED_USER_ID;
import static ua.com.taxi.filter.AuthenticationFilter.LOGGINED_USER_NAME;
import static ua.com.taxi.filter.AuthenticationFilter.LOGGINED_USER_ROLES;

public class LoginPostController implements Controller {

    private UserService userService;

    public LoginPostController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String processRequest(HttpServletRequest request, HttpServletResponse response) {
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String hashedPassword = Hasher.hash(password);
        Optional<User> userOptional = userService.findByPhone(phone);

        String path = "";
        if (userOptional.isPresent() && userOptional.get().getPassword().equals(hashedPassword)) {
            List<Role> roles = userOptional.get().getRoles();

            List<String> roleNames = roles.stream()
                    .map(role -> role.getName())
                    .collect(Collectors.toList());

            request.getSession().setAttribute(LOGGINED_USER_NAME, userOptional.get().getName());
            request.getSession().setAttribute(LOGGINED_USER_ROLES, roleNames);
            request.getSession().setAttribute(LOGGINED_USER_ID, userOptional.get().getId());
            path = "/";
        } else {
            path = "/guest/user-login";
        }
        return REDIRECT_PREFIX.concat(path);
    }
}
