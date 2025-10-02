#!/bin/bash

echo "🛑 停止校园贴吧系统..."

# 停止 Tomcat
echo "🌐 停止 Tomcat..."
if pgrep -f "catalina" > /dev/null; then
    /usr/local/tomcat9/bin/catalina.sh stop
    sleep 2
    echo "   Tomcat 已停止"
else
    echo "   Tomcat 未在运行"
fi

# 可选：停止 MySQL（注释掉以保持 MySQL 运行）
# echo "📦 停止 MySQL..."
# brew services stop mysql

echo "✅ 停止完成！"
echo ""
echo "注意：MySQL 服务仍在运行（如需停止请运行: brew services stop mysql）"
