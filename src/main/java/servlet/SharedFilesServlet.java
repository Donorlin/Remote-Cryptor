package servlet;

import database.dao.ShareService;
import database.entity.ShareLog;
import servlet.common.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/received")
public class SharedFilesServlet extends HttpServlet {

    private List<ShareLog> sharedFilesList;
    private String SHARE_DIRECTORY = "D:/apache-tomcat-9.0.12/uploads/share";
//    private String SHARE_DIRECTORY = "/usr/local/apache-tomcat-9.0.12/uploads/share";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("jwt") != null
                && request.getSession().getAttribute("username") != null
                && request.getCookies() != null
                && request.getCookies().length > 0
        ) {
            if (ServletUtils.isAuthenticated(request)) {
                ShareService shareService = new ShareService(SHARE_DIRECTORY);
                String currentUser = (String) request.getSession().getAttribute("username");

                if (request.getParameter("searchWord") != null) {
                    sharedFilesList = shareService.getSharedFilesBySearch(currentUser, request.getParameter("searchWord"));
                } else {
                    sharedFilesList = shareService.getSharedFilesListByUsername(currentUser);
                }

                request.setAttribute("sharedFiles", sharedFilesList);
                request.getRequestDispatcher("/WEB-INF/jsps/views/received.jsp").forward(request, response);
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileIdParameter = request.getParameter("fileId");
        String decryptParameter = request.getParameter("decrypt");
        ShareService shareService = new ShareService(SHARE_DIRECTORY);
        String currentUser = (String) request.getSession().getAttribute("username");

        File fileToSend = null;
        try {
            Long fileId = Long.parseLong(fileIdParameter);
            if ("dont".equals(decryptParameter)) {
                fileToSend = shareService.getSharedFile(fileId);
            } else {
                fileToSend = shareService.decryptSharedFile(currentUser, fileId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        ServletUtils.sendResponseFile(response, fileToSend);
        if(!"dont".equals(decryptParameter)) {
            fileToSend.delete();
        }
    }


}
