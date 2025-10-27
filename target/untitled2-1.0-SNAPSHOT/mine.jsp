<%@ page import="com.bbs.service.impl.PostService" %>
<%@ page import="com.bbs.entity.Post" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="utf-8" />
  <meta name="author" content="rogepi" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <link rel="stylesheet" href="css/universal.css" />
  <link rel="stylesheet" href="css/modern-components.css" />
  <link rel="stylesheet" href="css/mine.css" />
  <title>校园贴吧</title>
  <link rel="icon" href="images/mine.png" type="image/x-icon" />
  <script src="js/modern-interactions.js"></script>
</head>
<body>
<div class="container">
  <div class="header">
    <h1>校园贴吧</h1>
    <p>Everything happens for a reason</p>
  </div>

  <div class="topnav">
    <ul>
      <li><a href="home.jsp">首页</a></li>
      <li><a href="mine.jsp">我的</a></li>
      <li><a href="publish.html">发帖</a></li>
      <li><a href="about.jsp">个人资料</a></li>
      <li><a href="search.jsp">搜索</a></li>
      <li><a href="home_login.jsp">退出登录</a></li>
    </ul>
  </div>

<div class="row">
  <div class="card">
    <h3>我的贴子</h3>
    <div class="post-scroll">
      <div class="post-cards">
        <%
          String author = (String) session.getAttribute("username_session");
          PostService postService = new PostService();
          List<Post> posts = null;
          try {
            posts = postService.findPostByAuthor(author);
          } catch (Exception e) {
            e.printStackTrace();
          }
          if (posts != null && !posts.isEmpty()) {
            for (Post post : posts) {
        %>
        <div class="post-card" data-id="<%= post.getId() %>" onclick="location.href='post.jsp?id=<%= post.getId() %>'">
          <div class="pc-title">
            <a href="post.jsp?id=<%= post.getId() %>" onclick="event.stopPropagation();"><%= post.getTitle() %></a>
          </div>
          <div class="pc-meta">发表于 <%= post.getDate() %></div>
          <div class="pc-content"> <%= post.getContent() %> </div>
          <div class="pc-actions">
            <form action="editPost.jsp" method="post" onclick="event.stopPropagation();">
              <input type="hidden" name="id" value="<%=post.getId()%>">
              <button type="submit" class="btn btn-secondary">编辑</button>
            </form>
            <button class="btn btn-danger btn-del" data-id="<%=post.getId()%>" onclick="event.stopPropagation();">删除</button>
          </div>
        </div>
        <%
            }
          } else {
        %>
          <div class="empty-state">暂无帖子，去<a href="publish.html">发帖</a>吧～</div>
        <%
          }
        %>
        </div>
      </div>
    </div>
  </div>
</div>

  <div class="footer">
    <h2>Nothing Is Impossible</h2>
  </div>
</div>

</body>
</html>