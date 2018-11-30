package servlet;

import database.dao.CommentService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/comment")
public class CommentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileId = req.getParameter("fileId");
        String comment = req.getParameter("comment");
        String currentUser = (String) req.getSession().getAttribute("username");

        System.out.println("FileId " + fileId);
        System.out.println("Comment " + comment);
        System.out.println("User " + currentUser);

        CommentService commentService = new CommentService();
        commentService.addComment(fileId, currentUser, comment);

        resp.sendRedirect(req.getContextPath() + "/received");
    }
}
