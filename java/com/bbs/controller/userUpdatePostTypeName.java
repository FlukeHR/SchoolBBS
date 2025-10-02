package com.bbs.controller;

import com.bbs.pojo.Post;
import com.bbs.pojo.PostType;
import com.bbs.service.PostService;
import com.bbs.service.PostTypeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/userUpdatePostTypeName")
public class userUpdatePostTypeName extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username_session") == null) {
            response.sendRedirect("index.html");
            return;
        }
        String currentUser = (String) session.getAttribute("username_session");

        String idStr = request.getParameter("id");
        String newName = request.getParameter("newName");
        String redirect = request.getParameter("redirectPage");
        if (redirect == null || redirect.isEmpty()) {
            redirect = "my_postType.jsp";
        }
        if (idStr == null || idStr.isEmpty() || newName == null || newName.trim().isEmpty()) {
            String info = "参数缺失：类型ID或新名称";
            request.setAttribute("outputMessage", info);
            request.getRequestDispatcher("/info.jsp").forward(request, response);
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            PostTypeService typeService = new PostTypeService();
            PostService postService = new PostService();

            PostType type = typeService.findPostTypeById(id);
            if (type == null) {
                String info = "类型不存在";
                request.setAttribute("outputMessage", info);
                request.getRequestDispatcher("/info.jsp").forward(request, response);
                return;
            }
            if (type.getCreator() == null || !type.getCreator().equals(currentUser)) {
                String info = "无权操作：只能管理自己创建的类型";
                request.setAttribute("outputMessage", info);
                request.getRequestDispatcher("/info.jsp").forward(request, response);
                return;
            }

            String oldName = type.getTypeName();
            String trimmedNew = newName.trim();

            // 先将所有旧类型名称的帖子更新为新名称
            List<Post> posts = postService.findAllPostByType(oldName);
            if (posts != null) {
                for (Post p : posts) {
                    p.setPostType(trimmedNew);
                    postService.updatePost(p);
                }
            }

            // 更新类型名称
            type.setTypeName(trimmedNew);
            typeService.updatePostTypeName(type);

            response.sendRedirect(redirect);
        } catch (Exception e) {
            e.printStackTrace();
            String info = "更新贴子类型名称失败：" + e.getMessage();
            request.setAttribute("outputMessage", info);
            request.getRequestDispatcher("/info.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
