package servlet.filter;

import com.google.common.cache.Cache;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ValidateCsrfTokenFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Assume its HTTP
        HttpServletRequest httpReq = (HttpServletRequest) request;
        if (httpReq.getMethod().equals("POST")) {
            // Get the salt sent with the request
            String salt = httpReq.getParameter("X-TOKEN");
            // Validate that the salt is in the cache
            Cache<String, Boolean> csrfPreventionSaltCache = (Cache<String, Boolean>)
                    httpReq.getSession().getAttribute("X-TOKEN-CACHE");

            if (csrfPreventionSaltCache != null &&
                    salt != null &&
                    csrfPreventionSaltCache.getIfPresent(salt) != null) {

                // If the salt is in the cache, we move on
                chain.doFilter(request, response);
            } else {
                // Otherwise we throw an exception aborting the request flow
                throw new ServletException("Potential CSRF detected!! Inform DJ ASAP.");
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
