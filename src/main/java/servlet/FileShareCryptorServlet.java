package servlet;

import crypto.CryptoUtils;
import database.user.ShareService;
import database.user.UserService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import servlet.common.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@WebServlet("/share")
public class FileShareCryptorServlet extends HttpServlet {

    private String SHARE_DIRECTORY = "D:/apache-tomcat-9.0.12/uploads/share";

    // private String UPLOAD_DIRECTORY = "/usr/local/apache-tomcat-9.0.12/uploads";
    private List<String> userList;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserService userService = new UserService();
        userList = userService.getUsernameList();
        request.setAttribute("userList", userList); // TODO minus current user
        request.getRequestDispatcher("/WEB-INF/jsps/share.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        File fileToShare = null;
        String usernameToShareWith = null;

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
                        if (item.getFieldName().equals("shareWith")) {
                            usernameToShareWith = item.getString();
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        ShareService shareService = new ShareService(SHARE_DIRECTORY);
        boolean retVal = shareService.shareFile("xjahodka", usernameToShareWith, fileToShare);
        if (!retVal) {
            req.setAttribute("errorMessage", "Sharing failed.");
            req.setAttribute("userList", userList);
            req.getRequestDispatcher("/WEB-INF/jsps/share.jsp").forward(req, resp);
        } else {
            req.setAttribute("errorMessage", "File is shared with " + usernameToShareWith);
            req.setAttribute("userList", userList);
            req.getRequestDispatcher("/WEB-INF/jsps/share.jsp").forward(req, resp);
        }
        fileToShare.delete();
    }
}
