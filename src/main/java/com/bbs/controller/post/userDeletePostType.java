package com.bbs.controller.post;

import com.bbs.entity.Post;
import com.bbs.entity.PostType;
import com.bbs.service.impl.CommentService;
import com.bbs.service.impl.PostService;
import com.bbs.service.impl.PostTypeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/userDeletePostType")
public class userDeletePostType extends HttpServlet {
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
        String redirect = request.getParameter("redirectPage");
        if (redirect == null || redirect.isEmpty()) {
            redirect = "my_postType.jsp";
        }
        if (idStr == null || idStr.isEmpty()) {
            String info = "参数缺失：类型ID";
            request.setAttribute("outputMessage", info);
            request.getRequestDispatcher("/info.jsp").forward(request, response);
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            PostTypeService typeService = new PostTypeService();
            PostService postService = new PostService();
            CommentService commentService = new CommentService();

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
            String typeName = type.getTypeName();

            // 删除该类型下所有帖子及其评论
            List<Post> posts = postService.findAllPostByType(typeName);
            if (posts != null) {
                for (Post p : posts) {
                    if (commentService.findCommentsByPostId(p.getId()) != null) {
                        for (int i = 0; i < commentService.findCommentsByPostId(p.getId()).size(); i++) {
                            commentService.removeCommentById(commentService.findCommentsByPostId(p.getId()).get(i).getId());
                        }
                    }
                    postService.removePost(p.getId());
                }
            }

            // 删除类型
            typeService.deletePostType(id);

            response.sendRedirect(redirect);
        } catch (Exception e) {
            e.printStackTrace();
            String info = "删除贴子类型失败：" + e.getMessage();
            request.setAttribute("outputMessage", info);
            request.getRequestDispatcher("/info.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
