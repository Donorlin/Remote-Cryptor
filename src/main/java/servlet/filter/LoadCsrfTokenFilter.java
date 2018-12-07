package servlet.filter;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

public class LoadCsrfTokenFilter implements Filter {

    private SecureRandom secureRandom = new SecureRandom();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("LoadCsrfTokenFilter");

        HttpServletRequest httpReq = (HttpServletRequest) request;

        Cache<String, Boolean> csrfPreventionSaltCache = (Cache<String, Boolean>)
                httpReq.getSession().getAttribute("X-TOKEN-CACHE");

        if (csrfPreventionSaltCache == null){
            csrfPreventionSaltCache = CacheBuilder.newBuilder()
                    .maximumSize(5000)
                    .expireAfterWrite(20, TimeUnit.MINUTES)
                    .build();

            httpReq.getSession().setAttribute("X-TOKEN-CACHE", csrfPreventionSaltCache);
        }

        // Generate the salt and store it in the users cache
        String salt = generateToken();
        csrfPreventionSaltCache.put(salt, Boolean.TRUE);
        // Add the salt to the current request so it can be used
        // by the page rendered in this request
        httpReq.setAttribute("X-TOKEN", salt);
        chain.doFilter(request, response);
    }

    private String generateToken() {
        byte[] buffer = new byte[50];
        this.secureRandom.nextBytes(buffer);
        return DatatypeConverter.printHexBinary(buffer);
    }
}