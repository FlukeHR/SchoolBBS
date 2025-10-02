<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bbs.service.PostTypeService" %>
<%@ page import="com.bbs.pojo.PostType" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <title>贴子类型管理</title>
  <link rel="stylesheet" href="css/admin.css">
  <link rel="icon" href="images/index.png" type="image/x-icon" />
</head>
<body style="position: absolute">
<h1>贴子类型管理</h1>
<button onclick="location.href='admin_index.html'" style="position: absolute;right: 390px">返回管理页</button>

<h2>类型列表</h2>
<table id="typeTable">
  <thead>
  <tr>
    <th>ID</th>
    <th>类型名称</th>
    <th>创建者</th>
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
      types = svc.findAllPostTypes();
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (types != null) {
      for (PostType t : types) {
  %>
  <tr>
    <td><%= t.getId() %></td>
    <td><%= t.getTypeName() %></td>
    <td><%= t.getCreator() %></td>
    <td><%= t.getCreateTime() %></td>
    <td><%= t.getDescription() %></td>
    <td>
      <form action="adminDeletePostType" method="post" onsubmit="return confirm('确认删除该贴子类型吗？删除后首页将不再显示该类型');">
        <input type="hidden" name="redirectPage" value="admin_postType.jsp">
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
