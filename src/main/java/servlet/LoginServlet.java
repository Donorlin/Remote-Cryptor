package servlet;

import database.entity.User;
import database.user.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("user") != null) { // TODO placeholder
            request.setAttribute("errorMessage", "You are already logged in. Please log out if you want to change a user.");
            request.getRequestDispatcher("/WEB-INF/jsps/cryptor.jsp").forward(request, response);
        }
        request.getRequestDispatcher("/WEB-INF/jsps/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        UserService userService = new UserService();
        boolean retVal = userService.checkUserHash(username, password);
        if (!retVal) {
            req.setAttribute("errorMessage", "Bad user name or password");
            req.getRequestDispatcher("WEB-INF/jsps/login.jsp").forward(req, resp);
        } else {
            User thisUser = userService.getUserByUsername(username);
            req.getSession().setAttribute("user", thisUser);
            // TODO napasovat vymysleny token do cookieny a prerobit session aby ukladal tento token a nasledne verifikoval
            // TODO bolo by to fajn spravit Filter triedou
            resp.sendRedirect(req.getContextPath() + "/cryptor");
        }
    }
}
