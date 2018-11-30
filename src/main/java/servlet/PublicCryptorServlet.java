package servlet;

import crypto.CryptoUtils;
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

@WebServlet("/cryptor")
public class PublicCryptorServlet extends HttpServlet {
    private static long serialVersionUID = 1L;
     private final String UPLOAD_DIRECTORY = "D:/apache-tomcat-9.0.12/uploads";
//    private String UPLOAD_DIRECTORY = "/usr/local/apache-tomcat-9.0.12/uploads";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsps/views/cryptor.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        if (ServletFileUpload.isMultipartContent(req)) {
            try {
                List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(req);
                File inputFile = null;
                File outputFile = null;
                File key = null;
                String fileName = null;
                String mode = null;
                for (FileItem item : multiparts) {
                    if (!item.isFormField()) {
                        if (item.getFieldName().equals("inputFile")) {
                            fileName = new File(item.getName()).getName();
                            inputFile = new File(UPLOAD_DIRECTORY + File.separator + fileName);
                            item.write(inputFile);
                        }
                        if (item.getFieldName().equals("key")) {
                            String keyFileName = new File(item.getName()).getName();
                            key = new File(UPLOAD_DIRECTORY + File.separator + keyFileName);
                            item.write(key);
                        }
                    } else {
                        if (item.getFieldName().equals("type")) {
                            mode = item.getString();
                        }
                    }
                }

                if ("encrypt".equals(mode)) {
                    outputFile = new File(UPLOAD_DIRECTORY + File.separator + fileName + ".enc");
                    CryptoUtils.encrypt(inputFile, outputFile, key);
                } else if ("decrypt".equals(mode)) {
                    outputFile = new File(UPLOAD_DIRECTORY + File.separator + fileName + ".dec");
                    CryptoUtils.decrypt(inputFile, outputFile, key);
                }

                ServletUtils.sendResponseFile(resp, outputFile);

                inputFile.delete();
                outputFile.delete();
                key.delete();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }
}
