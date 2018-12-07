package servlet;

import crypto.CryptoUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import servlet.common.ServletUtils;

import javax.servlet.ServletConfig;
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

    private String UPLOAD_DIRECTORY;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        UPLOAD_DIRECTORY = config.getServletContext().getInitParameter("UPLOAD_DIRECTORY");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsps/views/cryptor.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            File inputFile = (File) req.getAttribute("inputFile");;
            File key = (File) req.getAttribute("key");
            String fileName = (String) req.getAttribute("fileName");
            String mode = (String) req.getAttribute("mode");
            File outputFile = null;

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
