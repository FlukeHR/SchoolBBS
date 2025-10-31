<%@ page import="com.bbs.entity.Recharge" %>
<%@ page import="com.bbs.entity.Trade" %>
<%@ page import="com.bbs.service.impl.RechargeService" %>
<%@ page import="com.bbs.service.impl.TradeService" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>用户交易记录</title>
    <link rel="stylesheet" href="css/universal.css">
    <link rel="stylesheet" href="css/modern-components.css">
    <style>
        body {
            color: black;
        }
        h1, h2, h3, h4 {
            color: black;
        }
        .transaction-info {
            color: black;
        }
        .user-info h2 {
            color: black;
        }
        .transaction-item {
            background: #fff;
            border: 1px solid #ddd;
            border-radius: 8px;
            margin-bottom: 10px;
            padding: 15px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .transaction-info {
            flex: 1;
        }
        .transaction-amount {
            font-weight: bold;
            font-size: 16px;
        }
        .amount-positive {
            color: #2e7d32;
        }
        .amount-negative {
            color: #c62828;
        }
        .transaction-status {
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: bold;
        }
        .status-success {
            background: #e8f5e8;
            color: #2e7d32;
        }
        .status-pending {
            background: #fff3e0;
            color: #f57c00;
        }
        .status-failed {
            background: #ffebee;
            color: #c62828;
        }
        .tab-container {
            margin-bottom: 20px;
        }
        .tab-buttons {
            display: flex;
            gap: 10px;
            margin-bottom: 15px;
        }
        .tab-button {
            padding: 10px 20px;
            border: 1px solid #ddd;
            background: #f5f5f5;
            cursor: pointer;
            border-radius: 6px;
        }
        .tab-button.active {
            background: #007bff;
            color: white;
            border-color: #007bff;
        }
        .tab-content {
            display: none;
        }
        .tab-content.active {
            display: block;
        }
        .user-info {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>用户交易记录</h1>
        <a href="admin_userTransaction.jsp" class="btn btn-secondary">返回用户交易管理</a>
    </div>

    <%
        String username = request.getParameter("username");
        if (username == null || username.trim().isEmpty()) {
    %>
    <div class="card">
        <p style="text-align: center; color: #f44336;">未指定用户名</p>
    </div>
    <%
        } else {
            RechargeService rechargeService = new RechargeService();
            TradeService tradeService = new TradeService();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    %>
    
    <div class="user-info">
        <h2>用户: <%= username %></h2>
        <%
            try {
                java.math.BigDecimal balance = rechargeService.getUserBalance(username);
        %>
        <p>当前余额: <span style="font-size: 18px; font-weight: bold; color: #2e7d32;">¥<%= balance.toString() %></span></p>
        <%
            } catch (Exception e) {
        %>
        <p>当前余额: <span style="color: #f44336;">获取失败</span></p>
        <%
            }
        %>
    </div>

    <div class="tab-container">
        <div class="tab-buttons">
            <div class="tab-button active" onclick="showTab('recharge')">充值记录</div>
            <div class="tab-button" onclick="showTab('purchase')">购买记录</div>
            <div class="tab-button" onclick="showTab('sales')">销售记录</div>
        </div>

        <!-- 充值记录 -->
        <div id="recharge" class="tab-content active">
            <h3>充值记录</h3>
            <%
                try {
                    List<Recharge> recharges = rechargeService.getUserRechargeHistory(username);
                    if (recharges != null && !recharges.isEmpty()) {
                        for (Recharge recharge : recharges) {
            %>
            <div class="transaction-item">
                <div class="transaction-info">
                    <div><strong>充值订单</strong> - <%= recharge.getOrderNo() %></div>
                    <div>支付方式: <%= "WECHAT".equals(recharge.getPaymentMethod()) ? "微信支付" : "支付宝" %></div>
                    <div>时间: <%= sdf.format(recharge.getCreateTime()) %></div>
                    <% if (recharge.getRemark() != null && !recharge.getRemark().isEmpty()) { %>
                    <div>备注: <%= recharge.getRemark() %></div>
                    <% } %>
                </div>
                <div>
                    <div class="transaction-amount amount-positive">+¥<%= recharge.getAmount().toString() %></div>
                    <div class="transaction-status <%= "SUCCESS".equals(recharge.getStatus()) ? "status-success" : "PENDING".equals(recharge.getStatus()) ? "status-pending" : "status-failed" %>">
                        <%= "SUCCESS".equals(recharge.getStatus()) ? "成功" : "PENDING".equals(recharge.getStatus()) ? "待支付" : "失败" %>
                    </div>
                </div>
            </div>
            <%
                        }
                    } else {
            %>
            <div class="card">
                <p style="text-align: center; color: #666;">暂无充值记录</p>
            </div>
            <%
                    }
                } catch (Exception e) {
            %>
            <div class="card">
                <p style="text-align: center; color: #f44336;">加载充值记录失败: <%= e.getMessage() %></p>
            </div>
            <%
                }
            %>
        </div>

        <!-- 购买记录 -->
        <div id="purchase" class="tab-content">
            <h3>购买记录</h3>
            <%
                try {
                    List<Trade> purchases = tradeService.listTradesByBuyer(username);
                    if (purchases != null && !purchases.isEmpty()) {
                        for (Trade trade : purchases) {
            %>
            <div class="transaction-item">
                <div class="transaction-info">
                    <div><strong>购买商品</strong> - <%= trade.getTitle() %></div>
                    <div>卖家: <%= trade.getAuthor() %></div>
                    <div>购买时间: <%= trade.getPaymentTime() != null ? sdf.format(trade.getPaymentTime()) : "未知" %></div>
                    <% if (trade.getDescription() != null && !trade.getDescription().isEmpty()) { %>
                    <div>描述: <%= trade.getDescription().length() > 50 ? trade.getDescription().substring(0, 50) + "..." : trade.getDescription() %></div>
                    <% } %>
                </div>
                <div>
                    <div class="transaction-amount amount-negative">-¥<%= trade.getPrice() != null ? trade.getPrice().toString() : "0" %></div>
                    <div class="transaction-status status-success">已完成</div>
                </div>
            </div>
            <%
                        }
                    } else {
            %>
            <div class="card">
                <p style="text-align: center; color: #666;">暂无购买记录</p>
            </div>
            <%
                    }
                } catch (Exception e) {
            %>
            <div class="card">
                <p style="text-align: center; color: #f44336;">加载购买记录失败: <%= e.getMessage() %></p>
            </div>
            <%
                }
            %>
        </div>

        <!-- 销售记录 -->
        <div id="sales" class="tab-content">
            <h3>销售记录</h3>
            <%
                try {
                    List<Trade> sales = tradeService.listTradesByAuthor(username);
                    if (sales != null && !sales.isEmpty()) {
                        for (Trade trade : sales) {
            %>
            <div class="transaction-item">
                <div class="transaction-info">
                    <div><strong>销售商品</strong> - <%= trade.getTitle() %></div>
                    <% if ("PAID".equals(trade.getStatus())) { %>
                    <div>买家: <%= trade.getBuyer() %></div>
                    <div>成交时间: <%= trade.getPaymentTime() != null ? sdf.format(trade.getPaymentTime()) : "未知" %></div>
                    <% } else { %>
                    <div>状态: <%= "PENDING".equals(trade.getStatus()) ? "待支付" : "AVAILABLE".equals(trade.getStatus()) ? "在售" : trade.getStatus() %></div>
                    <div>发布时间: <%= sdf.format(trade.getDate()) %></div>
                    <% } %>
                    <% if (trade.getDescription() != null && !trade.getDescription().isEmpty()) { %>
                    <div>描述: <%= trade.getDescription().length() > 50 ? trade.getDescription().substring(0, 50) + "..." : trade.getDescription() %></div>
                    <% } %>
                </div>
                <div>
                    <% if ("PAID".equals(trade.getStatus())) { %>
                    <div class="transaction-amount amount-positive">+¥<%= trade.getPrice() != null ? trade.getPrice().toString() : "0" %></div>
                    <div class="transaction-status status-success">已售出</div>
                    <% } else { %>
                    <div class="transaction-amount">¥<%= trade.getPrice() != null ? trade.getPrice().toString() : "0" %></div>
                    <div class="transaction-status <%= "PENDING".equals(trade.getStatus()) ? "status-pending" : "status-success" %>">
                        <%= "PENDING".equals(trade.getStatus()) ? "待支付" : "AVAILABLE".equals(trade.getStatus()) ? "在售" : trade.getStatus() %>
                    </div>
                    <% } %>
                </div>
            </div>
            <%
                        }
                    } else {
            %>
            <div class="card">
                <p style="text-align: center; color: #666;">暂无销售记录</p>
            </div>
            <%
                    }
                } catch (Exception e) {
            %>
            <div class="card">
                <p style="text-align: center; color: #f44336;">加载销售记录失败: <%= e.getMessage() %></p>
            </div>
            <%
                }
            %>
        </div>
    </div>

    <%
        }
    %>
</div>

<script>
function showTab(tabName) {
    // 隐藏所有标签内容
    const contents = document.querySelectorAll('.tab-content');
    contents.forEach(content => content.classList.remove('active'));
    
    // 移除所有按钮的激活状态
    const buttons = document.querySelectorAll('.tab-button');
    buttons.forEach(button => button.classList.remove('active'));
    
    // 显示选中的标签内容
    document.getElementById(tabName).classList.add('active');
    
    // 激活对应的按钮
    event.target.classList.add('active');
}
</script>
</body>
</html>