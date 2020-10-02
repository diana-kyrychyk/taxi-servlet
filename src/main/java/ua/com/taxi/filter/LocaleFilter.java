package ua.com.taxi.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LocaleFilter extends HttpFilter {

    public static final String LANGUAGE_PARAM = "lang";
    public static final String DEFAULT_LANG = "uk";

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req.getParameter(LANGUAGE_PARAM) != null) {
            req.getSession().setAttribute(LANGUAGE_PARAM, req.getParameter(LANGUAGE_PARAM));
        }

        if (req.getSession().getAttribute(LANGUAGE_PARAM) == null) {
            req.getSession().setAttribute(LANGUAGE_PARAM, DEFAULT_LANG);
        }

        chain.doFilter(req,res);
    }
}
