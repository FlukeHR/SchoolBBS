<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bbs.service.PostTypeService" %>
<%@ page import="com.bbs.pojo.PostType" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>我的贴吧</title>
  <link rel="stylesheet" href="css/css/universal.css">
  <link rel="stylesheet" href="css/css/modern-components.css">
  <link rel="icon" href="images/index.png" type="image/x-icon" />
  <script src="js/modern-interactions.js"></script>
</head>
<body>
<div class="container">
<div class="header">
  <h1>我的贴吧</h1>
  <button onclick="location.href='home.jsp'" class="btn btn-secondary">返回首页</button>
</div>

<%
  HttpSession sess = request.getSession(false);
  if (sess == null || sess.getAttribute("username_session") == null) {
%>
<script>
  alert('请先登录');
  window.location.href = 'index.html';
</script>
<%
    return;
  }
  String currentUser = (String) sess.getAttribute("username_session");
%>

<h2 style="text-align: center;">我创建的贴子类型</h2>

<div class="card">
  <form action="PostTypeCreate" method="post" class="form-inline">
    <input type="hidden" name="redirectPage" value="my_postType.jsp">
    <input type="text" name="typeName" placeholder="类型名称" required class="form-input">
    <input type="text" name="description" placeholder="描述" class="form-input">
    <button type="submit" class="btn btn-primary">新建类型</button>
  </form>
  <small>提示：新建后可在下表重命名或删除。删除会清空该类型下的所有帖子和评论。</small>
  <div>
    <a href="publish.html">去发帖</a>
  </div>
  <hr>
</div>
<div class="card">
<table id="typeTable" class="data-table" style="width: 100%; border-collapse: separate; border-spacing: 0; border: 1px solid var(--border-color);">
  <thead style="background-color: var(--bg-secondary);">
  <tr>
    <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 80px;">ID</th>
    <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 20%;">类型名称</th>
    <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 20%;">创建时间</th>
    <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 30%;">描述</th>
    <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 20%;">操作</th>
  </tr>
  </thead>
  <tbody>
  <%
    PostTypeService svc = new PostTypeService();
    List<PostType> types = null;
    try {
      types = svc.findPostTypesByCreator(currentUser);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (types != null && !types.isEmpty()) {
      for (PostType t : types) {
  %>
  <tr style="border-bottom: 1px solid var(--border-color);">
    <td style="padding: 12px; border-right: 1px solid var(--border-color);"><%= t.getId() %></td>
    <td style="padding: 12px; border-right: 1px solid var(--border-color);"><%= t.getTypeName() %></td>
    <td style="padding: 12px; border-right: 1px solid var(--border-color);"><%= t.getCreateTime() %></td>
    <td style="padding: 12px; border-right: 1px solid var(--border-color);"><%= t.getDescription() %></td>
    <td style="padding: 12px;">
      <form action="userUpdatePostTypeName" method="post" class="inline-form">
        <input type="hidden" name="redirectPage" value="my_postType.jsp">
        <input type="hidden" name="id" value="<%= t.getId() %>">
        <input type="text" name="newName" placeholder="新名称" required class="form-input form-input-small">
        <button type="submit" class="btn btn-secondary">重命名</button>
      </form>
      <form action="userDeletePostType" method="post" class="inline-form" onsubmit="return confirm('确认删除该类型吗？将清空此类型下所有帖子和评论，且不可恢复');">
        <input type="hidden" name="redirectPage" value="my_postType.jsp">
        <input type="hidden" name="id" value="<%= t.getId() %>">
        <button type="submit" class="btn btn-danger">删除</button>
      </form>
    </td>
  </tr>
  <%
      }
    }
  %>
  </tbody>
</table>
</div>
</div>
</body>
</html>
