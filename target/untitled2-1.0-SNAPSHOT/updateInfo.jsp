<%@ page import="com.bbs.service.UserService" %>
<%@ page import="com.bbs.pojo.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="zh-CN">
<head>
  <meta charset="utf-8" />
  <meta name="author" content="rogepi" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <link rel="stylesheet" href="css/css/universal.css" />
  <link rel="stylesheet" href="css/css/modern-components.css" />
  <link rel="stylesheet" href="css/css/about.css" />
  <title>修改个人信息</title>
  <link rel="icon" href="images/userManage.png" type="image/x-icon" />
  <link rel="stylesheet" href="css/css/updateInfo.css" />
  <script src="js/modern-interactions.js"></script>
</head>
<body>
<div class="container">
  <div class="header">
    <h1>修改个人信息</h1>
    <p>Everything happens for a reason</p>

  <div class="topnav">
  <ul>
    <li><a href="home.jsp">首页</a></li>
    <li><a href="mine.jsp">我的</a></li>
    <li><a href="publish.html">发帖</a></li>
    <li><a href="about.jsp">个人资料</a></li>
    <li><a href="home_login.jsp">退出登录</a></li>
  </ul>
</div>

<%
  String author = (String) session.getAttribute("username_session");
  UserService userService = new UserService();
  User user = null;
  if (author != null) {
    try {
      user = userService.findUserByName(author);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
%>

<div class="row">
  <div class="card">
    <h3>修改个人信息</h3>
    <br>
    <div class="form-container">
      <form action="UpdateInfo" method="POST">
        <div class="form-group">
          <label for="username">用户名：</label>
          <input type="hidden" id = "oldName" name="oldName" value="<%=author %>" readonly>
          <input type="text" id="username" name="username" value="<%= user != null ? user.getUsername() : "" %>" required>
        </div>
        <div class="form-group">
          <label for="password">密码：</label>
          <input type="hidden" id = "oldPassword" name="oldPassword" value="<%=user.getPassword() %>" readonly>
          <input type="password" id="password" name="password" value="<%= user != null ? user.getPassword() : "" %>" required>
        </div>
        <div class="form-group">
          <label for="sex">性别：</label>
          <select id="sex" name="sex" style="height: 35px">
            <option value="男" <%= user != null && "男".equals(user.getSex()) ? "selected" : "" %>>男</option>
            <option value="女" <%= user != null && "女".equals(user.getSex()) ? "selected" : "" %>>女</option>
          </select>
        </div>
        <div class="form-group">
          <button type="submit">保存更改</button>
        </div>
      </form>
    </div>
  </div>
</div>

<div class="footer">
  <h2>Nothing Is Impossible</h2>
</div>

</div>
</body>
</html>