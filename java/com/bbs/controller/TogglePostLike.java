package com.bbs.controller;

import com.bbs.service.LikePostService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/TogglePostLike")
public class TogglePostLike extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username_session") == null) {
            response.sendRedirect("index.html");
            return;
        }
        String username = (String) session.getAttribute("username_session");
        String postIdStr = request.getParameter("post_id");
        String redirect = request.getParameter("redirectPage");
        if (postIdStr == null || postIdStr.isEmpty()) {
            response.sendRedirect("index.html");
            return;
        }
        int postId = Integer.parseInt(postIdStr);
        if (redirect == null || redirect.isEmpty()) {
            redirect = "post.jsp";
        }
        try {
            LikePostService svc = new LikePostService();
            boolean liked = svc.hasLiked(username, postId);
            if (liked) svc.unlike(username, postId); else svc.like(username, postId);
            // 保持在帖子详情
            if ("post.jsp".equals(redirect)) {
                response.sendRedirect("post.jsp?id=" + postId);
            } else {
                response.sendRedirect(redirect);
            }
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
