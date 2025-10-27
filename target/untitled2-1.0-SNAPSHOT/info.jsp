<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>提示信息 - 校园贴吧</title>
  <link rel="stylesheet" href="css/universal.css">
  <link rel="stylesheet" href="css/modern-components.css">
  <link rel="stylesheet" href="css/info.css" />
  <link rel="icon" href="images/error.png" type="image/x-icon" />
  <script src="js/modern-interactions.js"></script>
</head>
<body>
<div class="container">
  <div class="header">
    <h1>校园贴吧</h1>
    <p>Everything happens for a reason</p>
  </div>

  <%
    String outputMessage = (String) request.getAttribute("outputMessage");
    if (outputMessage == null) {
      outputMessage = "操作失败";
    }
  %>
  
  <div class="row">
    <div class="card" style="text-align: center; min-height: 200px; display: flex; flex-direction: column; align-items: center; justify-content: center;">
      <div style="margin-bottom: 20px;">
        <h3 style="color: #e53e3e; margin-bottom: 15px;">⚠️ 提示</h3>
        <p style="font-size: 16px; color: #2d3748; margin-bottom: 25px;"><%= outputMessage %></p>
      </div>
      <div style="display: flex; gap: 15px; flex-wrap: wrap; justify-content: center;">
        <a href="index.html" class="btn btn-primary">返回登录</a>
        <a href="signup.html" class="btn btn-secondary">注册账号</a>
      </div>
    </div>
  </div>

  <div class="footer">
    <h2>Nothing Is Impossible</h2>
  </div>
</div>
</body>
</html>