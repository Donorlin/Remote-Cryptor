package servlet.common;

import com.google.gson.Gson;
import jdk.internal.util.xml.impl.Input;
import servlet.common.dto.ErrorOutputDTO;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.Optional;

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

    public static void sendResponseBytes(HttpServletResponse servletResponse, byte[] responseBytes) throws IOException {
        servletResponse.setContentType(FILE_CONTENT_TYPE);
        servletResponse.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", "private_key.der"));
        servletResponse.setStatus(HttpServletResponse.SC_OK);

        OutputStream out = servletResponse.getOutputStream();
        InputStream in = new ByteArrayInputStream(responseBytes);
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

    public static Optional<String> getJWTCookieValue(Cookie[] cookies) {
        return Arrays.stream(cookies).filter(c -> ("token").equals(c.getName())).map(Cookie::getValue).findFirst();
    }

    public static boolean isAuthenticated(HttpServletRequest request) {
        if (ServletUtils.getJWTCookieValue(request.getCookies()).isPresent()) {
            String token = ServletUtils.getJWTCookieValue(request.getCookies()).get();
            String username = (String) request.getSession().getAttribute("username");
            if (JWTUtils.verifyJWT(username, token)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
