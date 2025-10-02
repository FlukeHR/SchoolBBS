package com.bbs.controller;

import com.bbs.pojo.PostType;
import com.bbs.service.PostTypeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/PostTypeCreate")
public class PostTypeCreate extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        String typeName = request.getParameter("typeName");
        String description = request.getParameter("description");
        String redirect = request.getParameter("redirectPage");
        HttpSession session = request.getSession();
        String creator = (String) session.getAttribute("username_session");

        if (creator == null) {
            response.getWriter().print(
                "<script type=\"text/javascript\">\n"
                + "alert('请先登录！');\n"
                + "window.location.href='index.html';\n"
                + "</script>");
            return;
        }

        PostType postType = new PostType();
        postType.setTypeName(typeName);
        postType.setDescription(description);
        postType.setCreator(creator);

        PostTypeService postTypeService = new PostTypeService();
        try {
            // 检查类型名是否已存在
            PostType existingType = postTypeService.findPostTypeByName(typeName);
            if (existingType != null) {
                response.getWriter().print(
                    "<script type=\"text/javascript\">\n"
                    + "alert('该贴子类型已存在！');\n"
                    + "window.history.back();\n"
                    + "</script>");
                return;
            }
            
            postTypeService.createPostType(postType);
            String target = (redirect == null || redirect.isEmpty()) ? "publish.html" : redirect;
            response.getWriter().print(
                "<script type=\"text/javascript\">\n"
                + "alert('贴子类型创建成功！');\n"
                + "window.location.href='" + target + "';\n"
                + "</script>");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().print(
                "<script type=\"text/javascript\">\n"
                + "alert('创建失败：" + e.getMessage() + "');\n"
                + "window.history.back();\n"
                + "</script>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        doPost(request, response);
    }
}
