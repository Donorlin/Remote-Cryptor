package servlet.common;

import com.google.gson.Gson;
import servlet.common.dto.ErrorOutputDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ServletUtils {

    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String FILE_CONTENT_TYPE = "application/octet-stream";

    private static final Gson GSON_MAPPER = new Gson();

    public static void sendResponseObject(HttpServletResponse servletResponse, Object responseObject) throws IOException {
        servletResponse.setStatus(HttpServletResponse.SC_OK);
        servletResponse.setContentType(JSON_CONTENT_TYPE);
        servletResponse.getWriter().write(GSON_MAPPER.toJson(responseObject));
    }

    public static void sendResponseFile(HttpServletResponse servletResponse, File responseFile) throws IOException {
        servletResponse.setContentType(FILE_CONTENT_TYPE);
        servletResponse.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", responseFile.getName()));
        servletResponse.setStatus(HttpServletResponse.SC_OK);

        OutputStream out = servletResponse.getOutputStream();
        FileInputStream in = new FileInputStream(responseFile);
        byte[] buffer = new byte[4096];
        int length;
        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }

        in.close();
        out.flush();
    }

    public static void sendError(int httpStatus, String errorCode, String errorMessage, HttpServletResponse servletResponse) throws IOException {
        ErrorOutputDTO error = new ErrorOutputDTO(httpStatus, errorCode, errorMessage);
        servletResponse.setStatus(httpStatus);
        servletResponse.setContentType(JSON_CONTENT_TYPE);
        servletResponse.getWriter().write(GSON_MAPPER.toJson(error));
    }

    public static void sendNotFoundError(String errorMessage, HttpServletResponse servletResponse) throws IOException {
        sendError(HttpServletResponse.SC_NOT_FOUND, "NOT_FOUND", errorMessage, servletResponse);
    }
}
