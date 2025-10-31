<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.util.*" %>
<%@ page import="com.bbs.service.impl.PostService" %>
<%@ page import="com.bbs.entity.Post" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>帖子类型</title>
  <link rel="stylesheet" href="css/universal.css" />
  <link rel="stylesheet" href="css/modern-components.css" />
  <link rel="stylesheet" href="css/home.css" />
  <link rel="icon" href="images/index.png" type="image/x-icon" />
  <style>
    .container{ max-width: 960px; margin: 0 auto; }
    .search-bar{ display:flex; gap:8px; margin:12px 0 16px; }
    .search-bar input[type=text]{ flex:1; padding:8px 10px; border:1px solid #ddd; border-radius:6px; }
    .search-bar button{ padding:8px 14px; border-radius:6px; border:1px solid #ddd; background:#fff; color:#333; font-weight:600; cursor:pointer; }
    .search-bar button:hover{ background:#f3f4f6; }
    .card{ padding:12px; border:1px solid #eee; border-radius:8px; box-shadow:0 2px 8px rgba(0,0,0,.04); margin-bottom:12px; }
    .post-row a{ text-decoration:none; color:#333; }
    .post-row a:hover{ color:#1f6feb; }
    .empty{ text-align:center; color:#777; padding:24px 0; }
  </style>
  <script src="js/modern-interactions.js"></script>
</head>
<body>
<div class="container">
  <div class="header">
    <h1>校园贴吧</h1>
    <p>Everything happens for a reason</p>
  </div>

  <div class="topnav">
    <ul>
      <li><a href="home.jsp">首页</a></li>
      <li><a href="mine.jsp">我的</a></li>
      <li><a href="publish.html">发帖</a></li>
      <li><a href="about.jsp">个人资料</a></li>
      <li><a href="search.jsp">搜索</a></li>
      <li><a href="trade.html">交易</a></li>
      <li><a href="home_login.jsp">退出登录</a></li>
    </ul>
  </div>

  <div class="container">
    <%! // 简单的HTML转义工具，避免外部依赖
        public static String escapeHtml(String s){
            if (s == null) return "";
            StringBuilder sb = new StringBuilder();
            for (int i=0;i<s.length();i++){
                char c = s.charAt(i);
                switch(c){
                    case '&': sb.append("&amp;"); break;
                    case '<': sb.append("&lt;"); break;
                    case '>': sb.append("&gt;"); break;
                    case '"': sb.append("&quot;"); break;
                    case '\'': sb.append("&#39;"); break;
                    default: sb.append(c);
                }
            }
            return sb.toString();
        }
    %>
    <%
      request.setCharacterEncoding("UTF-8");
      String type = request.getParameter("type");
      String q = request.getParameter("q");
      String pageParam = request.getParameter("page");
      String sizeParam = request.getParameter("size");
      String sort = request.getParameter("sort"); // date_desc | date_asc
      if (type == null) type = "";
      if (q == null) q = "";
      int pageNo = 1;
      int size = 10;
      if (sort == null || sort.trim().isEmpty()) sort = "date_desc";
      try { if (pageParam != null) pageNo = Math.max(1, Integer.parseInt(pageParam)); } catch(Exception ignore) {}
      try { if (sizeParam != null) size = Math.max(1, Math.min(50, Integer.parseInt(sizeParam))); } catch(Exception ignore) {}
      int offset = (pageNo - 1) * size;

      String typeTitle = type;

      List<Post> list = new ArrayList<>();
      int total = 0;
      try {
        PostService svc = new PostService();
        list = svc.searchPostsByTypePaged(type, q, offset, size, sort);
        total = svc.countPostsByTypeKeyword(type, q);
      } catch (Exception e) {
        e.printStackTrace();
      }

      int totalPages = (int) Math.ceil(total / (double) size);
      if (totalPages == 0) totalPages = 1;

      // 关键词高亮（对已转义后的字符串替换）
      String kw = q == null ? "" : q.trim();
      boolean hasKw = kw.length() > 0;
      String kwEsc = escapeHtml(kw);
      String markStart = "<mark>";
      String markEnd = "</mark>";
    %>

    <h2 style="margin-top:18px; display:flex; align-items:center; gap:10px;">
      <span><%= escapeHtml(typeTitle) %></span>
      <small style="color:#666;">类型主页</small>
    </h2>

    <form class="search-bar" method="get" action="type.jsp">
      <input type="hidden" name="type" value="<%= type %>">
      <input type="text" name="q" value="<%= escapeHtml(q) %>" placeholder="按关键词搜索该类型的帖子..." />
      <select name="sort">
        <option value="date_desc" <%= "date_desc".equals(sort)?"selected":"" %>>最新优先</option>
        <option value="date_asc" <%= "date_asc".equals(sort)?"selected":"" %>>最早优先</option>
      </select>
      <select name="size">
        <option value="10" <%= size==10?"selected":"" %>>每页10条</option>
        <option value="20" <%= size==20?"selected":"" %>>每页20条</option>
        <option value="30" <%= size==30?"selected":"" %>>每页30条</option>
      </select>
      <button type="submit">搜索</button>
    </form>

    <div>
      <%
        if (list != null && !list.isEmpty()) {
          for (Post p : list) {
            String author = p.getAuthor() != null ? p.getAuthor() : "匿名";
            String titleRaw = p.getTitle() == null ? "" : p.getTitle();
            String titleEsc = escapeHtml(titleRaw);
            if (hasKw) { titleEsc = titleEsc.replace(kwEsc, markStart + kwEsc + markEnd); }
      %>
      <div class="card post-row">
        <a href="post.jsp?id=<%= p.getId() %>">
          <%= author %> · <%= titleEsc %> · <%= p.getDate() != null ? p.getDate() : "" %>
        </a>
      </div>
      <%
          }
        } else {
      %>
      <div class="empty">暂无符合条件的帖子。</div>
      <%
        }
      %>
    </div>

    <div style="display:flex;align-items:center;gap:8px;justify-content:center;margin:16px 0;">
      <form method="get" action="type.jsp" style="display:inline;">
        <input type="hidden" name="type" value="<%= type %>">
        <input type="hidden" name="q" value="<%= escapeHtml(q) %>">
        <input type="hidden" name="page" value="<%= Math.max(1, pageNo-1) %>">
        <input type="hidden" name="size" value="<%= size %>">
        <input type="hidden" name="sort" value="<%= sort %>">
        <button type="submit" <%= pageNo<=1?"disabled":"" %>>上一页</button>
      </form>
      <span>第 <%= pageNo %> / <%= totalPages %> 页（共 <%= total %> 条）</span>
      <form method="get" action="type.jsp" style="display:inline;">
        <input type="hidden" name="type" value="<%= type %>">
        <input type="hidden" name="q" value="<%= escapeHtml(q) %>">
        <input type="hidden" name="page" value="<%= Math.min(totalPages, pageNo+1) %>">
        <input type="hidden" name="size" value="<%= size %>">
        <input type="hidden" name="sort" value="<%= sort %>">
        <button type="submit" <%= pageNo>=totalPages?"disabled":"" %>>下一页</button>
      </form>
    </div>
  </div>
</div>
</body>
</html>
