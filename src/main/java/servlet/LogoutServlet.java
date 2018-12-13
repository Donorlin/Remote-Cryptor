package servlet;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    // just for sure
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doLogout(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doLogout(req, resp);
    }

    private void doLogout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();
        Cookie toRemove = new Cookie("token", "");
        toRemove.setMaxAge(0);
        resp.addCookie(toRemove);
        req.setAttribute("errorMessage", "Log out successful.");

        // HACK HACK HACK
        Cache<String, Boolean> csrfPreventionSaltCache = (Cache<String, Boolean>)
                req.getSession().getAttribute("X-TOKEN-CACHE");

        if (csrfPreventionSaltCache == null){
            csrfPreventionSaltCache = CacheBuilder.newBuilder()
                    .maximumSize(5000)
                    .expireAfterWrite(20, TimeUnit.MINUTES)
                    .build();

            req.getSession().setAttribute("X-TOKEN-CACHE", csrfPreventionSaltCache);
        }

        // Generate the salt and store it in the users cache
        String salt = generateToken();
        csrfPreventionSaltCache.put(salt, Boolean.TRUE);
        // Add the salt to the current request so it can be used
        // by the page rendered in this request
        req.setAttribute("X-TOKEN", salt);
        // HACK HACK HACK

        req.getRequestDispatcher("/WEB-INF/jsps/views/login.jsp").forward(req, resp);
    }

    private String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] buffer = new byte[50];
        secureRandom.nextBytes(buffer);
        return DatatypeConverter.printHexBinary(buffer);
    }
}
