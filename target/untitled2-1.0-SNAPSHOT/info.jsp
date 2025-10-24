
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Info</title>
  <link rel="stylesheet" href="css/css/universal.css">
  <link rel="stylesheet" href="css/css/info.css" />
  <link rel="icon" href="images/error.png" type="image/x-icon" />
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
<div class="bk">
  <div class="box">
      <div style="font-size: larger"><%=outputMessage%></div>
    <a href="index.html" style="font-size: larger;color: black;opacity: 0.7">返回重新登录</a>
    <img src="images/cry.png" alt=""style="width: 20%;height: 25%;opacity: 0.5;border-radius:30%">
  </div>
</div>
<h1></h1>
</div>
</body>
</html>
