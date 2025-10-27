<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bbs.service.impl.TradeService" %>
<%@ page import="com.bbs.entity.Trade" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>商品交易管理</title>
    <link rel="stylesheet" href="css/universal.css">
    <link rel="stylesheet" href="css/modern-components.css">
    <link rel="icon" href="images/admin.png" type="image/x-icon" />
    <script src="js/modern-interactions.js"></script>
    <style>
        .content-preview {
            max-width: 200px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
        .inline-form {
            display: inline;
            margin: 0;
        }
        .trade-image {
            width: 50px;
            height: 50px;
            object-fit: cover;
            border-radius: 4px;
        }
        .price-cell {
            color: #d23;
            font-weight: bold;
        }
    </style>
</head>
<body>
<div class="container">
<div class="header">
    <h1>商品交易管理</h1>
    <button onclick="location.href='admin_index.html'" class="btn btn-secondary">返回管理页</button>
</div>
    <!-- 显示所有商品 -->
    <h2>商品列表</h2>
    <div class="card">
    <table id="tradesTable" class="data-table" style="width: 100%; border-collapse: separate; border-spacing: 0; border: 1px solid var(--border-color);">
        <thead style="background-color: var(--bg-secondary);">
            <tr>
                <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 60px;">ID</th>
                <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 80px;">图片</th>
                <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 15%;">标题</th>
                <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 10%;">价格</th>
                <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 25%;">描述</th>
                <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 12%;">作者</th>
                <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 15%;">发布时间</th>
                <th style="padding: 12px; text-align: left; border-bottom: 1px solid var(--border-color); width: 8%;">操作</th>
            </tr>
        </thead>
        <tbody>
            <%
                TradeService tradeService = new TradeService();
                List<Trade> trades = tradeService.listTrades();
                for (Trade trade : trades) {
                    String firstImage = "";
                    if (trade.getImages() != null && !trade.getImages().isEmpty()) {
                        String[] images = trade.getImages().split(",");
                        if (images.length > 0) {
                            firstImage = images[0].trim();
                        }
                    }
            %>
            <tr style="border-bottom: 1px solid var(--border-color);">
                <td style="padding: 12px; border-right: 1px solid var(--border-color);"><%= trade.getId() %></td>
                <td style="padding: 12px; border-right: 1px solid var(--border-color);">
                    <% if (!firstImage.isEmpty()) { %>
                        <img src="<%= firstImage %>" alt="商品图片" class="trade-image" />
                    <% } else { %>
                        <div style="width: 50px; height: 50px; background: #f0f0f0; border-radius: 4px; display: flex; align-items: center; justify-content: center; font-size: 12px; color: #999;">无图</div>
                    <% } %>
                </td>
                <td style="padding: 12px; border-right: 1px solid var(--border-color);"><%= trade.getTitle() != null ? trade.getTitle() : "" %></td>
                <td style="padding: 12px; border-right: 1px solid var(--border-color);" class="price-cell">￥<%= trade.getPrice() != null ? trade.getPrice() : "0" %></td>
                <td style="padding: 12px; border-right: 1px solid var(--border-color);" class="content-preview"><%= trade.getDescription() != null ? trade.getDescription() : "" %></td>
                <td style="padding: 12px; border-right: 1px solid var(--border-color);"><%= trade.getAuthor() != null ? trade.getAuthor() : "" %></td>
                <td style="padding: 12px; border-right: 1px solid var(--border-color);"><%= trade.getDate() != null ? trade.getDate().toString().substring(0, 19) : "" %></td>
                <td style="padding: 12px;">
                    <form action="adminDeleteTrade" method="post" class="inline-form" onsubmit="return confirm('确认删除这个商品吗？');">
                        <input type="hidden" name="redirectPage" value="admin_selectTrade.jsp">
                        <input type="hidden" name="id" value="<%= trade.getId() %>">
                        <button type="submit" class="btn btn-danger">删除</button>
                    </form>
                </td>
            </tr>
            <%
                }
            %>
        </tbody>
    </table>
    </div>

    <% if (trades.isEmpty()) { %>
    <div class="card" style="text-align: center; padding: 40px;">
        <h3>暂无商品数据</h3>
        <p>目前还没有用户发布商品</p>
    </div>
    <% } %>

    <div class="card" style="margin-top: 20px; padding: 15px;">
        <h3>统计信息</h3>
        <p>总商品数量: <strong><%= trades.size() %></strong></p>
        <p>说明: 管理员可以删除不当的商品交易信息，确保平台交易的安全性。</p>
    </div>
</div>
</body>
</html>