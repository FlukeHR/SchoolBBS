<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.bbs.service.impl.PostService" %>
<%@ page import="com.bbs.service.impl.PostTypeService" %>
<%@ page import="com.bbs.entity.Post" %>
<%@ page import="com.bbs.entity.PostType" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>

<html lang="zh-CN">

<head>
  <meta http-equiv="Expires" content="0">
  <meta http-equiv="kiben" content="no-cache">
  <meta charset="utf-8" />
  <meta name="author" content="rogepi" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <link rel="stylesheet" href="css/universal.css" />
  <link rel="stylesheet" href="css/home.css" />
  <link rel="stylesheet" href="css/modern-components.css" />
  <title>校园贴吧</title>
  <link rel="icon" href="images/index.png" type="image/x-icon" />
  <link rel="shortcut icon" href="images/index.png" type="image/x-icon" />

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
      <li><a href="my_postType.jsp">我的贴吧</a></li>
      <li><a href="publish.html">发帖</a></li>
      <li><a href="about.jsp">个人资料</a></li>
      <li><a href="search.jsp">搜索</a></li>
      <li><a href="trade.html">交易</a></li>
      <li><a href="home_login.jsp">退出登录</a></li>
    </ul>
  </div>

  <div class="row" style="display: flex; justify-content: center;">
    <div class="rightcolumn" style="width: 80%; max-width: 1200px;">
      <%
        // 动态加载所有贴子类型
        PostTypeService postTypeService = new PostTypeService();
        List<PostType> postTypes = null;
        PostService postService = new PostService();
        List<Post> posts = null;
        try {
          postTypes = postTypeService.findAllPostTypes();
        } catch (Exception e) {
          e.printStackTrace();
        }
        
        if (postTypes != null && !postTypes.isEmpty()) {
          for (PostType postType : postTypes) {
          String typeName = postType.getTypeName();
          int typeId = postType.getId();
          boolean followed = false;
          String username = (String) session.getAttribute("username_session");
          if (username != null) {
            try {
              com.bbs.service.impl.FollowService fs = new com.bbs.service.impl.FollowService();
              followed = fs.isFollowed(username, typeId);
            } catch (Exception e) { e.printStackTrace(); }
          }
      %>
      <div class="card" style="margin-bottom: 20px; min-height: 200px;">
        <h3 style="display:flex;align-items:center;gap:10px;">
          <a href="type.jsp?type=<%= java.net.URLEncoder.encode(typeName, "UTF-8") %>" style="text-decoration:underline;color:#ffffff;font-weight:700;">
            <%= typeName %>
          </a>
          <form action="ToggleFollowType" method="post" style="margin:0;">
            <input type="hidden" name="redirectPage" value="home.jsp">
            <input type="hidden" name="typeId" value="<%= typeId %>">
            <button type="submit" style="padding:2px 8px;">
              <%= followed ? "取消关注" : "关注" %>
            </button>
          </form>
        </h3>
        <br>
        <%
          try {
            posts = postService.findPostByType(typeName);
          } catch (Exception e) {
            e.printStackTrace();
          }
          if (posts != null) {
            for (Post post : posts) { %>
        <form action="./post.jsp" method="post" id="form<%=post.getId()%>">
          <label>
            <input type="text" name="id" value="<%=post.getId()%>">
          </label>
          <div class="posta">
            <a href="" onclick="document.getElementById('form<%=post.getId()%>').submit();return false;">
              <%= post.getAuthor() != null ? post.getAuthor() : "匿名" %> · <%= post.getTitle() %> · <%= post.getDate() != null ? post.getDate() : "" %>
            </a>
          </div>
        </form>
        <%
            }
          }
        %>
      </div>
      <%
          }
        }
        else {
      %>
      <div class="card" style="text-align:center;min-height:160px;display:flex;align-items:center;justify-content:center;">
        <div>
          <p>暂无贴子类型。</p>
          <p><a href="my_postType.jsp" style="color:#235D3A;">前往“我的贴吧”创建一个类型</a></p>
        </div>
      </div>
      <%
        }
      %>
    </div>
  </div>
</div>
</body>
