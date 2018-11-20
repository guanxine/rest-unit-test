package com.k2data.demo.rest.unit.web.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by guanxine on 18-11-19.
 */
public class UnitFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        chain.doFilter(request, response);
    }

    public void destroy() {

    }
}
