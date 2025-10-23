package com.bbs.controller;

import com.bbs.service.FollowService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/ToggleFollowType")
public class ToggleFollowType extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username_session") == null) {
            response.sendRedirect("index.html");
            return;
        }
        String username = (String) session.getAttribute("username_session");
        String idStr = request.getParameter("typeId");
        String redirect = request.getParameter("redirectPage");
        if (redirect == null || redirect.isEmpty()) redirect = "home.jsp";
        if (idStr == null || idStr.isEmpty()) {
            request.setAttribute("outputMessage", "缺少类型ID");
            try {
                request.getRequestDispatcher("/info.jsp").forward(request, response);
            } catch (Exception ignored) {}
            return;
        }
        try {
            int typeId = Integer.parseInt(idStr);
            FollowService followService = new FollowService();
            boolean followed = followService.isFollowed(username, typeId);
            if (followed) followService.unfollow(username, typeId);
            else followService.follow(username, typeId);
            response.sendRedirect(redirect);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("outputMessage", "操作失败" + e.getMessage());
            try {
                request.getRequestDispatcher("/info.jsp").forward(request, response);
            } catch (Exception ignored) {}
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
