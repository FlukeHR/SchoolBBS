package com.bbs.controller;

import com.bbs.pojo.User;
import com.bbs.pojo.Post;
import com.bbs.pojo.Comment;
import com.bbs.service.UserService;
import com.bbs.service.PostService;
import com.bbs.service.CommentService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/adminDeleteUser")
public class adminDeleteUser extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uidStr = request.getParameter("uid");
        String redirect = request.getParameter("redirectPage");

        if (uidStr == null || uidStr.isEmpty()) {
            String info = "参数缺失，请检查输入";
            request.setAttribute("outputMessage", info);
            request.getRequestDispatcher("/info.jsp").forward(request, response);
            return;
        }

        try {
            int uid = Integer.parseInt(uidStr);

            UserService userService = new UserService();
            PostService postService = new PostService();
            CommentService commentService = new CommentService();

            // 查询用户名，用于按作者删除帖子与评论
            User target = userService.selectUserById(uid);
            if (target == null) {
                String info = "用户不存在";
                request.setAttribute("outputMessage", info);
                request.getRequestDispatcher("/info.jsp").forward(request, response);
                return;
            }
            String author = target.getUsername();

            // 1) 删除该用户发表的评论（对他人帖子下的评论）
            List<Comment> commentsByUser = commentService.findCommentByAuthor(author);
            if (commentsByUser != null) {
                for (Comment c : commentsByUser) {
                    commentService.removeCommentById(c.getId());
                }
            }

            // 2) 删除该用户发表的帖子及其下所有评论
            List<Post> postsByUser = postService.findPostByAuthor(author);
            if (postsByUser != null) {
                for (Post p : postsByUser) {
                    List<Comment> commentsUnderPost = commentService.findCommentsByPostId(p.getId());
                    if (commentsUnderPost != null) {
                        for (Comment c : commentsUnderPost) {
                            commentService.removeCommentById(c.getId());
                        }
                    }
                    postService.removePost(p.getId());
                }
            }

            // 3) 删除用户账户
            User user = new User();
            user.setUid(uid);
            userService.deleteUser(user);

            if (redirect == null || redirect.isEmpty()) {
                redirect = "admin_selectUser.jsp";
            }
            response.sendRedirect(redirect);
        } catch (Exception e) {
            e.printStackTrace();
            String info = "删除用户失败";
            request.setAttribute("outputMessage", info);
            request.getRequestDispatcher("/info.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
