package servlet.filter;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;


public class PublicCryptorFilter implements Filter {

    private String UPLOAD_DIRECTORY;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        UPLOAD_DIRECTORY = filterConfig.getServletContext().getInitParameter("UPLOAD_DIRECTORY");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;

        if (httpReq.getMethod().equals("POST")) {
            File inputFile = null;
            File key = null;
            String fileName = null;
            String mode = null;
            String xtoken = null;
            if (ServletFileUpload.isMultipartContent(httpReq)) {
                try {
                    List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(httpReq);

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
                            if (item.getFieldName().equals("X-TOKEN")) {
                                xtoken = item.getString();
                            }
                        }
                    }
                } catch(Exception ex) {
                    System.out.println(ex);
                }
            }

            System.out.println("inputFile " + inputFile.getName());
            System.out.println("key " + key.getName());
            System.out.println("fileName " + fileName);
            System.out.println("mode " + mode);
            System.out.println("xtoken " + xtoken);

            httpReq.setAttribute("inputFile", inputFile);
            httpReq.setAttribute("key", key);
            httpReq.setAttribute("fileName", fileName);
            httpReq.setAttribute("mode", mode);
            httpReq.setAttribute("X-TOKEN", xtoken);
            chain.doFilter(httpReq, httpResp);
        } else {
            chain.doFilter(httpReq, httpResp);
        }
    }
}
