package com.bbs.controller.trade;

import com.bbs.entity.Comment;
import com.bbs.service.impl.CommentService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/TradeCommentAdd")
public class TradeCommentAdd extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    request.setCharacterEncoding("UTF-8");
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("username_session") == null) {
      response.sendRedirect("index.html");
      return;
    }
    String author = (String) session.getAttribute("username_session");
    String postIdStr = request.getParameter("post_id");
    String content = request.getParameter("content");
    int post_id = Integer.parseInt(postIdStr);

    Comment c = new Comment();
    c.setPost_id(post_id);
    c.setAuthor(author);
    c.setContent(content);

    try {
      CommentService svc = new CommentService();
      svc.createComment(c);
      response.sendRedirect("trade-detail.html?id=" + post_id);
    } catch (Exception e) {
      e.printStackTrace();
      response.sendRedirect("trade-detail.html?id=" + post_id);
    }
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    doPost(request, response);
  }
}
