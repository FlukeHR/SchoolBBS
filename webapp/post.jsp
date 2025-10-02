<%@ page import="java.util.List" %>
<%@ page import="com.bbs.service.PostService" %>
<%@ page import="com.bbs.service.LikePostService" %>
<%@ page import="com.bbs.service.LikeCommentService" %>
<%@ page import="com.bbs.pojo.Post" %>
<%@ page import="com.bbs.service.CommentService" %>
<%@ page import="com.bbs.pojo.Comment" %>
<%--
  Created by IntelliJ IDEA.
  User: lc
  Date: 2025/3/11
  Time: 17:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>

<html lang="zh-CN">

<head>
  <meta charset="utf-8" />
  <meta name="author" content="rogepi" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <link rel="stylesheet" href="css/universal.css" />
  <link rel="stylesheet" href="css/about.css" />
  <link rel="stylesheet" href="css/post.css" />
  <script src="js/post.js"></script>
  <title>校园贴吧</title>
  <link rel="icon" href="images/post.png" type="image/x-icon" />
</head>

<body>
<div class="header">
  <h1>校园贴吧</h1>
  <p>Everything happens for a reason</p>
</div>

<div class="topnav">
  <ul>
    <li><a href="home.jsp">首页</a></li>
    <a href="javascript:history.back(-1)">返回</a>
    <li><a href="mine.jsp">我的</a></li>
    <li><a href="publish.html">发帖</a></li>
    <li><a href="about.jsp">个人资料</a></li>
    <li><a href="search.jsp">搜索</a></li>
    <li><a href="smile.html">退出登录</a></li>
  </ul>
</div>

<div class="row">
  <div class="card">
    <%
      int id = Integer.parseInt(request.getParameter("id"));
      PostService postService = new PostService();
      Post post = new Post();
      post.setId(id);
      Post post1 = null;
      try {
        post1 = postService.findPostById(post.getId());
      } catch (Exception e) {
        e.printStackTrace();
      }
      // 检查 post1 是否为 null
      if (post1 == null) {
    %>
    <h3>帖子不存在或已被删除</h3>
    <%
    } else {
    %>
    <h3><%= post1.getTitle() %></h3>
    <div class="ifmt">
      <div class="author">
        <p>作者：<%= post1.getAuthor() %></p>
      </div>
      <div class="type">
        <p>类型：<%= post1.getPostType() %></p>
      </div>
      <div class="date">
        <p>时间：<%= post1.getDate() %></p>
      </div>
    </div>
    <div class="content">
      <%= post1.getContent() %>
    </div>
    <div style="margin-top:8px;display:flex;align-items:center;gap:12px;">
      <%
        // 帖子点赞信息
        String currUser = (String) session.getAttribute("username_session");
        int likeCount = 0;
        boolean liked = false;
        try {
          LikePostService lps = new LikePostService();
          likeCount = lps.countLikes(post1.getId());
          if (currUser != null) {
            liked = lps.hasLiked(currUser, post1.getId());
          }
        } catch (Exception e) { e.printStackTrace(); }
      %>
      <span>点赞数：<%= likeCount %></span>
      <% if (currUser != null) { %>
      <form action="TogglePostLike" method="post" style="display:inline-block;margin:0;">
        <input type="hidden" name="redirectPage" value="post.jsp">
        <input type="hidden" name="post_id" value="<%= post1.getId() %>">
        <button type="submit"><%= liked ? "取消点赞" : "点赞" %></button>
      </form>
      <% } %>
    </div>
    
    <%
      // 显示媒体文件（图片和视频）
      String mediaFiles = post1.getMediaFiles();
      if (mediaFiles != null && !mediaFiles.isEmpty()) {
        String[] files = mediaFiles.split(",");
    %>
    <div class="media-content" style="margin-top: 20px;">
      <%
        for (String file : files) {
          file = file.trim();
          String lowerFile = file.toLowerCase();
          if (lowerFile.endsWith(".jpg") || lowerFile.endsWith(".jpeg") || 
              lowerFile.endsWith(".png") || lowerFile.endsWith(".gif") || 
              lowerFile.endsWith(".webp")) {
      %>
      <img src="<%= request.getContextPath() + "/" + file %>" alt="图片" style="max-width: 100%; margin: 10px 0; border-radius: 5px;">
      <%
          } else if (lowerFile.endsWith(".mp4") || lowerFile.endsWith(".webm") || 
                     lowerFile.endsWith(".ogg") || lowerFile.endsWith(".mov")) {
      %>
      <video controls style="max-width: 100%; margin: 10px 0; border-radius: 5px;">
        <source src="<%= request.getContextPath() + "/" + file %>" type="video/<%= file.substring(file.lastIndexOf(".") + 1) %>">
        您的浏览器不支持视频播放。
      </video>
      <%
          }
        }
      %>
    </div>
    <%
      }
    %>
    <%
      }
    %>
  </div>

  <div class="card comment">
    <hr>
    <div class="title">
      <div class="text">
        <h3>评论</h3>
      </div>
      <div class="btn">
        <button onclick="input_show()">+</button>
      </div>
    </div><br>
    <div class="cinput" id="ci" style="display: none;">
      <form action="CommentAdd" method="post">
        <input type="text" id="content" name="content">
        <% if (post1 != null) { %>
        <input style="display: none" type="text" id="post_id" name="post_id" value="<%= post1.getId() %>">
        <% } %>
        <div class="btn">
          <button type="submit">评论</button>
        </div>
      </form>
    </div>
    <br>
    <div class="comments">
      <%
        if (post1 != null) {
          CommentService commentService = new CommentService();
          List<Comment> comments = null;
          try {
            comments = commentService.findCommentByPostId(post1.getId());
          } catch (Exception e) {
            e.printStackTrace();
          }
          if (comments != null) {
            for (Comment comment : comments) {
      %>
      <div style="display:flex;align-items:center;justify-content:space-between;gap:8px;">
        <h5 style="margin:6px 0;"> <%= comment.getAuthor() %> : <%= comment.getContent() %> </h5>
        <div>
          <%
            int cLikeCount = 0;
            boolean cLiked = false;
            String currUser2 = (String) session.getAttribute("username_session");
            try {
              LikeCommentService lcs = new LikeCommentService();
              cLikeCount = lcs.countLikes(comment.getId());
              if (currUser2 != null) {
                cLiked = lcs.hasLiked(currUser2, comment.getId());
              }
            } catch (Exception e) { e.printStackTrace(); }
          %>
          <span style="margin-right:6px;">赞：<%= cLikeCount %></span>
          <% if (currUser2 != null) { %>
          <form action="ToggleCommentLike" method="post" style="display:inline-block;margin:0;">
            <input type="hidden" name="redirectPage" value="post.jsp">
            <input type="hidden" name="post_id" value="<%= post1.getId() %>">
            <input type="hidden" name="comment_id" value="<%= comment.getId() %>">
            <button type="submit"><%= cLiked ? "取消点赞" : "点赞" %></button>
          </form>
          <% } %>
        </div>
      </div>
      <hr style="border:none;border-top:1px solid #eee;"/>
      <%
            }
          }
        }
      %>
    </div>
  </div>

</div>

<div class="footer">
  <h2>Nothing Is Impossible</h2>
</div>

</body>

</html>