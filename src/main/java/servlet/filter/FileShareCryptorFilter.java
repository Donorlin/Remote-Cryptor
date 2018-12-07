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

public class FileShareCryptorFilter implements Filter {

    private String SHARE_DIRECTORY;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        SHARE_DIRECTORY = filterConfig.getServletContext().getInitParameter("SHARE_DIRECTORY");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;

        if (httpReq.getMethod().equals("POST")) {
            File fileToShare = null;
            String usernameToShareWith = null;
            String comment = null;
            String xtoken = null;

            if (ServletFileUpload.isMultipartContent(httpReq)) {
                try {
                    List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(httpReq);

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
                            }if("X-TOKEN".equals(item.getFieldName())) {
                                xtoken = item.getString();
                            }
                        }
                    }
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }

            httpReq.setAttribute("shareWith", usernameToShareWith);
            httpReq.setAttribute("comment", comment);
            httpReq.setAttribute("fileToShare", fileToShare);
            httpReq.setAttribute("X-TOKEN", xtoken);
            chain.doFilter(httpReq, httpResp);
        } else {
            chain.doFilter(httpReq, httpResp);
        }
    }

}



