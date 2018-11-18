package servlet;

import servlet.common.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

@WebServlet("/decryptor")
public class DecryptorDownloadServlet extends HttpServlet {

    // private final String JAR_DIRECTORY = "D:/apache-tomcat-9.0.12/uploads/decryptor";
    private String JAR_DIRECTORY = "/usr/local/apache-tomcat-9.0.12/uploads/decryptor";
    private String JAR_FILENAME = "local_decryptor_3000.jar";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            File jar = new File(JAR_DIRECTORY + File.separator + JAR_FILENAME);
            ServletUtils.sendResponseFile(resp, jar);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
