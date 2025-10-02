<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bbs.service.PostTypeService" %>
<%@ page import="com.bbs.pojo.PostType" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <title>我的贴吧</title>
  <link rel="stylesheet" href="css/admin.css">
  <link rel="icon" href="images/index.png" type="image/x-icon" />
</head>
<body style="position: absolute">
<h1>我的贴吧</h1>
<button onclick="location.href='home.jsp'" style="position: absolute;right: 390px">返回首页</button>

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

<h2>我创建的贴子类型</h2>

<div class="card" style="padding:12px;margin-bottom:12px;">
  <form action="PostTypeCreate" method="post" style="display:flex;gap:8px;align-items:center;flex-wrap:wrap;">
    <input type="hidden" name="redirectPage" value="my_postType.jsp">
    <input type="text" name="typeName" placeholder="类型名称" required>
    <input type="text" name="description" placeholder="描述">
    <button type="submit">新建类型</button>
  </form>
  <small>提示：新建后可在下表重命名或删除。删除会清空该类型下的所有帖子和评论。</small>
  <div>
    <a href="publish.html">去发帖</a>
  </div>
  <hr>
</div>
<table id="typeTable">
  <thead>
  <tr>
    <th>ID</th>
    <th>类型名称</th>
    <th>创建时间</th>
    <th>描述</th>
    <th>操作</th>
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
  <tr>
    <td><%= t.getId() %></td>
    <td><%= t.getTypeName() %></td>
    <td><%= t.getCreateTime() %></td>
    <td><%= t.getDescription() %></td>
    <td>
      <form action="userUpdatePostTypeName" method="post" style="display:inline-block;margin-right:8px;">
        <input type="hidden" name="redirectPage" value="my_postType.jsp">
        <input type="hidden" name="id" value="<%= t.getId() %>">
        <input type="text" name="newName" placeholder="新名称" required>
        <button type="submit">重命名</button>
      </form>
      <form action="userDeletePostType" method="post" style="display:inline-block;" onsubmit="return confirm('确认删除该类型吗？将清空此类型下所有帖子和评论，且不可恢复');">
        <input type="hidden" name="redirectPage" value="my_postType.jsp">
        <input type="hidden" name="id" value="<%= t.getId() %>">
        <button type="submit" style="background:#d9534f;color:white;border:none;padding:4px 10px;border-radius:3px;cursor:pointer;">删除</button>
      </form>
    </td>
  </tr>
  <%
      }
    }
  %>
  </tbody>
</table>
</body>
</html>
