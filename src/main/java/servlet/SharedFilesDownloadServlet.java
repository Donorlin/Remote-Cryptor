package servlet;

import database.user.ShareService;
import database.user.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/recieved")
public class SharedFilesDownloadServlet extends HttpServlet {

    private Map<Long, String> sharedFilesList;
    private String SHARE_DIRECTORY = "D:/apache-tomcat-9.0.12/uploads/share";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ShareService shareService = new ShareService(SHARE_DIRECTORY);
        sharedFilesList = shareService.getSharedFilesListByUsername("xsabik"); // TODO actual user
        System.out.println("LIST");
        System.out.println(sharedFilesList);
        request.setAttribute("sharedFiles", sharedFilesList); // TODO minus current user
        request.getRequestDispatcher("/WEB-INF/jsps/recieved.jsp").forward(request, response);
    }


}
