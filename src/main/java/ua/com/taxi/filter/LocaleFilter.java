package ua.com.taxi.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LocaleFilter extends HttpFilter {

    public static final String LANGUAGE_PARAM = "lang";

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req.getParameter(LANGUAGE_PARAM) != null) {
            req.getSession().setAttribute(LANGUAGE_PARAM, req.getParameter(LANGUAGE_PARAM));
        }
        chain.doFilter(req,res);
    }
}
