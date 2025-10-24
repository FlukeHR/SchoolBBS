<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bbs.service.PostService" %>
<%@ page import="com.bbs.pojo.Post" %>
<%@ page import="com.bbs.service.PostTypeService" %>
<%@ page import="com.bbs.pojo.PostType" %>
<%@ page import="java.util.List" %>
<html lang="zh-CN">
<head>
    <meta charset="utf-8" />
    <meta name="author" content="rogepi" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>编辑帖子 - 校园贴吧</title>
    <link rel="icon" href="images/postManage.png" type="image/x-icon" />
    <link rel="stylesheet" href="css/css/universal.css" />
  <link rel="stylesheet" href="css/css/modern-components.css" />
  <link rel="stylesheet" href="css/css/trade.css" />
  <style>
    .edit-wrap { display: flex; gap: 16px; flex-wrap: wrap; }
    .edit-main { flex: 1; min-width: 280px; }
    .form-group { margin-bottom: 16px; }
    .form-group label { display: block; font-weight: 600; margin-bottom: 6px; color: #333; }
    .form-group input, .form-group textarea, .form-group select {
      width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 6px; font-size: 14px; box-sizing: border-box;
    }
    .form-group textarea { min-height: 150px; resize: vertical; }
    .form-group button {
      padding: 10px 20px; background-color: #007BFF; color: white; border: none;
      border-radius: 6px; cursor: pointer; font-size: 14px; margin-top: 8px;
    }
    .form-group button:hover { background-color: #0056b3; }
    .return-link { margin-bottom: 12px; display: inline-block; color: #007BFF; text-decoration: none; }
  </style>
  <script src="js/modern-interactions.js"></script>
</head>
<body>
<div class="container">
  <div class="topnav">
    <ul>
        <li><a href="home.jsp">首页</a></li>
        <li><a href="mine.jsp">我的</a></li>
        <li><a href="publish.html">发帖</a></li>
        <li><a href="about.jsp">个人资料</a></li>
        <li><a href="smile.html">退出登录</a></li>
    </ul>
  </div>

<div class="row">
    <div class="card">
        <a href="mine.jsp" class="return-link">← 返回我的帖子</a>
        <h2>编辑帖子</h2>
        <%
            int id = Integer.parseInt(request.getParameter("id"));
            PostService postService = new PostService();
            Post post = null;
            try {
                post = postService.findPostById(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (post != null) {
        %>
        <div class="edit-wrap">
            <div class="edit-main">
                <form action="UpdatePost" method="post">
                    <input type="hidden" name="id" value="<%= post.getId() %>">
                    <div class="form-group">
                        <label for="title">标题</label>
                        <input type="text" id="title" name="title" value="<%= post.getTitle() %>" required>
                    </div>
                    <div class="form-group">
                        <label for="content">内容</label>
                        <textarea id="content" name="content" required><%= post.getContent() %></textarea>
                    </div>
                    <div class="form-group">
                        <label for="type">类型</label>
                        <select id="type" name="type">
                    <% 
                        PostTypeService postTypeService = new PostTypeService();
                        List<PostType> postTypes = null;
                        try {
                            postTypes = postTypeService.findAllPostTypes();
                            if (postTypes != null && !postTypes.isEmpty()) {
                                for (PostType postType : postTypes) {
                                    String typeName = postType.getTypeName();
                    %>
                                    <option value="<%= typeName %>" <%= typeName.equals(post.getPostType()) ? "selected" : "" %>><%= typeName %></option>
                    <% 
                                }
                            } else {
                                // 如果没有类型，保留默认选项
                    %>
                                    <option value="学习讨论" <%= "学习讨论".equals(post.getPostType()) ? "selected" : "" %>>学习讨论</option>
                                    <option value="校园生活" <%= "校园生活".equals(post.getPostType()) ? "selected" : "" %>>校园生活</option>
                                    <option value="表白墙" <%= "表白墙".equals(post.getPostType()) ? "selected" : "" %>>表白墙</option>
                    <% 
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            // 异常情况下显示默认选项
                    %>
                            <option value="学习讨论" <%= "学习讨论".equals(post.getPostType()) ? "selected" : "" %>>学习讨论</option>
                            <option value="校园生活" <%= "校园生活".equals(post.getPostType()) ? "selected" : "" %>>校园生活</option>
                            <option value="表白墙" <%= "表白墙".equals(post.getPostType()) ? "selected" : "" %>>表白墙</option>
                    <% 
                        }
                    %>
                        </select>
                    </div>
                    <div class="form-group">
                        <button type="submit">保存更改</button>
                    </div>
                </form>
            </div>
        </div>
        <%
            } else {
        %>
        <p>帖子不存在或已被删除。</p>
        <%
            }
        %>
    </div>
</div>

  <div class="footer">
    <h2>Nothing Is Impossible</h2>
  </div>

<div class="footer">
    <h2>Nothing Is Impossible</h2>
</div>
</div>
</body>
</html>