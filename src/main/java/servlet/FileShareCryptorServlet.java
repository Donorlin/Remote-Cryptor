package servlet;

import database.dao.ShareService;
import database.dao.UserService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import servlet.common.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@WebServlet("/share")
public class FileShareCryptorServlet extends HttpServlet {

//    private String SHARE_DIRECTORY = "D:/apache-tomcat-9.0.12/uploads/share";
     private String SHARE_DIRECTORY = "/usr/local/apache-tomcat-9.0.12/uploads/share";

    private List<String> userList;

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

        File fileToShare = null;
        String usernameToShareWith = null;
        String currentUser = (String) req.getSession().getAttribute("username");
        String comment = null;

        if (ServletFileUpload.isMultipartContent(req)) {
            try {
                List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(req);

                for (FileItem item : multiparts) {
                    if (!item.isFormField()) {
                        if (item.getFieldName().equals("inputFile")) {
                            String fileName = new File(item.getName()).getName();
                            fileToShare = new File(SHARE_DIRECTORY + File.separator + fileName);
                            item.write(fileToShare);
                        }
                    } else {
                        if (("shareWith").equals(item.getFieldName())) {
                            usernameToShareWith = item.getString();
                        }
                        if (("comment").equals(item.getFieldName())) {
                            comment = item.getString();
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        ShareService shareService = new ShareService(SHARE_DIRECTORY);
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
