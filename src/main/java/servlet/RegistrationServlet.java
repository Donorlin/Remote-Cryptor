package servlet;

import database.user.UserService;
import servlet.common.PwdUtils;
import servlet.common.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/")
public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("jwt") != null
                && request.getSession().getAttribute("username") != null
                && request.getCookies() != null
                && request.getCookies().length > 0
        ) {
            if(ServletUtils.isAuthenticated(request)) {
                request.setAttribute("errorMessage", "You are already logged in. Please log out if you want to register a new user.");
                request.getRequestDispatcher("/WEB-INF/jsps/share.jsp").forward(request, response);
                return;
            } else {
                request.setAttribute("errorMessage", "Wrong or no login token.");
                request.getRequestDispatcher("/WEB-INF/jsps/register.jsp").forward(request, response);
                return;
            }
        }
        request.getRequestDispatcher("/WEB-INF/jsps/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (!PwdUtils.isPwdStrong(password)) {
            req.setAttribute("errorMessage", "Weak password.");
            req.getRequestDispatcher("/WEB-INF/jsps/register.jsp").forward(req, resp);
            return;
        }

        if (PwdUtils.isPwdInDictionary(password)) {
            req.setAttribute("errorMessage", "Given password is in our pwd dictionary, choose different password");
            req.getRequestDispatcher("/WEB-INF/jsps/register.jsp").forward(req, resp);
            return;
        }

        UserService userService = new UserService();
        boolean retVal = userService.storeUser(username, password);
        if (!retVal) {
            req.setAttribute("errorMessage", "User with given username already exists.");
            req.getRequestDispatcher("/WEB-INF/jsps/register.jsp").forward(req, resp);
        } else {
            req.setAttribute("errorMessage", "Registration successful. Please log in.");
            req.getRequestDispatcher("/WEB-INF/jsps/login.jsp").forward(req, resp);
        }
    }
}
