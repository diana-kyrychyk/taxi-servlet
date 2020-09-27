package ua.com.taxi.controller;

import ua.com.taxi.model.Role;
import ua.com.taxi.model.User;
import ua.com.taxi.service.UserService;
import ua.com.taxi.service.impl.UserServiceImpl;
import ua.com.taxi.util.Hasher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ua.com.taxi.filter.AuthenticationFilter.LOGGINED_USER_NAME;
import static ua.com.taxi.filter.AuthenticationFilter.LOGGINED_USER_ROLES;

@WebServlet("/user-login")
public class LoginServlet extends HttpServlet {

    private UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/user/user-login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String phone = req.getParameter("phone");
        String password = req.getParameter("password");
        String hashedPassword = Hasher.hash(password);
        Optional<User> userOptional = userService.findByPhone(phone);

        if (userOptional.isPresent() && userOptional.get().getPassword().equals(hashedPassword)) {
            List<Role> roles = userOptional.get().getRoles();

            List<String> roleNames = roles.stream()
                    .map(role -> role.getName())
                    .collect(Collectors.toList());

            req.getSession().setAttribute(LOGGINED_USER_NAME, userOptional.get().getName());
            req.getSession().setAttribute(LOGGINED_USER_ROLES, roleNames);
            resp.sendRedirect("/index.jsp");
        } else {
            resp.sendRedirect("/user-login");
        }
    }

}
