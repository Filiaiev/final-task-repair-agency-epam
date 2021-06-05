package com.filiaiev.agency.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {

    private static Logger logger = Logger.getLogger(EncodingFilter.class);

    private static String encoding = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.debug("Encoding filter init started");

        encoding = filterConfig.getInitParameter("encoding");

        logger.trace("Filter config encoding: " + encoding);
        logger.debug("Encoding filter init successfully ended");
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
        logger.debug("Encoding filter destroying");
    }
}
