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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AuthenticationFilter extends HttpFilter {

    public static final String LOGGINED_USER_ROLES = "logginedUserRoles";
    public static final String LOGGINED_USER_NAME = "logginedUserName";

    static final List<String> adminPages = Arrays.asList(
            "/user-list",
            "/user-edit");

    static final List<String> userPages = Arrays.asList("/index.jsp");

    static final List<String> guestPages = Arrays.asList(
            "/index.jsp",
            "/",
            "/user-login",
            "/user-registration",
            "/user-logout"
    );

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);


    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        LOGGER.debug("Request to '{}' path", req.getServletPath());

        if (!isExistingPath(req.getServletPath()) && !req.getServletPath().startsWith("/static")) {
            LOGGER.warn("Request to unexisted path '{}'", req.getServletPath());
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else if (guestPages.contains(req.getServletPath()) || req.getServletPath().startsWith("/static")) {
            chain.doFilter(req, res);
        } else {
            filterProtectedRequests(req, res, chain);
        }
    }

    private void filterProtectedRequests(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String user = extractUser(request);
        List<String> roles = extractRoles(request);
        if (user == null) {
            LOGGER.warn("Not logginned request to '{}' path", request.getServletPath());
            response.sendRedirect("/user-login");
        } else if (userPages.contains(request.getServletPath()) && !roles.contains(Role.USER)) {
            LOGGER.warn("Access denied for user '{}' to USER path '{}'", user, request.getServletPath());
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } else if (adminPages.contains(request.getServletPath()) && !roles.contains(Role.ADMIN)) {
            LOGGER.warn("Access denied for user '{}' to ADMIN path '{}'", user, request.getServletPath());
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean isExistingPath(String path) {
        return guestPages.contains(path) || userPages.contains(path) || adminPages.contains(path);
    }

    private List<String> extractRoles(HttpServletRequest request) {
        List<String> roles = (List<String>) request.getSession().getAttribute(LOGGINED_USER_ROLES);
        return roles != null ? roles : new ArrayList<>();
    }

    private String extractUser(HttpServletRequest request) {
        String user = (String) request.getSession().getAttribute(LOGGINED_USER_NAME);
        return user;
    }
}
