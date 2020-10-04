package ua.com.taxi.controller.validation;

import ua.com.taxi.model.dto.UserRegistrationDto;
import ua.com.taxi.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {

    private static final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";
    private static final String PHONE_PATTERN = "^[+][0-9]{12}";
    private UserService userService;

    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    public Map<String, String> validate(UserRegistrationDto user) {
        Map<String, String> fieldsErrors = new HashMap<>();
        validatePassword(user, fieldsErrors);
        validateName(user, fieldsErrors);
        validatePhone(user, fieldsErrors);
        return fieldsErrors;
    }

    private void validatePassword(UserRegistrationDto user, Map<String, String> fieldsErrors) {
        validatePasswordConfirm(user, fieldsErrors);
        validatePasswordPattern(user, fieldsErrors);
    }

    private void validatePasswordConfirm(UserRegistrationDto user, Map<String, String> fieldsErrors) {
        if (!user.getPassword().equals(user.getPasswordConfirm())) {
            fieldsErrors.put("password-confirm", "{taxi.validation.constraints.password.confirm.message}");
        }
    }

    private void validatePasswordPattern(UserRegistrationDto user, Map<String, String> fieldsErrors) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(user.getPassword());
        if (!matcher.matches()) {
            fieldsErrors.put("password", "{taxi.validation.constraints.password.pattern.message}");
        }
    }

    private void validateName(UserRegistrationDto user, Map<String, String> fieldsErrors) {
        if (user.getName() == null || user.getName().isEmpty() || user.getName().length() < 2) {
            fieldsErrors.put("name", "{taxi.validation.constraints.name.min.message}");
        }
    }

    private void validatePhone(UserRegistrationDto user, Map<String, String> fieldsErrors) {
        boolean isValidPattern = validatePhonePattern(user, fieldsErrors);
        if (isValidPattern && userService.findByPhone(user.getPhone()).isPresent()) {
            fieldsErrors.put("phone", "{taxi.validation.constraints.phone.exists.message}");
        }
    }

    private boolean validatePhonePattern(UserRegistrationDto user, Map<String, String> fieldsErrors) {
        boolean result = true;
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(user.getPhone());
        if (!matcher.matches()) {
            fieldsErrors.put("phone", "{taxi.validation.constraints.phone.pattern.message}");
            result = false;
        }
        return result;
    }
}
