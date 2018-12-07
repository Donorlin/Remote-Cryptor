package servlet;

import servlet.common.ServletUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

@WebServlet("/decryptor")
public class DecryptorDownloadServlet extends HttpServlet {

    private String DECRYPTOR_JAR_DIRECTORY;
    private String DECRYPTOR_JAR_FILENAME;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DECRYPTOR_JAR_DIRECTORY = config.getServletContext().getInitParameter("DECRYPTOR_JAR_DIRECTORY");
        DECRYPTOR_JAR_FILENAME = config.getServletContext().getInitParameter("DECRYPTOR_JAR_FILENAME");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            File jar = new File(DECRYPTOR_JAR_DIRECTORY + File.separator + DECRYPTOR_JAR_FILENAME);
            ServletUtils.sendResponseFile(resp, jar);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
