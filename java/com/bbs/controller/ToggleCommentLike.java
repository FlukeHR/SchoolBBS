package com.bbs.controller;

import com.bbs.service.LikeCommentService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/ToggleCommentLike")
public class ToggleCommentLike extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username_session") == null) {
            response.sendRedirect("index.html");
            return;
        }
        String username = (String) session.getAttribute("username_session");
        String commentIdStr = request.getParameter("comment_id");
        String postIdStr = request.getParameter("post_id");
        String redirect = request.getParameter("redirectPage");
        if (redirect == null || redirect.isEmpty()) redirect = "post.jsp";
        if (commentIdStr == null || commentIdStr.isEmpty() || postIdStr == null || postIdStr.isEmpty()) {
            response.sendRedirect("index.html");
            return;
        }
        int commentId = Integer.parseInt(commentIdStr);
        int postId = Integer.parseInt(postIdStr);
        try {
            LikeCommentService svc = new LikeCommentService();
            boolean liked = svc.hasLiked(username, commentId);
            if (liked) svc.unlike(username, commentId); else svc.like(username, commentId);
            response.sendRedirect("post.jsp?id=" + postId);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("post.jsp?id=" + postId);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
