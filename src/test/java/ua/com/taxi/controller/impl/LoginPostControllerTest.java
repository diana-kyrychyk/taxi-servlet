package ua.com.taxi.controller.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ua.com.taxi.model.Role;
import ua.com.taxi.model.User;
import ua.com.taxi.service.UserService;
import ua.com.taxi.util.Hasher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.taxi.filter.AuthenticationFilter.LOGGINED_USER_ID;
import static ua.com.taxi.filter.AuthenticationFilter.LOGGINED_USER_NAME;
import static ua.com.taxi.filter.AuthenticationFilter.LOGGINED_USER_ROLES;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Hasher.class)
public class LoginPostControllerTest {

    String parameterNamePhone = "phone";
    String parameterNamePassword = "password";
    int id = 1;
    String name = "testName";
    String phone = "+380987770000";
    String password = "passwordTest";

    UserService userServiceMock = mock(UserService.class);
    HttpServletRequest requestMock = mock(HttpServletRequest.class);
    HttpSession sessionMock = mock(HttpSession.class);
    HttpServletResponse responseMock = mock(HttpServletResponse.class);
    LoginPostController loginPostController = new LoginPostController(userServiceMock);

    @Test
    public void shouldReturnLoginPage(){
        when(requestMock.getParameter(parameterNamePhone)).thenReturn(phone);
        when(requestMock.getParameter(parameterNamePassword)).thenReturn(password);
        PowerMockito.mockStatic(Hasher.class);
        given(Hasher.hash(password)).willReturn(password);
        when(userServiceMock.findByPhone(phone)).thenReturn(Optional.empty());

        String actualPath = loginPostController.processRequest(requestMock, responseMock);

        assertEquals("redirect:/guest/user-login", actualPath);
    }

    @Test
    public void shouldReturnIndexPageAndSetSessionAttributes(){
        when(requestMock.getParameter(parameterNamePhone)).thenReturn(phone);
        when(requestMock.getParameter(parameterNamePassword)).thenReturn(password);
        when(requestMock.getSession()).thenReturn(sessionMock);
        PowerMockito.mockStatic(Hasher.class);
        given(Hasher.hash(password)).willReturn(password);
        when(userServiceMock.findByPhone(phone)).thenReturn(buildUser());

        String actualPath = loginPostController.processRequest(requestMock, responseMock);

        verify(sessionMock, times(1)).setAttribute(LOGGINED_USER_ID, id);
        verify(sessionMock, times(1)).setAttribute(LOGGINED_USER_NAME, name);
        verify(sessionMock, times(1)).setAttribute(LOGGINED_USER_ROLES, Arrays.asList(Role.ADMIN));
        assertEquals("redirect:/", actualPath);
    }


    private Optional<User> buildUser(){
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setPhone(phone);
        user.setPassword(password);

        Role role = new Role();
        role.setName(Role.ADMIN);
        user.setRoles(Arrays.asList(role));
        return Optional.of(user);
    }
}
