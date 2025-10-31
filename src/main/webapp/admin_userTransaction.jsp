<%@ page import="com.bbs.entity.User" %>
<%@ page import="com.bbs.service.impl.UserService" %>
<%@ page import="com.bbs.service.impl.RechargeService" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>用户交易管理</title>
    <link rel="stylesheet" href="css/universal.css">
    <link rel="stylesheet" href="css/modern-components.css">
    <style>
        .user-card {
            background: #fff;
            border: 1px solid #ddd;
            border-radius: 8px;
            margin-bottom: 15px;
            padding: 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .balance-info {
            display: flex;
            align-items: center;
            gap: 15px;
            margin: 10px 0;
        }
        .balance-amount {
            font-size: 18px;
            font-weight: bold;
            color: #2e7d32;
        }
        .action-buttons {
            display: flex;
            gap: 10px;
            margin-top: 15px;
        }
        .balance-form {
            display: none;
            margin-top: 15px;
            padding: 15px;
            background: #f5f5f5;
            border-radius: 6px;
        }
        .form-group {
            margin-bottom: 10px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        .form-group input, .form-group select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        .search-bar {
            margin-bottom: 20px;
            padding: 15px;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>用户交易管理</h1>
        <a href="admin_index.html" class="btn btn-secondary">返回后台首页</a>
    </div>

    <div class="search-bar">
        <form method="get" style="display: flex; gap: 10px; align-items: center;">
            <input type="text" name="search" placeholder="搜索用户名" 
                   value="<%= request.getParameter("search") != null ? request.getParameter("search") : "" %>"
                   style="flex: 1; padding: 8px; border: 1px solid #ccc; border-radius: 4px;">
            <button type="submit" class="btn btn-primary">搜索</button>
            <button type="button" onclick="batchUpdateBalance()" class="btn" style="background:#ff9800;color:white;">批量设置余额</button>
        </form>
    </div>

    <%
        UserService userService = new UserService();
        RechargeService rechargeService = new RechargeService();
        List<User> users;
        String searchParam = request.getParameter("search");
        
        try {
            if (searchParam != null && !searchParam.trim().isEmpty()) {
                users = userService.searchUsers(searchParam.trim());
            } else {
                users = userService.selectAll();
            }
            
            if (users != null && !users.isEmpty()) {
                for (User user : users) {
                    BigDecimal balance = BigDecimal.ZERO;
                    try {
                        balance = rechargeService.getUserBalance(user.getUsername());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
    %>
    <div class="user-card">
        <div style="display: flex; justify-content: space-between; align-items: center;">
            <div>
                <h3 style="color: black;"><%= user.getUsername() %></h3>
                <p style="color: black;">性别: <%= user.getSex() != null ? user.getSex() : "未设置" %></p>
                <p style="color: black;">注册时间: <%= user.getRtime() %></p>
            </div>
            <div class="balance-info">
                <span>当前余额:</span>
                <span class="balance-amount">¥<%= balance.toString() %></span>
            </div>
        </div>
        
        <div class="action-buttons">
            <button onclick="toggleBalanceForm('<%= user.getUsername() %>')" class="btn btn-primary">修改余额</button>
            <a href="admin_userTransactionHistory.jsp?username=<%= user.getUsername() %>" class="btn btn-primary" target="_blank">查看交易记录</a>
        </div>
        
        <div id="balanceForm_<%= user.getUsername() %>" class="balance-form">
            <form onsubmit="updateBalance(event, '<%= user.getUsername() %>')">
                <div class="form-group">
                    <label>操作类型:</label>
                    <select name="type" required>
                        <option value="ADD">增加余额</option>
                        <option value="SUBTRACT">减少余额</option>
                        <option value="SET">设置余额</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>金额:</label>
                    <input type="number" name="amount" step="0.01" min="0" required placeholder="请输入金额">
                </div>
                <div class="form-group">
                    <label>备注:</label>
                    <input type="text" name="remark" placeholder="操作原因（可选）">
                </div>
                <div style="display: flex; gap: 10px;">
                    <button type="submit" class="btn btn-primary">确认修改</button>
                    <button type="button" onclick="toggleBalanceForm('<%= user.getUsername() %>')" class="btn btn-secondary">取消</button>
                </div>
            </form>
        </div>
    </div>
    <%
                }
            } else {
    %>
    <div class="card">
        <p style="text-align: center; color: #666;">
            <%= searchParam != null && !searchParam.trim().isEmpty() ? "未找到匹配的用户" : "暂无用户数据" %>
        </p>
    </div>
    <%
            }
        } catch (Exception e) {
            e.printStackTrace();
    %>
    <div class="card">
        <p style="text-align: center; color: #f44336;">加载用户数据时发生错误: <%= e.getMessage() %></p>
    </div>
    <%
        }
    %>
</div>

<script>
function toggleBalanceForm(username) {
    const form = document.getElementById('balanceForm_' + username);
    if (form.style.display === 'none' || form.style.display === '') {
        form.style.display = 'block';
    } else {
        form.style.display = 'none';
    }
}

function batchUpdateBalance() {
    const amount = prompt('请输入要设置的余额金额（所有用户的余额将被设置为此金额）:', '50');
    
    if (amount === null) {
        return; // 用户取消
    }
    
    if (!amount || amount.trim() === '') {
        alert('请输入有效的金额');
        return;
    }
    
    if (isNaN(amount) || parseFloat(amount) < 0) {
        alert('请输入有效的金额');
        return;
    }
    
    if (confirm('确认要将所有用户的余额设置为 ¥' + amount + ' 吗？\\n此操作不可撤销！')) {
        fetch('adminBatchUpdateBalance', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'amount=' + encodeURIComponent(amount.trim())
        })
        .then(response => response.json())
        .then(result => {
            if (result.ok) {
                alert('批量更新成功！\\n' + result.msg);
                location.reload();
            } else {
                alert('操作失败：' + result.msg);
            }
        })
        .catch(error => {
            alert('操作失败：' + error.message);
        });
    }
}


function updateBalance(event, username) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    
    const amount = formData.get('amount');
    const type = formData.get('type');
    
    if (!amount || amount.trim() === '') {
        alert('请输入金额');
        return;
    }
    
    if (isNaN(amount) || parseFloat(amount) < 0) {
        alert('请输入有效的金额');
        return;
    }
    
    const data = {
        username: username,
        type: type,
        amount: amount.trim(),
        remark: formData.get('remark') || '管理员操作'
    };
    
    if (confirm('确认要修改用户 ' + username + ' 的余额吗？')) {
        fetch('adminUpdateBalance', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'username=' + encodeURIComponent(data.username) +
                  '&type=' + encodeURIComponent(data.type) +
                  '&amount=' + encodeURIComponent(data.amount) +
                  '&remark=' + encodeURIComponent(data.remark)
        })
        .then(response => response.json())
        .then(result => {
            if (result.ok) {
                alert('余额修改成功！');
                location.reload();
            } else {
                alert('操作失败：' + result.msg);
            }
        })
        .catch(error => {
            alert('操作失败：' + error.message);
        });
    }
}
</script>
</body>
</html>