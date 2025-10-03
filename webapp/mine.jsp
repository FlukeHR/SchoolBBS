<%@ page import="com.bbs.service.PostService" %>
<%@ page import="com.bbs.pojo.Post" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>

<head>
  <meta charset="utf-8" />
  <meta name="author" content="rogepi" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <link rel="stylesheet" href="css/css/universal.css" />
  <link rel="stylesheet" href="css/css/about.css" />
  <link rel="stylesheet" href="css/css/mine.css" />
  <link rel="stylesheet" href="css/css/admin.css" />

  <title>校园贴吧</title>
  <link rel="icon" href="images/mine.png" type="image/x-icon" />
</head>
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
    <li><a href="smile.html">退出登录</a></li>
  </ul>
</div>

<div class="row">
  <div class="Mycard">
    <h3>我的贴子</h3>
    <div style="margin-top:10px;">
      <div id="postScroll" style="max-height:320px;overflow-y:auto;padding-right:6px;">
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
        <div class="post-card" data-id="<%= post.getId() %>" onclick="location.href='post.jsp?id=<%= post.getId() %>'" style="border:1px solid #eee;border-radius:8px;padding:12px;margin-bottom:12px;box-shadow:0 2px 8px rgba(0,0,0,.04);min-height:150px;display:flex;flex-direction:column;cursor:pointer;">
          <div class="pc-title" style="font-size:16px;font-weight:600;margin-bottom:6px;">
            <a href="post.jsp?id=<%= post.getId() %>" onclick="event.stopPropagation();" style="text-decoration:none;color:#333;"><%= post.getTitle() %></a>
          </div>
          <div class="pc-meta" style="font-size:12px;color:#888;margin-bottom:8px;">发表于 <%= post.getDate() %></div>
          <div class="pc-content" style="color:#444;display:-webkit-box;-webkit-line-clamp:3;-webkit-box-orient:vertical;overflow:hidden;"> <%= post.getContent() %> </div>
          <div class="pc-actions" style="margin-top:10px;">
            <form action="editPost.jsp" method="post" style="display:inline;margin-right:8px;" onclick="event.stopPropagation();">
              <input type="hidden" name="id" value="<%=post.getId()%>">
              <button type="submit">编辑</button>
            </form>
            <button class="btn-del" data-id="<%=post.getId()%>" style="display:inline;" onclick="event.stopPropagation();">删除</button>
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

</body>

</html>