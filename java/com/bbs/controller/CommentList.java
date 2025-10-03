package com.bbs.controller;

import com.bbs.pojo.Comment;
import com.bbs.service.CommentService;
import com.bbs.service.LikeCommentService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/CommentList")
public class CommentList extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        String postId = request.getParameter("post_id");
        if (postId == null || postId.trim().isEmpty()) { response.getWriter().write("{\"ok\":false,\"msg\":\"缺少post_id\"}"); return; }
        try {
            int id = Integer.parseInt(postId);
            CommentService svc = new CommentService();
            List<Comment> list = svc.findCommentsByPostId(id);
            LikeCommentService likeSvc = new LikeCommentService();
            StringBuilder sb = new StringBuilder();
            sb.append("{\"ok\":true,\"data\":[");
            boolean first = true;
            for (Comment c : list) {
                if (!first) sb.append(','); first = false;
                int likes = 0;
                try { likes = likeSvc.countLikes(c.getId()); } catch (Exception ignore) {}
                sb.append('{')
                  .append("\"id\":").append(c.getId()).append(',')
                  .append("\"author\":\"").append(esc(c.getAuthor())).append("\",")
                  .append("\"content\":\"").append(esc(c.getContent())).append("\",")
                  .append("\"date\":\"").append(c.getDate()).append("\",")
                  .append("\"likes\":").append(likes)
                  .append('}');
            }
            sb.append("]}");
            response.getWriter().write(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"ok\":false,\"msg\":\"失败\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doGet(req, resp);
    }

    private String esc(String s){ if (s==null) return ""; return s.replace("\\","\\\\").replace("\"","\\\"").replace("\n"," ").replace("\r"," "); }
}
