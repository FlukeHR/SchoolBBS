<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bbs.service.impl.PostTypeService" %>
<%@ page import="com.bbs.entity.PostType" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>贴子类型管理</title>
  <link rel="stylesheet" href="css/universal.css">
  <link rel="stylesheet" href="css/modern-components.css">
  <link rel="icon" href="images/index.png" type="image/x-icon" />
  <script src="js/modern-interactions.js"></script>
</head>
<body>
<div class="container">
<div class="header">
  <h1>贴子类型管理</h1>
  <button onclick="location.href='admin_index.html'" class="btn btn-secondary">返回管理页</button>
</div>
  <!-- 显示所有类型 -->
  <h2 style="text-align: center;">类型列表</h2>
  <div class="card">
<table id="typeTable" class="data-table" style="width: 100%; border-collapse: separate; border-spacing: 0; border: 1px solid var(--border-color);">
  <thead style="background-color: var(--bg-secondary);">
  <tr>
    <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 80px;">ID</th>
    <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 15%;">类型名称</th>
    <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 15%;">创建者</th>
    <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 15%;">创建时间</th>
    <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 30%;">描述</th>
    <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 10%;">操作</th>
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
  <tr style="border-bottom: 1px solid var(--border-color);">
    <td style="padding: 12px; border-right: 1px solid var(--border-color);"><%= t.getId() %></td>
    <td style="padding: 12px; border-right: 1px solid var(--border-color);"><%= t.getTypeName() %></td>
    <td style="padding: 12px; border-right: 1px solid var(--border-color);"><%= t.getCreator() %></td>
    <td style="padding: 12px; border-right: 1px solid var(--border-color);"><%= t.getCreateTime() %></td>
    <td style="padding: 12px; border-right: 1px solid var(--border-color);" class="content-preview"><%= t.getDescription() %></td>
    <td style="padding: 12px;">
      <form action="adminDeletePostType" method="post" class="inline-form" onsubmit="return confirm('确认删除该贴子类型吗？删除后首页将不再显示该类型');">
        <input type="hidden" name="redirectPage" value="admin_postType.jsp">
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
