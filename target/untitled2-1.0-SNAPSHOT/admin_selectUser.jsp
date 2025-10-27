<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bbs.service.impl.UserServiceImpl" %>
<%@ page import="com.bbs.entity.User" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>用户管理</title>
    <link rel="stylesheet" href="css/universal.css">
    <link rel="stylesheet" href="css/modern-components.css">
    <link rel="icon" href="images/userManage.png" type="image/x-icon" />
    <script src="js/modern-interactions.js"></script>
</head>
<body>
<div class="container">
<div class="header">
    <h1>用户管理</h1>
    <button onclick="location.href='admin_index.html'" class="btn btn-secondary">返回管理页</button>
</div>
    <!-- 显示所有用户 -->
    <h2>用户列表</h2>
    <div class="card">
    <table id="usersTable" class="data-table" style="width: 100%; border-collapse: separate; border-spacing: 0; border: 1px solid var(--border-color);">
        <thead style="background-color: var(--bg-secondary);">
            <tr>
                <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 80px;">ID</th>
                <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 25%;">用户名</th>
                <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 15%;">性别</th>
                <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 50%;">操作</th>
            </tr>
        </thead>
        <tbody>
            <%
                UserServiceImpl userService = new UserServiceImpl();
                List<User> users = userService.selectAllUsers();
                for (User user : users) {
            %>
            <tr style="border-bottom: 1px solid var(--border-color);">
                <td style="padding: 12px; border-right: 1px solid var(--border-color);"><%= user.getUid() %></td>
                <td style="padding: 12px; border-right: 1px solid var(--border-color);"><%= user.getUsername() %></td>
                <td style="padding: 12px; border-right: 1px solid var(--border-color);"><%= user.getSex() %></td>
                <td style="padding: 12px;">
                    <form action="adminResetPassword" method="post" class="inline-form">
                        <input type="hidden" name="redirectPage" value="admin_selectUser.jsp">
                        <input type="hidden" name="uid" value="<%= user.getUid() %>">
                        <input type="hidden" name="userName" value="<%= user.getUsername() %>">
                        <input type="hidden" name="sex" value="<%= user.getSex() %>">
                        <input type="text" name="newPassword" placeholder="输入新密码" class="form-input form-input-small">
                        <button type="submit" class="btn btn-secondary">重置密码</button>
                    </form>
                    <form action="adminDeleteUser" method="post" class="inline-form" onsubmit="return confirm('确认删除该用户吗？此操作不可恢复');">
                        <input type="hidden" name="redirectPage" value="admin_selectUser.jsp">
                        <input type="hidden" name="uid" value="<%= user.getUid() %>">
                        <button type="submit" class="btn btn-danger">删除用户</button>
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