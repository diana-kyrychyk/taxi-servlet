package ua.com.taxi.controller.impl;

import ua.com.taxi.controller.Controller;
import ua.com.taxi.controller.MainServlet;
import ua.com.taxi.controller.validation.UserValidator;
import ua.com.taxi.filter.LocaleFilter;
import ua.com.taxi.model.dto.UserRegistrationDto;
import ua.com.taxi.service.UserService;
import ua.com.taxi.util.ValidationMessageLocalizator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

public class UserRegistrationPostController implements Controller {

    private UserService userService;

    private UserValidator validator;
    private ValidationMessageLocalizator localizator = new ValidationMessageLocalizator();

    public UserRegistrationPostController(UserService userService, UserValidator validator) {
        this.userService = userService;
        this.validator = validator;
    }

    @Override
    public String processRequest(HttpServletRequest request, HttpServletResponse response) {
        UserRegistrationDto user = extractUser(request);
        Map<String, String> fieldsErrors = validator.validate(user);
        String page = "";
        if (!fieldsErrors.isEmpty()) {
            localizator.localize(fieldsErrors, extractLocale(request));
            fillFailedAttributes(request, user, fieldsErrors);
            page = "/WEB-INF/user/user-registration.jsp";
        } else {
            userService.registrate(user);
            page = MainServlet.REDIRECT_PREFIX.concat("/guest/user-login");
        }
        return page;
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

    private void fillFailedAttributes(HttpServletRequest req, UserRegistrationDto user, Map<String, String> fieldsErrors) {
        req.setAttribute("fieldsErrors", fieldsErrors);
        req.setAttribute("name", user.getName());
        req.setAttribute("phone", user.getPhone());
    }
}
