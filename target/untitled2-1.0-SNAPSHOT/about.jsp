<%@ page import="com.bbs.service.impl.UserServiceImpl" %>
<%@ page import="com.bbs.entity.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="utf-8" />
  <meta name="author" content="rogepi" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <link rel="stylesheet" href="css/universal.css" />
  <link rel="stylesheet" href="css/about.css" />
  <link rel="stylesheet" href="css/modern-components.css" />
  <title>校园贴吧</title>
  <link rel="icon" href="images/info.png" type="image/x-icon" />
  <script src="js/modern-interactions.js"></script>
</head>
<body>
<div class="container">
  <div class="header">
    <h1>个人信息</h1>
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

<%
  String author = (String) session.getAttribute("username_session");
  UserServiceImpl userService = new UserServiceImpl();
  User user = null;
  if (author != null) {
    try {
      user = userService.findUserByName(author);
      request.setAttribute("user", user);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  if (user == null) {
%>
  <div class="row">
    <div class="card">
      <h3 style="text-align:center;">请先登录</h3>
      <div class="profile" style="text-align:center;">
        <a href="index.html" style="color: cornflowerblue;">前往登录</a>
      </div>
    </div>
  </div>
<%
  } else {
    int len = user.getPassword() != null ? user.getPassword().length() : 0;
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < len; i++) { sb.append("*"); }
%>
  <!-- 个人页面信息展示 -->
  <div class="row">
    <div class="card">
      <h3 style="text-align: center;">个人信息</h3>
      <br>
      <div class="profile">
        <div>用 户 名：&emsp;<%= user.getUsername() %></div>
        <div>密&nbsp;&emsp;码:&emsp;&nbsp;<%= sb.toString() %></div>
        <div>性&nbsp;&emsp;别：&emsp;<%= userService.findSexById(user.getUid()) %></div>
        <div>注册时间：&emsp;<%= user.getRtime() %></div>
        <button type="button" onclick="window.location.href='updateInfo.jsp'">修改个人资料</button>
      </div>
    </div>
    <div class="card" style="margin-top:16px;">
      <h3 style="text-align:center;">个人关注</h3>
      <div style="padding:8px 0;">
        <%
          com.bbs.service.impl.FollowService followSvc = new com.bbs.service.impl.FollowService();
          java.util.List<com.bbs.entity.PostType> followedTypes = null;
          try {
            followedTypes = followSvc.findFollowedTypes(author);
          } catch (Exception e) { e.printStackTrace(); }
          if (followedTypes != null && !followedTypes.isEmpty()) {
            for (com.bbs.entity.PostType t : followedTypes) {
        %>
          <div style="display:flex;align-items:center;justify-content:space-between;border-bottom:1px solid #eee;padding:6px 0;">
            <div>
              <strong><%= t.getTypeName() %></strong>
              <small style="color:#888;margin-left:8px;">ID: <%= t.getId() %></small>
            </div>
            <form action="ToggleFollowType" method="post" style="margin:0;">
              <input type="hidden" name="redirectPage" value="about.jsp">
              <input type="hidden" name="typeId" value="<%= t.getId() %>">
              <button type="submit">取消关注</button>
            </form>
          </div>
        <%
            }
          } else {
        %>
          <div style="text-align:center;color:#666;">暂无关注的贴子类型</div>
        <%
          }
        %>
      </div>
    </div>
  </div>
<%
  }
%>
</div>
</body>
</html>