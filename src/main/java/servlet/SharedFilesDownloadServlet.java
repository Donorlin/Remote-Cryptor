package servlet;

import database.user.ShareService;
import database.user.UserService;
import servlet.common.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@WebServlet("/recieved")
public class SharedFilesDownloadServlet extends HttpServlet {

    private Map<Long, String> sharedFilesList;
    //    private String SHARE_DIRECTORY = "D:/apache-tomcat-9.0.12/uploads/share";
    private String SHARE_DIRECTORY = "/usr/local/apache-tomcat-9.0.12/uploads/share";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("jwt") != null
                && request.getSession().getAttribute("username") != null
                && request.getCookies() != null
                && request.getCookies().length > 0
        ) {
            if(ServletUtils.isAuthenticated(request)) {
                ShareService shareService = new ShareService(SHARE_DIRECTORY);
                String currentUser = (String) request.getSession().getAttribute("username");
                sharedFilesList = shareService.getSharedFilesListByUsername(currentUser);
                request.setAttribute("sharedFiles", sharedFilesList);
                request.getRequestDispatcher("/WEB-INF/jsps/recieved.jsp").forward(request, response);
                return;
            } else {
                request.setAttribute("errorMessage", "Wrong or no login token.");
                request.getRequestDispatcher("/WEB-INF/jsps/login.jsp").forward(request, response);
                return;
            }
        }
        request.setAttribute("errorMessage", "Please log in.");
        request.getRequestDispatcher("/WEB-INF/jsps/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileIdParameter = request.getParameter("fileId");
        ShareService shareService = new ShareService(SHARE_DIRECTORY);
        String currentUser = (String) request.getSession().getAttribute("username");

        File decryptedFile = null;
        try {
            Long fileId = Long.parseLong(fileIdParameter);
            decryptedFile = shareService.decryptSharedFile(currentUser, fileId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        ServletUtils.sendResponseFile(response, decryptedFile);
        decryptedFile.delete();
    }


}
