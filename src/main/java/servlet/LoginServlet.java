package servlet;

import database.entity.User;
import database.user.UserService;
import servlet.common.JWTUtils;
import servlet.common.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("jwt") != null
                && request.getSession().getAttribute("username") != null
                && request.getCookies() != null
                && request.getCookies().length > 0
        ) {
           if(ServletUtils.isAuthenticated(request)) {
               request.setAttribute("errorMessage", "You are already logged in. Please log out if you want to change a user.");
               request.getRequestDispatcher("/WEB-INF/jsps/views/share.jsp").forward(request, response);
               return;
           } else {
               request.setAttribute("errorMessage", "Wrong or no login token.");
               request.getRequestDispatcher("/WEB-INF/jsps/views/login.jsp").forward(request, response);
               return;
           }
        }
        request.getRequestDispatcher("/WEB-INF/jsps/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        UserService userService = new UserService();
        boolean retVal = userService.checkUserHash(username, password);
        if (!retVal) {
            req.setAttribute("errorMessage", "Bad user name or password");
            req.getRequestDispatcher("WEB-INF/jsps/views/login.jsp").forward(req, resp);
        } else {
            String token = JWTUtils.generateJWT(username);
            req.getSession().setAttribute("jwt", token);
            req.getSession().setAttribute("username", username);
            resp.addCookie(new Cookie("token", token));
            resp.sendRedirect(req.getContextPath() + "/share");
        }
    }
}
