package ua.com.taxi.controller;

import ua.com.taxi.controller.validation.UserValidator;
import ua.com.taxi.filter.LocaleFilter;
import ua.com.taxi.model.dto.UserRegistrationDto;
import ua.com.taxi.service.UserService;
import ua.com.taxi.service.impl.UserServiceImpl;
import ua.com.taxi.util.ValidationMessageLocalizator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

@WebServlet("/guest/user-registration")
public class UserRegistrationServlet extends HttpServlet {

    private UserService userService = new UserServiceImpl();

    private UserValidator validator = new UserValidator();
    private ValidationMessageLocalizator localizator = new ValidationMessageLocalizator();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/user/user-registration.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserRegistrationDto user = extractUser(req);
        Map<String, String> fieldsErrors = validator.validate(user);
        if (!fieldsErrors.isEmpty()) {
            localizator.localize(fieldsErrors, extractLocale(req));
            returnToRegistration(req, resp, user, fieldsErrors);
        } else {
            userService.registrate(user);
            resp.sendRedirect("/guest/user-login");
        }
    }

    private Locale extractLocale(HttpServletRequest req) {
        String lang = (String) req.getSession().getAttribute(LocaleFilter.LANGUAGE_PARAM);
        return Locale.forLanguageTag(lang);
    }

    private UserRegistrationDto extractUser(HttpServletRequest request) {
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String passwordConfirm = request.getParameter("password-confirm");

        return new UserRegistrationDto(name, phone, password, passwordConfirm);
    }

    private void returnToRegistration(HttpServletRequest req, HttpServletResponse resp, UserRegistrationDto user, Map<String, String> fieldsErrors) throws ServletException, IOException {
        req.setAttribute("fieldsErrors", fieldsErrors);
        req.setAttribute("name", user.getName());
        req.setAttribute("phone", user.getPhone());

        req.getRequestDispatcher("/WEB-INF/user/user-registration.jsp").forward(req, resp);
    }
}
