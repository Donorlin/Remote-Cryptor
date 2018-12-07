package servlet.filter;

import com.google.common.cache.Cache;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import servlet.common.ServletUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Scanner;

public class ValidateCsrfTokenFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;
        if (httpReq.getMethod().equals("POST")) {
            String salt;
            if (ServletFileUpload.isMultipartContent(httpReq)) {
                System.out.println("SOM TU V MULTI");
                salt = (String)httpReq.getAttribute("X-TOKEN");
            } else {
                System.out.println("SOM TU V KLASIC");
                salt = httpReq.getParameter("X-TOKEN");
            }
            Cache<String, Boolean> csrfPreventionSaltCache = (Cache<String, Boolean>)
                    httpReq.getSession().getAttribute("X-TOKEN-CACHE");

            if (csrfPreventionSaltCache != null &&
                    salt != null &&
                    csrfPreventionSaltCache.getIfPresent(salt) != null) {
                chain.doFilter(request, response);
            } else {
                httpResp.sendRedirect(httpReq.getContextPath() + "/static/html/csrferror.html");
            }
        } else {
            chain.doFilter(httpReq, httpResp);
        }
    }
}
