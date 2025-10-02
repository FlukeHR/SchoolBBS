#!/bin/bash

echo "🚀 启动校园贴吧系统..."

# 检查并启动 MySQL
echo "📦 检查 MySQL 服务..."
if ! pgrep -x mysqld > /dev/null; then
    echo "   启动 MySQL..."
    brew services start mysql
    sleep 3
else
    echo "   MySQL 已在运行"
fi

# 检查并启动 Tomcat
echo "🌐 检查 Tomcat 服务..."
if ! pgrep -f "catalina" > /dev/null; then
    echo "   启动 Tomcat..."
    /usr/local/tomcat9/bin/catalina.sh start
    echo "   等待应用部署..."
    sleep 5
else
    echo "   Tomcat 已在运行"
fi

# 打开浏览器
echo "🌍 打开浏览器..."
open http://localhost:8080/untitled2/index.html

echo "✅ 启动完成！"
echo ""
echo "访问地址："
echo "  - 登录页面: http://localhost:8080/untitled2/index.html"
echo "  - 管理员登录: http://localhost:8080/untitled2/adminLogin.jsp"
echo ""
echo "停止服务请运行: ./stop.sh"
