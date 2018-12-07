package servlet;

import database.dao.ShareService;
import database.dao.UserService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import servlet.common.ServletUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/share", name = "FileShareCryptorServlet")
public class FileShareCryptorServlet extends HttpServlet {

    private List<String> userList;
    private String SHARE_DIRECTORY;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SHARE_DIRECTORY = config.getServletContext().getInitParameter("SHARE_DIRECTORY");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("jwt") != null
                && request.getSession().getAttribute("username") != null
                && request.getCookies() != null
                && request.getCookies().length > 0
        ) {
            if (ServletUtils.isAuthenticated(request)) {
                fillUserlist(request);
                request.getRequestDispatcher("/WEB-INF/jsps/views/share.jsp").forward(request, response);
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        File fileToShare = (File)req.getAttribute("fileToShare");
        String usernameToShareWith = (String)req.getAttribute("shareWith");
        String comment = (String)req.getAttribute("comment");
        String currentUser = (String) req.getSession().getAttribute("username");


        ShareService shareService = new ShareService(SHARE_DIRECTORY);
        System.out.println(usernameToShareWith);
        System.out.println(currentUser);
        System.out.println(comment);
        System.out.println(fileToShare.getName());

        boolean retVal = shareService.shareFile(currentUser, usernameToShareWith, fileToShare, comment);
        fillUserlist(req);
        if (!retVal) {
            req.setAttribute("errorMessage", "Sharing failed.");
            req.getRequestDispatcher("/WEB-INF/jsps/views/share.jsp").forward(req, resp);
        } else {
            req.setAttribute("errorMessage", "File is shared with " + usernameToShareWith);
            req.getRequestDispatcher("/WEB-INF/jsps/views/share.jsp").forward(req, resp);
        }
        fileToShare.delete();
    }

    private void fillUserlist(HttpServletRequest request) {
        UserService userService = new UserService();
        String currentUser = (String) request.getSession().getAttribute("username");
        userList = userService.getUsernameList();
        userList.remove(currentUser);
        request.setAttribute("userList", userList);
    }
}
