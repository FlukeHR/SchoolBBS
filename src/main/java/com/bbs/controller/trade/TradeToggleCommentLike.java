package com.bbs.controller.trade;

import com.bbs.service.impl.LikeCommentService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/TradeToggleCommentLike")
public class TradeToggleCommentLike extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username_session") == null) {
            response.setStatus(401);
            response.getWriter().write("{\"ok\":false,\"msg\":\"未登录\"}");
            return;
        }
        String username = (String) session.getAttribute("username_session");
        String commentIdStr = request.getParameter("comment_id");
        if (commentIdStr == null || commentIdStr.isEmpty()) {
            response.getWriter().write("{\"ok\":false,\"msg\":\"缺少comment_id\"}");
            return;
        }
        int commentId = Integer.parseInt(commentIdStr);
        try {
            LikeCommentService svc = new LikeCommentService();
            boolean liked = svc.hasLiked(username, commentId);
            if (liked) svc.unlike(username, commentId); else svc.like(username, commentId);
            int likes = svc.countLikes(commentId);
            response.getWriter().write("{\"ok\":true,\"liked\":" + (!liked) + ",\"likes\":" + likes + "}");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"ok\":false,\"msg\":\"失败\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
