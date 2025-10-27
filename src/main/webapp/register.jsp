<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="utf-8" />
  <meta name="author" content="rogepi" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>注册结果</title>
  <link rel="stylesheet" href="css/universal.css" />
  <link rel="stylesheet" href="css/info.css" />
  <link rel="stylesheet" href="css/modern-components.css" />
  <link rel="icon" href="images/register.png" type="image/x-icon" />
  <script src="js/modern-interactions.js"></script>
</head>
<body>
<div class="container">
<%--<h1><%=request.getAttribute("outputMessage")%></h1>--%>
<%
  String outputMessage = (String) request.getAttribute("outputMessage");
  if (outputMessage == null) {
    outputMessage = "操作失败";
  }
%>
  <div class="card">
    <h3>注册结果</h3>
    <div class="box">
      <div style="font-size: larger; color: var(--text-primary); margin-bottom: 20px;"><%=outputMessage%></div>
      <a href="signup.html" class="btn-primary" style="text-decoration: none; display: inline-block; margin-bottom: 20px;">返回重新注册</a>
      <img src="images/cry.png" alt="" style="width: 20%; height: 25%; opacity: 0.5; border-radius: 30%;">
    </div>
  </div>
</div>
</body>
</html>
