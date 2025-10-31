<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bbs.service.impl.PostService" %>
<%@ page import="com.bbs.entity.Post" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="zh-CN">
<head>
    <meta charset="utf-8" />
    <meta name="author" content="rogepi" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="stylesheet" href="css/universal.css" />
    <link rel="stylesheet" href="css/modern-components.css" />
    <link rel="stylesheet" href="css/search.css" />
    <title>校园贴吧</title>
    <link rel="icon" href="images/search.png" type="image/x-icon" />
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
            </div>
        </div>
    </div>

<div class="row">
    <div class="card">
        <h3>搜索帖子</h3>
        <form action="search.jsp" method="get" class="search-form">
            <input type="text" name="keyword" placeholder="请输入关键字" required class="form-input">
            <button type="submit" class="btn btn-primary">搜索</button>
        </form>
    </div>
</div>
<%
    // 获取请求参数中的关键字
    String keyword = request.getParameter("keyword");
    if (keyword != null && !keyword.isEmpty()) {
        // 创建PostService实例
        PostService postService = new PostService();
        // 调用searchPosts方法获取搜索结果
        List<Post> searchResults = null;
        try {
            searchResults = postService.searchPosts(keyword);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 显示搜索结果
        if (searchResults != null && !searchResults.isEmpty()) {
%>
<div class="row">
    <div class="card search-results">
        <h3 style="color: white">搜索结果</h3>
        <div class="post-list">
            <div class="post-items">
            <%
                for (Post post : searchResults) {
            %>
            <div class="post-item">
                <h4><a href="post.jsp?id=<%= post.getId() %>"><%= post.getTitle() %></a></h4>
                <div class="post-meta">
                    <span>作者：<%= post.getAuthor() %></span>
                    <span>类型：<%= post.getPostType() %></span>
                    <span>时间：<%= post.getDate() %></span>
                </div>
            </div>
            <%
                }
            %>
            </div>
        </div>
    </div>
</div>
<%
} else {
%>
<div class="row">
    <div class="card empty-state">
        <h3>没有找到相关帖子</h3>
        <p>试试其他关键词或浏览<a href="home.jsp">首页</a>推荐内容</p>
    </div>
</div>
<%
        }
    }
%>

    <div class="footer">
        <h2>Nothing Is Impossible</h2>
    </div>
</div>
</body>
</html>