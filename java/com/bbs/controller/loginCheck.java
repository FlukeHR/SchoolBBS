package com.bbs.controller;

import com.bbs.pojo.User;
import com.bbs.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/loginCheck")
public class loginCheck extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    doPost(request, response);
  }
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException{
    request.setCharacterEncoding("UTF-8");
    String username = request.getParameter("username");
    String password = request.getParameter("userpwd");
    UserService userService = new UserService();
    try {
      User user = userService.check(username, password);
      if (user != null) {
        request.setAttribute("user", user);
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        session.setAttribute("username_session", username);
        String redirect = request.getParameter("redirect");
        if (redirect != null && redirect.trim().length() > 0) {
          response.sendRedirect(redirect);
          return;
        }
        request.getRequestDispatcher("home.jsp").forward(request, response);
      }
      else {
        String info = "用户名或密码有误!";
        request.setAttribute("outputMessage", info);
        request.getRequestDispatcher("/info.jsp").forward(request, response);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      // 异常时不要返回空白页面，统一转发到信息页（开发期带上错误信息便于排查）
      String err = e.getClass().getSimpleName() + ": " + (e.getMessage() == null ? "" : e.getMessage());
      request.setAttribute("outputMessage", "登录失败，请稍后重试。" + "\\n" + err);
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      request.getRequestDispatcher("/info.jsp").forward(request, response);
    }
  }
}
