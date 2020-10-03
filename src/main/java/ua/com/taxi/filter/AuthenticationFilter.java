package ua.com.taxi.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.com.taxi.model.Role;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class AuthenticationFilter extends HttpFilter {

    public static final String LOGGINED_USER_ROLES = "logginedUserRoles";
    public static final String LOGGINED_USER_NAME = "logginedUserName";
    public static final String LOGGINED_USER_ID = "logginedUserId";

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        LOGGER.debug("Request to '{}' path", req.getServletPath());

        if (req.getServletPath().startsWith("/admin")) {
            filterAdminRequests(req, res, chain);
        } else if (req.getServletPath().startsWith("/user")) {
            filterUserRequests(req, res, chain);
        } else if (req.getServletPath().startsWith("/guest")) {
            filterGuestRequests(req, res, chain);
        } else {
            chain.doFilter(req, res);
        }
    }

    private void filterUserRequests(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!isAuthorised(request)) {
            LOGGER.warn("Not logginned request to '{}' USER path", request.getServletPath());
            response.sendRedirect("/guest/user-login");
        } else if (!inRole(request, Role.USER)) {
            LOGGER.warn("Access denied to USER path '{}'", request.getServletPath());
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } else {
            chain.doFilter(request, response);
        }
    }

    private void filterAdminRequests(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!isAuthorised(request)) {
            LOGGER.warn("Not logginned request to '{}' ADMIN path", request.getServletPath());
            response.sendRedirect("/guest/user-login");
        } else if (!inRole(request, Role.ADMIN)) {
            LOGGER.warn("Access denied to ADMIN path '{}'", request.getServletPath());
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } else {
            chain.doFilter(request, response);
        }
    }

    private void filterGuestRequests(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (isAuthorised(request)) {
            LOGGER.warn("Access denied for authorised user to 'guest' path '{}'", request.getServletPath());
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean isAuthorised(HttpServletRequest req) {
        Object logginedAttribute = req.getSession().getAttribute(LOGGINED_USER_ID);
        return logginedAttribute != null;
    }

    private boolean inRole(HttpServletRequest req, String role) {
        List<String> roles = (List<String>) req.getSession().getAttribute(LOGGINED_USER_ROLES);
        return roles != null && roles.contains(role);
    }
}
