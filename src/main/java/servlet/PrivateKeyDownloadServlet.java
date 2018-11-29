package servlet;

import database.user.UserService;
import servlet.common.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/privatekey")
public class PrivateKeyDownloadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("jwt") != null
                && request.getSession().getAttribute("username") != null
                && request.getCookies() != null
                && request.getCookies().length > 0
        ) {
            if (ServletUtils.isAuthenticated(request)) {
                String currentUser = (String) request.getSession().getAttribute("username");
                UserService userService = new UserService();
                byte[] privateKeyBytes = userService.getPrivateKeyBytesByUsername(currentUser);
                ServletUtils.sendResponseBytes(response, privateKeyBytes);
                return;
            } else {
                request.setAttribute("errorMessage", "Wrong or no login token.");
                request.getRequestDispatcher("/WEB-INF/jsps/views/login.jsp").forward(request, response);
                return;
            }
        }
        request.setAttribute("errorMessage", "Please log in.");
        request.getRequestDispatcher("/WEB-INF/jsps/views/login.jsp").forward(request, response);
    }
}
