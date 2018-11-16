package servlet;

import database.user.UserService;

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
        if (request.getSession().getAttribute("user") != null) {
            request.setAttribute("errorMessage", "You are already logged in. Please log out if you want to register a new user.");
            request.getRequestDispatcher("/WEB-INF/jsps/cryptor.jsp").forward(request, response);
        }
        request.getRequestDispatcher("/WEB-INF/jsps/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
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
