package com.filiaiev.agency.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {

    private static String encoding = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Encoding com.filiaiev.agency.filter init start");
        encoding = filterConfig.getInitParameter("encoding");
        System.out.println("encoding is: " + encoding);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if(req.getCharacterEncoding() == null){
            req.setCharacterEncoding(encoding);
        }
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
        System.out.println("Encoding com.filiaiev.agency.filter destroy start");
    }
}
