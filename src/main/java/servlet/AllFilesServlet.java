package servlet;

import database.dao.ShareService;
import database.entity.ShareLog;
import servlet.common.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/allfiles")
public class AllFilesServlet extends HttpServlet {


    private List<ShareLog> sharedFilesList;
    //    private String SHARE_DIRECTORY = "D:/apache-tomcat-9.0.12/uploads/share";
    private String SHARE_DIRECTORY = "/usr/local/apache-tomcat-9.0.12/uploads/share";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("jwt") != null
                && request.getSession().getAttribute("username") != null
                && request.getCookies() != null
                && request.getCookies().length > 0
        ) {
            if (ServletUtils.isAuthenticated(request)) {
                ShareService shareService = new ShareService(SHARE_DIRECTORY);

                if (request.getParameter("searchWord") != null && !request.getParameter("searchWord").isEmpty()) {
                    sharedFilesList = shareService.getAllSharedFilesBySearch(request.getParameter("searchWord"));
                } else {
                    sharedFilesList = shareService.getAllSharedFiles();
                }

                request.setAttribute("sharedFiles", sharedFilesList);
                request.getRequestDispatcher("/WEB-INF/jsps/views/allfiles.jsp").forward(request, response);
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
