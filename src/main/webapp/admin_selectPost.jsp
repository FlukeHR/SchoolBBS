<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bbs.service.impl.PostService" %>
<%@ page import="com.bbs.entity.Post" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>帖子管理</title>
    <link rel="stylesheet" href="css/universal.css">
    <link rel="stylesheet" href="css/modern-components.css">
    <link rel="icon" href="images/postManage.png" type="image/x-icon" />
    <script src="js/modern-interactions.js"></script>
</head>
<body>
<div class="container">
<div class="header">
    <h1>帖子管理</h1>
    <button onclick="location.href='admin_index.html'" class="btn btn-secondary">返回管理页</button>
</div>
    <!-- 显示所有帖子 -->
    <h2 style="text-align: center;">帖子列表</h2>
    <div class="card">
    <table id="postsTable" class="data-table" style="width: 100%; border-collapse: separate; border-spacing: 0; border: 1px solid var(--border-color);">
        <thead style="background-color: var(--bg-secondary);">
            <tr>
                <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 80px;">ID</th>
                <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 20%;">标题</th>
                <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 40%;">内容</th>
                <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 15%;">作者</th>
                <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 10%;">操作</th>
            </tr>
        </thead>
        <tbody>
            <%
                PostService postService = new PostService();
                List<Post> posts = postService.findAllPost();
                for (Post post : posts) {
            %>
            <tr style="border-bottom: 1px solid var(--border-color);">
                <td style="padding: 12px; border-right: 1px solid var(--border-color);"><%= post.getId() %></td>
                <td style="padding: 12px; border-right: 1px solid var(--border-color);"><%= post.getTitle() %></td>
                <td style="padding: 12px; border-right: 1px solid var(--border-color);" class="content-preview"><%= post.getContent() %></td>
                <td style="padding: 12px; border-right: 1px solid var(--border-color);"><%= post.getAuthor() %></td>
                <td style="padding: 12px;">
                    <form action="adminDeletePost" method="post" class="inline-form">
                        <input type="hidden" name="redirectPage" value="admin_selectPost.jsp">
                        <input type="hidden" name="id" value="<%= post.getId() %>">
                        <button type="submit" class="btn btn-danger">删除</button>
                    </form>
                </td>
            </tr>
            <%
                }
            %>
        </tbody>
    </table>
    </div>
</div>
</body>
</html>